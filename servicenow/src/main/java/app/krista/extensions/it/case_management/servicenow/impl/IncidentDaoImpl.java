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

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extensions.it.case_management.servicenow.constants.Errors;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthService;
import app.krista.extensions.it.case_management.servicenow.service.Incident;
import app.krista.extensions.it.case_management.servicenow.service.IncidentDao;
import app.krista.extensions.it.case_management.servicenow.util.QueryBuilder;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Encoded;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncidentDaoImpl implements IncidentDao {

    public static final String API_NOW_V_1_TABLE = "/api/now/v1/table/";
    public static final String INCIDENT = "incident";
    public static final String ASSIGNED_TO = "assigned_to";
    public static final String VALUE = "value";
    public static final String CALLER_ID = "caller_id";

    public static final String INCIDENT1 = "incident/";
    public static final String SYS_USER = "sys_user/";
    public static final String SYSPARM_QUERY1 = "sysparm_query";
    public static final String NUMBER = "number";
    private final AuthService authService;
    private final UserDaoImpl userDaoImpl;
    private static final Logger LOG = LoggerFactory.getLogger(IncidentDaoImpl.class);

    @Encoded
    public static final String SYSPARM_QUERY = "?sysparm_query=";
    public static final String ORDERBY = "^ORDERBY";

    @Inject
    public IncidentDaoImpl(AuthService authService, UserDaoImpl userDaoImpl) {
        this.authService = authService;
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public List<Incident> getIncidents(long pageIndex, int pageSize, SearchQuery searchQuery) {
        List<Incident> incidents = new ArrayList<>();
        StringBuilder finalQuery = new StringBuilder();
        String rootUrl = ServiceNowUtil.getTableURL(INCIDENT);
        List<SearchCondition> searchConditions = searchQuery.getSearchConditions();
        List<QueryClause> queryClauses = searchQuery.getQueryClauses();

        if (searchConditions == null || searchConditions.isEmpty()) {
            throw new IllegalArgumentException(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN + ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY);
        }

        String operand = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
                QueryBuilder.serviceNowField.get(queryClauses.get(0).getFieldName())) : null;
        String operator = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
                queryClauses.get(0).getOperand()) : ServiceNowConstants.DESC;

        finalQuery.append(ServiceNowConstants.SYSPARM_QUERY);

        if (searchConditions.size() == 1 && incidents.isEmpty()) {
            return getIncidents(null, pageIndex, pageSize, operand, operator);
        }
        int count = 0;
        for (SearchCondition condition : searchConditions) {
            if (count == 0) {
                count++;
                continue;
            }
            String eachQuery = QueryBuilder.buildQuery(condition.getFieldName(), String.valueOf(condition.getOperator()), String.valueOf(condition.getOperand()));
            finalQuery.append(eachQuery);
        }
        if (String.valueOf(finalQuery).endsWith(ServiceNowConstants.AND)) {
            finalQuery = new StringBuilder(finalQuery.substring(0, finalQuery.length() - 4));
        }
        if (ServiceNowUtil.addOrderBy(operand)) {
            finalQuery.append(ServiceNowConstants.ORDERBY + operand + operator);
        }

        String url = String.valueOf(finalQuery).equals(SYSPARM_QUERY) ? rootUrl + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize
                + ServiceNowConstants.SYSPARM_LIMIT + pageIndex
                : rootUrl + String.valueOf(finalQuery) + ServiceNowConstants.SYSPARM_LIMIT + pageSize + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;

        incidents = getIncidents(url, 0, 0, null, null);
        return incidents;
    }


    @Override
    public List<Incident> getIncidents(String url, long pageIndex, int pageSize, String operand, String operator) {
        try {
            String endpointRaw =
                    API_NOW_V_1_TABLE + INCIDENT + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;
            endpointRaw = ServiceNowUtil.addOrderBy(operand) ? endpointRaw + ServiceNowConstants.ORDERBY + operand + operator : endpointRaw;

            String endPoint = (url == null) ? endpointRaw : url;

            JSONObject jsonResponse =
                    authService.getRestClient().get(endPoint, new HashMap<>());
            return getIncidentList(jsonResponse.getJSONArray(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENTS, cause);
        }
    }

    @Override
    public Incident getIncidentBySysId(String sysId) {
        try {
            JSONObject jsonIncident =
                    authService.getRestClient().get(ServiceNowUtil.getTableURL(INCIDENT1) + sysId, new HashMap<>())
                            .getJSONObject(ServiceNowConstants.RESULT);
            String assignedTo = null;
            String caller = null;
            if (jsonIncident.get(ASSIGNED_TO).getClass() != String.class) {
                assignedTo = userDaoImpl.getUserById(jsonIncident.getJSONObject(ASSIGNED_TO).getString(
                        VALUE)).toString();
            }

            if (jsonIncident.get(CALLER_ID).getClass() != String.class) {
                caller = userDaoImpl.getUserById(jsonIncident.getJSONObject(CALLER_ID).getString(
                        VALUE)).toString();
            }
            return IncidentImpl.create(jsonIncident, caller, assignedTo);
        } catch (JSONException e) {
            throw new IllegalArgumentException(
                    Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID, e);
        }
    }

    @Override
    public Incident getIncidentByIncidentId(String number) {
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(SYSPARM_QUERY1, NUMBER + "=" + number);
        try {
            JSONObject jsonIncident =
                    authService.getRestClient().get(ServiceNowUtil.getTableURL(INCIDENT), queryParams)
                            .getJSONArray(ServiceNowConstants.RESULT).getJSONObject(0);
            String assignedTo = null;
            String caller = null;
            if (jsonIncident.get(ASSIGNED_TO).getClass() != String.class) {
                assignedTo = userDaoImpl.getUserById(jsonIncident.getJSONObject(ASSIGNED_TO).getString(
                        VALUE)).toString();
            }
            if (jsonIncident.get(CALLER_ID).getClass() != String.class) {
                caller = userDaoImpl.getUserById(jsonIncident.getJSONObject(CALLER_ID).getString(
                        VALUE)).toString();
            }
            return IncidentImpl.create(jsonIncident, caller, assignedTo);
        } catch (JSONException cause) {
            throw new IllegalArgumentException(
                    Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID, cause);
        }

    }

    @Override
    public Incident createIncident(Map<String, Object> fields) {
        try {
            Incident incident = app.krista.extensions.it.case_management.servicenow.catalog.entities.Incident.createServiceNowIncident
                    (new app.krista.extensions.it.case_management.servicenow.catalog.entities.Incident(fields));
            JSONObject jsonIncident = authService.getRestClient().post(ServiceNowUtil.getTableURL(
                            INCIDENT),
                    IncidentImpl.getJSON(incident)).getJSONObject(ServiceNowConstants.RESULT);
            String assignedTo = null;
            String caller = null;
            if (jsonIncident.get(ASSIGNED_TO).getClass() != String.class) {
                assignedTo = userDaoImpl.getUserById(jsonIncident.getJSONObject(ASSIGNED_TO).getString(
                        VALUE)).toString();
            }
            if (jsonIncident.get(CALLER_ID).getClass() != String.class) {
                caller = userDaoImpl.getUserById(jsonIncident.getJSONObject(CALLER_ID).getString(
                        VALUE)).toString();
            }
            return IncidentImpl.create(jsonIncident, caller, assignedTo);
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT, cause);
        }
    }


    @Override
    public Incident updateIncident(Incident incidentToUpdate) {
        try {
            String sysId = incidentToUpdate.getSysId();
            JSONObject jsonIncident = authService.getRestClient().put(ServiceNowUtil.getTableURL(
                            INCIDENT1) + sysId,
                    IncidentImpl.getJSON(incidentToUpdate)).getJSONObject(ServiceNowConstants.RESULT);
            String assignedTo = null;
            String caller = null;
            if (jsonIncident.get(ASSIGNED_TO).getClass() != String.class) {
                assignedTo = userDaoImpl.getUserById(jsonIncident.getJSONObject(ASSIGNED_TO).getString(
                        VALUE)).toString();
            }
            if (jsonIncident.get(CALLER_ID).getClass() != String.class) {
                caller = userDaoImpl.getUserById(jsonIncident.getJSONObject(CALLER_ID).getString(
                        VALUE)).toString();
            }
            return IncidentImpl.create(jsonIncident, caller, assignedTo);
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT, cause);
        }
    }

    @Override
    public boolean assignUserToIncident(String sysIdUser, String sysIdIncident) {
        try {
            if (!userExists(sysIdUser)) return false;
            JSONObject jsonIncident = new JSONObject();
            jsonIncident.put(ASSIGNED_TO, sysIdUser);
            JSONObject jsonObject = authService.getRestClient()
                    .put(ServiceNowUtil.getTableURL(INCIDENT1) + sysIdIncident, jsonIncident);
            return jsonObject != null;
        } catch (JSONException cause) {
            LOG.error(cause.getMessage(), cause);
            return false;
        }
    }

    private boolean userExists(String sysIdUser) {
        JSONObject jsonUser =
                authService.getRestClient().get(ServiceNowUtil.getTableURL(SYS_USER) + sysIdUser, new HashMap<>());
        if (jsonUser == null) {
            return false;
        }
        return true;
    }

    private List<Incident> getIncidentList(JSONArray jsonArray) throws JSONException {
        List<Incident> allIncidents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonIncident = jsonArray.getJSONObject(i);
            String assignedTo = null;//
            String caller = null;
            if (jsonIncident.get(ASSIGNED_TO).getClass() != String.class) {
                String userSysId = jsonIncident.getJSONObject(ASSIGNED_TO).getString(VALUE);
                if (userExists(userSysId)) {
                    assignedTo = userDaoImpl.getUserById(userSysId).toString();
                }
            }

            if (jsonIncident.get(CALLER_ID).getClass() != String.class) {
                String userSysId = jsonIncident.getJSONObject(CALLER_ID).getString(VALUE);
                if (userExists(userSysId)) {
                    caller = userDaoImpl.getUserById(userSysId).toString()
                    ;
                }
            }
            allIncidents.add(IncidentImpl.create(jsonIncident, caller, assignedTo));
        }
        return allIncidents;
    }

}
