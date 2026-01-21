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

package app.krista.extensions.it.case_management.servicenow.catalog.store;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.it.case_management.servicenow.catalog.entities.ChangeRequest;
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.constants.Errors;
import app.krista.extensions.it.case_management.servicenow.impl.ChangeRequestDaoImpl;
import app.krista.extensions.it.case_management.servicenow.impl.ChangeRequestImpl;
import app.krista.extensions.it.case_management.servicenow.impl.UserDaoImpl;
import app.krista.extensions.it.case_management.servicenow.util.JsonMapper;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static app.krista.extensions.it.case_management.servicenow.constants.Constants.CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW;
import static app.krista.extensions.it.case_management.servicenow.constants.Errors.ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST;

@Service
public class ChangeRequestStore implements EntityStore<ChangeRequest> {
    private static final Logger LOG = LogManager.getLogger(ChangeRequestStore.class.getName());
    public static final String STATE = "State";
    private final ChangeRequestDaoImpl changeRequestDaoImpl;
    private final UserDaoImpl userDaoImpl;

    @Inject
    public ChangeRequestStore(ChangeRequestDaoImpl changeRequestDaoImpl, UserDaoImpl userDaoImpl) {
        this.changeRequestDaoImpl = changeRequestDaoImpl;
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public ChangeRequest create(Map<String, Object> map) {
        try {
            if (map.get(STATE) == null || map.get(STATE).toString().isBlank() || map.get(STATE).toString().equalsIgnoreCase("New")) {
                LOG.info(CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW);
                return ChangeRequest.create(
                        changeRequestDaoImpl.createChangeRequest(map));
            }
            String assignmentGroup = (String) map.get(Constants.ASSIGNMENT_GROUP);
            if (assignmentGroup != null && !assignmentGroup.isBlank()) {
                ChangeRequest createdRequest = ChangeRequest.create(
                        changeRequestDaoImpl.createChangeRequest(map));
                return ChangeRequest.create(changeRequestDaoImpl.moveToAssess(createdRequest.sysID));
            } else {
                LOG.error(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST);
                throw new IllegalStateException(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST);
            }
        } catch (Exception cause) {
            LOG.error(ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST, cause);
            throw new RuntimeException(ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST, cause);
        }
    }

    @Override
    public ChangeRequest get(String sysID) {
        ChangeRequest newChangeRequest = ChangeRequest.create(changeRequestDaoImpl.getChangeRequestBySysID(sysID));
        LOG.info("Change Request fetched successfully for id = " + sysID);
        return newChangeRequest;
    }

    @Override
    public ChangeRequest update(ChangeRequest changeRequest) {
        return null;
    }

    @Override
    public void delete(String sysId) {
        changeRequestDaoImpl.deleteChangeRequest(sysId);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<ChangeRequest> search(List<SearchCondition> conditions, long pageIndex,
                                      int pageSize) {
        return Collections.emptyList();
    }

    @Override
    public List<ChangeRequest> search(SearchQuery searchQuery, long pageIndex, int pageSize)
            throws IOException {
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        Type fileListType = new TypeToken<ArrayList<SearchCondition>>() {
        }.getType();
        List<SearchCondition> searchConditions = JsonMapper.fromJson(JsonMapper.toJson(conditions),
                fileListType);
        List<ChangeRequest> changeList = new ArrayList<>();
        for (SearchCondition con : searchConditions) {
            switch (con.getFieldName()) {
                case ChangeRequest.REQUEST_ID:
                    searchByRequestID(searchConditions, changeList);
                    break;
                case ChangeRequest.SHORT_DESCRIPTION:
                    searchByShortDescription(pageIndex, pageSize, searchConditions, changeList);
                    break;
                case ChangeRequest.SYS_ID:
                    searchBySysID(searchConditions, changeList);
                    break;
                case ChangeRequest.STATE:
                    searchByState(pageIndex, pageSize, searchConditions, changeList);
                    break;
                case ChangeRequest.PRIORITY:
                    searchByPriority(pageIndex, pageSize, searchConditions, changeList);
                    break;
                case ChangeRequest.REQUESTED_BY:
                    searchByReporter(pageIndex, pageSize, searchConditions, changeList);
                    break;
                case ChangeRequest.ASSIGNMENT_GROUP:
                    searchByAssignmentGroup(pageIndex, pageSize, searchConditions, changeList);
                    break;
                case ChangeRequest.ASSIGNED_TO:
                    searchByAssignedTo(pageIndex, pageSize, searchConditions, changeList);

            }
        }
        if (conditions.size() == 1 && changeList.isEmpty()) {
            List<ChangeRequest> allChangeRequests = changeRequestDaoImpl.getRequests(new HashMap<>())
                    .stream().map(ChangeRequest::create).collect(Collectors.toList());
            filterSearchResult(pageIndex, pageSize, changeList, allChangeRequests);
        }
        return changeList;

    }

    private void searchByAssignedTo(long pageIndex, int pageSize,
                                    List<SearchCondition> searchConditions, List<ChangeRequest> changeList) {
        String assignedTo = ServiceNowUtil.getConditionValue(searchConditions,
                ChangeRequest.ASSIGNED_TO);
        String assignedToSysID = userDaoImpl.getUserID(assignedTo);
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constants.SYSPARM_QUERY, "assigned_to=" + assignedToSysID);
        List<ChangeRequest> changeRequestsByReporter = changeRequestDaoImpl.getRequests(queryParams)
                .stream().map(ChangeRequest::create).collect(Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequestsByReporter);
    }

    private void searchByAssignmentGroup(long pageIndex, int pageSize,
                                         List<SearchCondition> searchConditions, List<ChangeRequest> changeList) {
        String match = ServiceNowUtil.getConditionValue(searchConditions,
                ChangeRequest.ASSIGNMENT_GROUP);
        List<ChangeRequest> changeRequests = changeRequestDaoImpl.getRequests(new HashMap<>())
                .stream().map(ChangeRequest::create)
                .filter(c -> c.assignmentGroup.toLowerCase().contains(match)).collect(
                        Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequests);

    }

    private void searchByReporter(long pageIndex, int pageSize,
                                  List<SearchCondition> searchConditions,
                                  List<ChangeRequest> changeList) {
        String requestedBy = ServiceNowUtil.getConditionValue(searchConditions, "Requested By");
        String reporterSysID = userDaoImpl.getUserID(requestedBy);
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constants.SYSPARM_QUERY, "requested_by=" + reporterSysID);
        List<ChangeRequest> changeRequestsByReporter = changeRequestDaoImpl.getRequests(queryParams)
                .stream().map(ChangeRequest::create).collect(Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequestsByReporter);
    }

