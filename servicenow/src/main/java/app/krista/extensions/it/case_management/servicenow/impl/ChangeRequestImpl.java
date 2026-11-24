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

import app.krista.extensions.it.case_management.servicenow.service.ChangeRequest;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ChangeRequestImpl implements ChangeRequest {

    private final String sysId;
    private final String requestId;
    private final String state;
    private final String shortDescription;
    private final String description;
    private final String risk;
    private final String priority;
    private final String impact;
    private final String assignmentGroup;
    private final String assignedTo;
    private final String model;
    private final String requestedBy;
    private final String approval;
    private static final String VALUE = "value";
    private static final String DISPLAY_VALUE = "display_value";
    public static final String SYS_ID = "sys_id";
    private static final String ASSIGNMENT_GROUP = "assignment_group";
    private static final String MANAGER = "manager";
    private static final String DOCUMENT_ID = "document_id";

    public static String findGroupSysID(JSONObject jsonRequest) throws JSONException {
        return String.valueOf(jsonRequest.getJSONObject(ASSIGNMENT_GROUP).get(VALUE));
    }

    public static String findManagerSysID(JSONObject jsonRequest) throws JSONException {
        return String.valueOf(jsonRequest.getJSONObject(MANAGER).get(VALUE));
    }
    int count= 0;

    public static String findDocumentID(JSONObject jsonRequest) throws JSONException {
        if(jsonRequest.getString(DOCUMENT_ID)==null || jsonRequest.getString(DOCUMENT_ID).isEmpty() || !jsonRequest.getString(DOCUMENT_ID).startsWith("{")) {
         return "";
        }
        return String.valueOf(jsonRequest.getJSONObject(DOCUMENT_ID).get(VALUE));
    }

    public static String findApprovalSysID(JSONObject jsonRequest) throws JSONException {
        if(jsonRequest.getString(SYS_ID)==null || jsonRequest.getString(SYS_ID).isEmpty() || !jsonRequest.getString(SYS_ID).startsWith("{")) {
            return "";
        }
        return String.valueOf(jsonRequest.get(SYS_ID));
    }

    public static String getStateID(String state) {
        if(state == null) return "";
        switch (state) {
            case "New":
                return "-5";
            case "Assess":
                return "-4";
            case "Authorize":
                return "-3";
            case "Scheduled":
                return "-2";
            case "Implement":
                return "-1";
            case "Review":
                return "0";
        }
        return "";
    }

    public static String getPriorityID(String priority) {
        if(priority == null) return "";
        switch (priority) {
            case "Low":
                return "4";
            case "Moderate":
                return "3";
            case "High":
                return "2";
            case "Critical":
                return "1";
        }
        return "";
    }

    public static String getImpactID(String impact) {
        if(impact == null) return "";
        switch (impact) {
            case "High":
                return "1";
            case "Medium":
                return "2";
            case "Low":
                return "3";
        }
        return "";
    }

    public ChangeRequestImpl(String sysId, String requestId, String state, String shortDescription,
            String description, String risk, String priority, String impact, String assignmentGroup,
            String assignedTo, String model, String requestedBy, String approval) {
        this.sysId = sysId;
        this.requestId = requestId;
        this.state = state;
        this.shortDescription = shortDescription;
        this.description = description;
        this.risk = risk;
        this.priority = priority;
        this.impact = impact;
        this.assignmentGroup = assignmentGroup;
        this.assignedTo = assignedTo;
        this.model = model;
        this.requestedBy = requestedBy;
        this.approval=approval;
    }

    public static ChangeRequestImpl create(JSONObject jsonChangeRequest) throws JSONException {
        String sysId = String.valueOf(jsonChangeRequest.getJSONObject("sys_id").get(VALUE));
        String requestId = String.valueOf(jsonChangeRequest.getJSONObject("number").get(VALUE));
        String state = String.valueOf(jsonChangeRequest.getJSONObject("state").get(DISPLAY_VALUE));
        String shortDescription = String.valueOf(jsonChangeRequest.getJSONObject("short_description").get(VALUE));
        String description = String.valueOf(jsonChangeRequest.getJSONObject("description").get(VALUE));
        String risk = String.valueOf(jsonChangeRequest.getJSONObject("risk").get(DISPLAY_VALUE));
        String priority = String.valueOf(jsonChangeRequest.getJSONObject("priority").get(DISPLAY_VALUE));
        String impact = String.valueOf(jsonChangeRequest.getJSONObject("impact").get(DISPLAY_VALUE));
        String assignmentGroup = String.valueOf(jsonChangeRequest.getJSONObject("assignment_group").get(DISPLAY_VALUE));
        String assignedTo = String.valueOf(jsonChangeRequest.getJSONObject("assigned_to").get(DISPLAY_VALUE));
        String model = String.valueOf(jsonChangeRequest.getJSONObject("chg_model").get(DISPLAY_VALUE));
        String requestedBy = String.valueOf(jsonChangeRequest.getJSONObject("requested_by").get(DISPLAY_VALUE));
        String approval = String.valueOf(jsonChangeRequest.getJSONObject("approval").get(DISPLAY_VALUE));
        return new ChangeRequestImpl(sysId, requestId, state, shortDescription, description, risk, priority,
                impact, assignmentGroup, assignedTo, model, requestedBy,approval);
    }

    @Override
    public String getSysId() {
        return sysId;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getShortDescription() {
        return shortDescription;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getRisk() {
        return risk;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public String getImpact() {
        return impact;
    }

    @Override
    public String getAssignmentGroup() {
        return assignmentGroup;
    }

    @Override
    public String getAssignedTo() {
        return assignedTo;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getRequestedBy() {
        return requestedBy;
    }

    @Override
    public String getApproval() {
        return approval;
    }

}
