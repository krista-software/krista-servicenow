/*
 * Servicenow Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.it.case_management.servicenow.impl;

import app.krista.extension.executor.SearchQuery;
import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.authentication.RestClient;
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.constants.Errors;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthService;
import app.krista.extensions.it.case_management.servicenow.service.ChangeRequest;
import app.krista.extensions.it.case_management.servicenow.service.ChangeRequestDao;
import app.krista.extensions.it.case_management.servicenow.service.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.*;

@Service
public class ChangeRequestDaoImpl implements ChangeRequestDao {


    private final AuthService authService;
    private final UserDaoImpl userDaoImpl;
    private final ServiceNowAttributes serviceNowAttributes;
    private static final Logger LOG = LogManager.getLogger(ChangeRequestDaoImpl.class.getName());

    @Inject
    public ChangeRequestDaoImpl(AuthService authService,
                                UserDaoImpl userDaoImpl, ServiceNowAttributes serviceNowAttributes) {
        this.authService = authService;
        this.userDaoImpl = userDaoImpl;
        this.serviceNowAttributes = serviceNowAttributes;
    }

    @Override
    public ChangeRequest createChangeRequest(Map<String, Object> map) {
        try {
            String url = ServiceNowConstants.CHANGE_URL;
            JSONObject requestBody = prepareJSONRequest(map);
            RestClient restClient = authService.getRestClient();
            return ChangeRequestImpl.create(
                    restClient.post(url, requestBody).getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException e) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST,
                    e);
        }
    }

    @Override
    public ChangeRequest moveToAssess(String id) {
        String url = ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL + id;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(Constants.STATE, "assess");
            RestClient restClient = authService.getRestClient();
            return ChangeRequestImpl.create(
                    restClient.patch(url, requestBody).getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            LOG.error(Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE, cause);
            throw new IllegalArgumentException(
                    Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE, cause);
        }
    }

    @Override
    public ChangeRequest escalateChangeRequest(String id) {
        String url = ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL + id;
        JSONObject requestBody = new JSONObject();
        try {
            JSONObject jsonRequest =
                    authService.getRestClient().get(url, new HashMap<>())
                            .getJSONObject(ServiceNowConstants.RESULT);
            String groupSysID = ChangeRequestImpl.findGroupSysID(jsonRequest);
            if (groupSysID == null || groupSysID.isEmpty()) {
                throw new IllegalArgumentException(Errors.GROUP_SYS_ID_CANNOT_BE_EMPTY_OR_NULL);
            }
            String manager = findManagerOfGroup(groupSysID);
            if (manager == null || manager.isEmpty()) {
                throw new IllegalArgumentException(Errors.MANAGER_CANNOT_BE_EMPTY_OR_NULL);
            }
            requestBody.put("assigned_to", manager);
            return ChangeRequestImpl.create(
                    authService.getRestClient().patch(url, requestBody)
                            .getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException e) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST, e);
        }
    }

    @Override
    public ChangeRequest getChangeRequestByRequestID(String id) {
        String url = ServiceNowConstants.CHANGE_URL;
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("sysparm_query", "number=" + id);
        JSONObject jsonRequests = authService.getRestClient().get(url, queryParams);
        try {
            return ChangeRequestImpl.create(
                    jsonRequests.getJSONArray(ServiceNowConstants.RESULT).getJSONObject(0));
        } catch (JSONException e) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST, e);
        }
    }

    @Override
    public ChangeRequest getChangeRequestBySysID(String id) {
        String url = ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL + id;
        HashMap<String, Object> queryParams = new HashMap<>();
        try {
            return ChangeRequestImpl.create(
                    authService.getRestClient().get(url, queryParams)
                            .getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException e) {
            throw new IllegalArgumentException(ServiceNowConstants.INVALID_INPUT, e.getCause());
        }
    }

    @Override
    public ChangeRequest approveChangeRequest(String id) {
        String url = "/api/now/table/change_request/" + id;
        RestClient restClient = authService.getRestClient();
        try {
            JSONObject requestBody = prepareRequestBodyForApproval();
            restClient.patch(url, requestBody);
            return getChangeRequestBySysID(id);
        } catch (JSONException e) {
            throw new IllegalArgumentException(ServiceNowConstants.INVALID_INPUT, e.getCause());
        }
    }

    private String findRequestInApprovalTable(String sysID) {

        String url = "/api/now/v1/table/sysapproval_approver";
        HashMap<String, Object> queryParams = new HashMap<>();
        JSONObject jsonRequests = authService.getRestClient().get(url, queryParams);
        try {
            JSONArray jsonRequestArray = jsonRequests.getJSONArray(ServiceNowConstants.RESULT);
            for (int i = 0; i <= jsonRequestArray.length() - 1; i++) {
                JSONObject jsonRequest = jsonRequestArray.getJSONObject(i);
                String documentID = ChangeRequestImpl.findDocumentID(jsonRequest);
                if (documentID.equals(sysID)) {
                    return ChangeRequestImpl.findApprovalSysID(jsonRequest);
                }
            }
            return null;
        } catch (JSONException e) {
            throw new IllegalArgumentException(ServiceNowConstants.INVALID_INPUT, e);
        }
    }

    @Override
    public List<ChangeRequest> getRequests(HashMap<String, Object> filters) {
        String url = ServiceNowConstants.CHANGE_URL;
        JSONObject jsonRequests = authService.getRestClient().get(url, filters);
        try {
            return getChangeRequestList(jsonRequests.getJSONArray(ServiceNowConstants.RESULT));
        } catch (JSONException e) {
            throw new IllegalArgumentException(ServiceNowConstants.INVALID_INPUT, e.getCause());
        }
    }

    @Override
    public List<ChangeRequest> getChangeRequests(long pageIndex, int pageSize,
                                                 SearchQuery searchQuery) {
        return Collections.emptyList();
    }

    @Override
    public void deleteChangeRequest(String id) {
        String url = ServiceNowConstants.CHANGE_URL + "/" + id;
        authService.getRestClient().delete(url, new HashMap<>());
        LOG.info("Change Request delete successfully executed");
    }

    private JSONObject prepareJSONRequest(Map<String, Object> map) throws JSONException {
        JSONObject requestBody = new JSONObject();
        if (map.get("Short Description") != null) {
            requestBody.put("short_description", map.get("Short Description"));
        }
        requestBody.put("assignment_group", map.get("Assignment group"));
        if (map.get("Priority") != null) {
            requestBody.put("priority", ChangeRequestImpl.getPriorityID((String) map.get("Priority")));
        }
        if (map.get("Risk") != null) {
            requestBody.put("risk", map.get("Risk"));
        }
        if (map.get("Model") != null) {
            requestBody.put("model", map.get("Model"));
        }
        if (map.get("Assigned to") != null) {
            requestBody.put("assigned_to", map.get("Assigned to"));
        }
        if (map.get("Impact") != null) {
            requestBody.put("impact", ChangeRequestImpl.getImpactID((String) map.get("Impact")));
        }
        return requestBody;
    }

    private List<ChangeRequest> getChangeRequestList(JSONArray jsonRequestArray)
            throws JSONException {
        List<ChangeRequest> allChangeRequests = new ArrayList<>();
        for (int i = 0; i < jsonRequestArray.length() - 1; i++) {
            JSONObject jsonRequest = jsonRequestArray.getJSONObject(i);
            allChangeRequests.add(ChangeRequestImpl.create(jsonRequest));
        }
        return allChangeRequests;
    }

    private String findManagerOfGroup(String groupSysID) {
        String url = "/api/now/v1/table/sys_user_group/" + groupSysID;
        try {
            JSONObject jsonRequest =
                    authService.getRestClient().get(url, new HashMap<>())
                            .getJSONObject(ServiceNowConstants.RESULT);
            String managerSysID = ChangeRequestImpl.findManagerSysID(jsonRequest);
            User manager = userDaoImpl.getUserById(managerSysID);
            return Objects.requireNonNull(manager).getFirstName() + " " + manager.getLastName();
        } catch (JSONException e) {
            throw new IllegalArgumentException(ServiceNowConstants.INVALID_INPUT, e.getCause());
        }
    }

    private JSONObject prepareRequestBodyForApproval() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("approval", "approved");
        return requestBody;
    }

    private JSONObject prepareRequestBodyForRequest() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put(Constants.STATE, "requested");
        String userSysID = userDaoImpl.getUserID((serviceNowAttributes.getUsername() == null || serviceNowAttributes.getUsername().isEmpty()) ? "admin" : serviceNowAttributes.getUsername());
        requestBody.put("approver", userSysID);
        return requestBody;
    }

}