    private void filterSearchResult(long pageIndex, int pageSize, List<ChangeRequest> changeList,
                                    List<ChangeRequest> changeRequestsByReporter) {
        changeRequestsByReporter.stream().skip(Math.max((pageIndex - 1) * pageSize, 0)).limit(pageSize)
                .forEach(changeList::add);
    }

    private void searchByPriority(long pageIndex, int pageSize,
                                  List<SearchCondition> searchConditions,
                                  List<ChangeRequest> changeList) {
        String priority = ServiceNowUtil.getConditionValue(searchConditions, "Priority");
        String priorityID = ChangeRequestImpl.getPriorityID(priority);
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constants.SYSPARM_QUERY, "priority=" + priorityID);
        List<ChangeRequest> changeRequestsByPriority = changeRequestDaoImpl.getRequests(queryParams)
                .stream().map(ChangeRequest::create).collect(Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequestsByPriority);
    }

    private void searchByState(long pageIndex, int pageSize, List<SearchCondition> searchConditions,
                               List<ChangeRequest> changeList) {
        String state = ServiceNowUtil.getConditionValue(searchConditions, "State");
        String stateId = ChangeRequestImpl.getStateID(state);
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constants.SYSPARM_QUERY, "state=" + stateId);
        List<ChangeRequest> changeRequestsByState = changeRequestDaoImpl.getRequests(queryParams)
                .stream().map(ChangeRequest::create).collect(Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequestsByState);
    }

    private void searchBySysID(List<SearchCondition> searchConditions,
                               List<ChangeRequest> changeList) {
        String sysID = ServiceNowUtil.getConditionValue(searchConditions, "Sys ID");
        ChangeRequest changeRequestBySysId = ChangeRequest.create(
                changeRequestDaoImpl.getChangeRequestBySysID(sysID));
        changeList.add(changeRequestBySysId);
    }

    private void searchByShortDescription(long pageIndex, int pageSize,
                                          List<SearchCondition> searchConditions,
                                          List<ChangeRequest> changeList) {
        String shortDescription = ServiceNowUtil.getConditionValue(searchConditions,
                "Short Description");
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(Constants.SYSPARM_QUERY, "short_description=" + shortDescription);
        List<ChangeRequest> changeRequests = changeRequestDaoImpl.getRequests(queryParams)
                .stream().map(ChangeRequest::create).collect(Collectors.toList());
        filterSearchResult(pageIndex, pageSize, changeList, changeRequests);
    }

    private void searchByRequestID(List<SearchCondition> searchConditions,
                                   List<ChangeRequest> changeList) {
        String requestID = ServiceNowUtil.getConditionValue(searchConditions, ChangeRequest.REQUEST_ID);
        changeList.add(
                ChangeRequest.create(changeRequestDaoImpl.getChangeRequestByRequestID(requestID)));
    }

    @Override
    public long count(List<SearchCondition> list) {
        long newLong = changeRequestDaoImpl.getRequests(new HashMap<>()).size();
        LOG.info("Success,In Change Request,count Record fetched = " + newLong);
        return newLong;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return List.of();
    }

}
