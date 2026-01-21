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
import app.krista.extensions.it.case_management.servicenow.service.User;
import app.krista.extensions.it.case_management.servicenow.service.UserDao;
import app.krista.extensions.it.case_management.servicenow.util.QueryBuilder;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jvnet.hk2.annotations.Service;

@Service
public class UserDaoImpl implements UserDao {


    public static final String API_NOW_V_1_TABLE = "/api/now/v1/table/";
    public static final String SYS_USER = "sys_user";
    public static final String API_NOW_V_1_TABLE_SYS_USER = "/api/now/v1/table/sys_user";
    public static final String SYSPARM_QUERY = "sysparm_query";
    public static final String USER_NAME = "user_name=";
    public static final String SYS_USER1 = "sys_user/";
    private final AuthService authService;

    @Inject
    public UserDaoImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public User getUserById(String id) {
        try {
            return UserImpl.create(
                    authService.getRestClient().get(ServiceNowUtil.getTableURL(SYS_USER1) + id,
                            new HashMap<>()).getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER, cause);
        }
    }

    @Override
    public List<User> getUsers(String url, long pageIndex , int pageSize,String operand, String operator ) {
        try {
            String endpointRaw =
                API_NOW_V_1_TABLE + SYS_USER + ServiceNowConstants.SYSPARM_LIMIT_2 +pageSize+ ServiceNowConstants.SYSPARM_OFFSET +pageIndex;
            endpointRaw=ServiceNowUtil.addOrderBy(operand) ? endpointRaw +ServiceNowConstants.ORDERBY + operand + operator:endpointRaw ;

            String endPoint = (url == null) ? endpointRaw: url;

            JSONObject jsonResponse =
                    authService.getRestClient().get(endPoint, new HashMap<>());
            return getUserList(jsonResponse.getJSONArray(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER, cause);
        }
    }

    @Override
    public List<User> getUsers(long pageIndex, int pageSize, SearchQuery searchQuery) {
        List<User> users = new ArrayList<>();
        StringBuilder finalQuery = new StringBuilder();
        String rootUrl = ServiceNowUtil.getTableURL(SYS_USER);
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

        if(searchConditions.size() == 1 && users.isEmpty()){
            return  getUsers(null,pageIndex,pageSize,operand,operator);
        }
        int count = 0 ;
        for (SearchCondition condition : searchConditions) {
            if(count == 0){count++;
                continue;
            }
            String eachQuery = QueryBuilder.buildQuery(condition.getFieldName(), String.valueOf(condition.getOperator()), String.valueOf(condition.getOperand()));
            finalQuery.append(eachQuery);
        }
        if(String.valueOf(finalQuery).endsWith(ServiceNowConstants.AND)){
            finalQuery = new StringBuilder(finalQuery.substring(0, finalQuery.length() - 4));
        }
        if (ServiceNowUtil.addOrderBy(operand)) {
            finalQuery.append(ServiceNowConstants.ORDERBY + operand + operator);
        }


        String url = String.valueOf(finalQuery).equals(ServiceNowConstants.SYSPARM_QUERY) ? rootUrl + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize
            + ServiceNowConstants.SYSPARM_LIMIT + pageIndex
            : rootUrl+String.valueOf(finalQuery) + ServiceNowConstants.SYSPARM_LIMIT + pageSize + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;

        return getUsers(url,0,0,null,null);

    }

    @Override
    public void deleteUser(String sysID) {
        authService.getRestClient().delete(ServiceNowUtil.getTableURL(SYS_USER1 + sysID), new HashMap<>());
    }

    @Override
    public String getUserID(String username) {
        String url = API_NOW_V_1_TABLE_SYS_USER;
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put(SYSPARM_QUERY, USER_NAME + username);
        try {
            JSONArray jsonArray =
                    authService.getRestClient().get(url, queryParams).getJSONArray(ServiceNowConstants.RESULT);
            JSONObject jsonRequest = jsonArray.getJSONObject(0);
            return UserImpl.findUserSysID(jsonRequest);
        } catch (JSONException cause) {
            throw new IllegalArgumentException(
                Errors.ERROR_OCCURRED_WHILE_FINDING_THE_SYSTEM_ID_OF_THE_USER, cause);
        }
    }

    private List<User> getUserList(JSONArray jsonRequestArray) throws JSONException {
        List<User> allUsers = new ArrayList<>();
        for (int i = 0; i <=jsonRequestArray.length() - 1; i++) {
            JSONObject jsonRequest = jsonRequestArray.getJSONObject(i);
            allUsers.add(UserImpl.create(jsonRequest));
        }
        return allUsers;
    }

}
