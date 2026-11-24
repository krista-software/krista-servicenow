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
import app.krista.extensions.it.case_management.servicenow.service.Case;
import app.krista.extensions.it.case_management.servicenow.service.CaseDao;
import app.krista.extensions.it.case_management.servicenow.util.QueryBuilder;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jvnet.hk2.annotations.Service;

@Service
public class CaseDaoImpl implements CaseDao {


  private final AuthService authService;

  @Inject
  public CaseDaoImpl(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public Case getCaseById(String id) {
    try {
      JSONObject jsonObject = authService.getRestClient()
          .get(ServiceNowUtil.getCustomerServiceURL("case/") + id, new HashMap<>());
      return CaseImpl.create(jsonObject.getJSONObject(ServiceNowConstants.RESULT));
    } catch (JSONException cause) {
      throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FETCHING_THE_CASE, cause);
    }
  }

  @Override
  public List<Case> getCaseList(long pageIndex, int pageSize, SearchQuery searchQuery) {
    List<Case> cases = new ArrayList<>();
    StringBuilder finalQuery = new StringBuilder();
    String rootUrl = ServiceNowUtil.getCustomerServiceURL("case");
    List<SearchCondition> searchConditions = searchQuery.getSearchConditions();
    List<QueryClause> queryClauses = searchQuery.getQueryClauses();

    if (searchConditions == null || searchConditions.isEmpty()) {
      throw new IllegalArgumentException(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN
          + ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY);
    }
    String operand = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
        QueryBuilder.serviceNowField.get(queryClauses.get(0).getFieldName()) ): null;
    String operator = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
        queryClauses.get(0).getOperand()) : ServiceNowConstants.DESC;

    finalQuery.append(ServiceNowConstants.SYSPARM_QUERY);

    if (searchConditions.size() == 1 && cases.isEmpty()) {
      return getCaseList(null, pageIndex, pageSize ,operand,operator);
    }
    int count = 0;
    for (SearchCondition condition : searchConditions) {
      if (count == 0) {
        count++;
        continue;
      }
      String eachQuery = QueryBuilder.buildQuery(condition.getFieldName(),
          String.valueOf(condition.getOperator()), String.valueOf(condition.getOperand()));
      finalQuery.append(eachQuery);
    }

    if (String.valueOf(finalQuery).endsWith("AND")) {
      finalQuery = new StringBuilder(finalQuery.substring(0, finalQuery.length() - 4));
    }
    if (ServiceNowUtil.addOrderBy(operand)) {

        finalQuery.append(ServiceNowConstants.ORDERBY + operand + operator);

    }

    String url = String.valueOf(finalQuery).equals(ServiceNowConstants.SYSPARM_QUERY) ? rootUrl
        + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize
        + ServiceNowConstants.SYSPARM_LIMIT + pageIndex
        : rootUrl + String.valueOf(finalQuery) + ServiceNowConstants.SYSPARM_LIMIT + pageSize
            + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;


    cases = getCaseList(url, 0, 0,null,null);
    return cases;
  }

  @Override
  public List<Case> getCaseList(String url, long pageIndex, int pageSize ,String operand, String operator ) {
    Map<String, Object> queryParams = new HashMap<>();
    String endpointRaw =
        "/api/sn_customerservice/" + "case" + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize
            + ServiceNowConstants.SYSPARM_OFFSET + pageIndex  ;
   endpointRaw=ServiceNowUtil.addOrderBy(operand) ? endpointRaw +ServiceNowConstants.ORDERBY + operand + operator:endpointRaw ;

    String endPoint = (url == null) ? endpointRaw : url;
    JSONObject jsonResponse =
        authService.getRestClient().get(endPoint, queryParams);
    try {
      return getListOfCases(jsonResponse.getJSONArray(ServiceNowConstants.RESULT));
    } catch (JSONException cause) {
      throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES, cause);
    }
  }

  @Override
  public Case createCase(Map<String, Object> map) {
    try {
      JSONObject newCaseJson = authService.getRestClient()
          .post(ServiceNowUtil.getCustomerServiceURL("case"), prepareJSONCase(map));
      String sysId = newCaseJson.getJSONObject(ServiceNowConstants.RESULT).getString("sys_id");
      return getCaseById(sysId);
    } catch (JSONException cause) {
      throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE, cause);
    }
  }


  private JSONObject prepareJSONCase(Map<String, Object> map) throws JSONException {
    JSONObject jsonCase = new JSONObject();
    jsonCase.put("short_description", map.get(
        app.krista.extensions.it.case_management.servicenow.catalog.entities.Case.SHORT_DESCRIPTION));
    String stateValue = ServiceNowUtil.getKeyFromValue(CaseImpl.stateKeyValues, (String) map.get(
        app.krista.extensions.it.case_management.servicenow.catalog.entities.Case.STATE));
    if (stateValue != null) {
      jsonCase.put("state", stateValue);
    }
    String priorityValue = ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues,
        (String) map.get(
            app.krista.extensions.it.case_management.servicenow.catalog.entities.Case.PRIORITY));
    if (priorityValue != null) {
      jsonCase.put("priority", priorityValue);
    }
    String channelValue = ServiceNowUtil.getKeyFromValue(CaseImpl.channelKeyValues,
        (String) map.get("Channel"));
    if (channelValue != null) {
      jsonCase.put("contact_type", channelValue);
    }
    jsonCase.put("assigned_to", map.get(
        app.krista.extensions.it.case_management.servicenow.catalog.entities.Case.ASSIGNED_TO));
    jsonCase.put("account", map.get(
        app.krista.extensions.it.case_management.servicenow.catalog.entities.Case.CUSTOMER));
    return jsonCase;
  }

  @Override
  public Case updateCase(Case caseToUpdate) {
    try {
      JSONObject updatedCaseJson = authService.getRestClient()
          .put(ServiceNowUtil.getCustomerServiceURL("case/") + caseToUpdate.getSysId(),
              CaseImpl.getJSON(caseToUpdate));
      String sysId = updatedCaseJson.getJSONObject(ServiceNowConstants.RESULT).getString("sys_id");
      return getCaseById(sysId);
    } catch (JSONException cause) {
      throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_UPDATING_THE_CASE, cause);
    }
  }


  private List<Case> getListOfCases(JSONArray jsonArray) throws JSONException {
    List<Case> allCases = new ArrayList<>();
    for (int i = 0; i <= jsonArray.length() - 1; i++) {
      JSONObject jsonRequest = jsonArray.getJSONObject(i);
      allCases.add(CaseImpl.create(jsonRequest));
    }
    return allCases;
  }

}
