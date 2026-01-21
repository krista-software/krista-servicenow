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

import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.service.Case;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class CaseImpl implements Case {

  private final String caseId;
  private final String channel;
  private final String priority;
  private final String accountId;
  private final String assignedTo;
  private final String shortDescription;
  private final String sysId;
  private final String state;

  public static final Map<String, String> stateKeyValues;
  public static final Map<String, String> channelKeyValues;

  static {
    stateKeyValues = new HashMap<>();
    stateKeyValues.put("1", "New");
    stateKeyValues.put("3", "Closed");
    stateKeyValues.put("6", "Resolved");
    stateKeyValues.put("10", "Open");
    stateKeyValues.put("18", "Awaiting Info");

    channelKeyValues = new HashMap<>();
    channelKeyValues.put("virtual_agent", "Virtual Agent");
    channelKeyValues.put("phone", "Phone");
    channelKeyValues.put("community", "Community");
    channelKeyValues.put("web", "Web");
    channelKeyValues.put("social", "Social");
    channelKeyValues.put("email", "Email");
    channelKeyValues.put("chat", "Chat");
    channelKeyValues.put("alert", "Alert");
  }

  public CaseImpl(String caseId, String channel, String priority, String accountId,
      String assignedTo, String shortDescription, String sysId, String state) {
    this.caseId = caseId;
    this.channel = channel;
    this.priority = priority;
    this.accountId = accountId;
    this.assignedTo = assignedTo;
    this.shortDescription = shortDescription;
    this.sysId = sysId;
    this.state = state;
  }

  public static CaseImpl create(JSONObject jsonCase) throws JSONException {
    String sysId = jsonCase.getString("sys_id");
    String caseId = jsonCase.getString("number");
    String state = getCaseState(jsonCase.getString("state"));
    String shortDescription =
        jsonCase.getString(Constants.SHORT_DESCRIPTION) == null ? ""
            : jsonCase.getString(Constants.SHORT_DESCRIPTION);
    String priority = ServiceNowUtil.getPriorityFromKey(jsonCase.getString("priority"));
    String channel = getChannelByKey(jsonCase.getString("contact_type"));
    String consumerId = jsonCase.getString("consumer");
    String assignedTo = jsonCase.getString("assigned_to");
    String accountId = "";
    if (!jsonCase.isNull(Constants.ACCOUNT)) {
      JSONObject jsonAccount = jsonCase.getJSONObject(Constants.ACCOUNT);
      if (jsonAccount != null) {
        accountId = jsonAccount.getString("name");
      }
    }
    return new CaseImpl(caseId, channel, priority, accountId,
        assignedTo, shortDescription, sysId, state);
  }

  public static JSONObject getJSON(Case caseToCreate) throws JSONException {
    JSONObject jsonCase = new JSONObject();
    jsonCase.put(Constants.SHORT_DESCRIPTION, caseToCreate.getShortDescription());
    String stateValue = ServiceNowUtil.getKeyFromValue(stateKeyValues, caseToCreate.getState());
    if (stateValue != null) {
      jsonCase.put("state", stateValue);
    }
    String priorityValue = ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues,
        caseToCreate.getPriority());
    if (priorityValue != null) {
      jsonCase.put("priority", priorityValue);
    }
    String channelValue = ServiceNowUtil.getKeyFromValue(channelKeyValues,
        caseToCreate.getChannel());
    if (channelValue != null) {
      jsonCase.put("contact_type", channelValue);
    }
    jsonCase.put("assigned_to", caseToCreate.getAssignedTo());
    jsonCase.put(Constants.ACCOUNT, caseToCreate.getAccountId());
    return jsonCase;
  }

  @Override
  public String getCaseId() {
    return caseId;
  }

  @Override
  public String getChannel() {
    return channel;
  }

  @Override
  public String getPriority() {
    return priority;
  }

  @Override
  public String getAccountId() {
    return accountId;
  }

  @Override
  public String getAssignedTo() {
    return assignedTo;
  }

  @Override
  public String getShortDescription() {
    return shortDescription;
  }

  @Override
  public String getSysId() {
    return sysId;
  }

  @Override
  public String getState() {
    return state;
  }

  private static String getCaseState(String number) {
    return stateKeyValues.get(number);
  }

  private static String getChannelByKey(String key) {
    return channelKeyValues.get(key);
  }

}
