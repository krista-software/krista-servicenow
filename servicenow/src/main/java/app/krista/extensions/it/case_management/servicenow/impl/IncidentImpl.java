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

import app.krista.extensions.it.case_management.servicenow.service.Incident;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IncidentImpl implements Incident {

    private final String sysId;
    private final String incidentId;
    private final String caller;
    private final String assignedTo;
    private final String state;
    private final String shortDescription;
    private final String description;
    private final String impact;
    private final String urgency;

    public static final Map<String, String> stateKeyValues;
    public static final Map<String, String> urgencyORImpactKeyValues;

    static {
        stateKeyValues = new HashMap<>();
        stateKeyValues.put("1", "New");
        stateKeyValues.put("2", "In Progress");
        stateKeyValues.put("3", "On Hold");
        stateKeyValues.put("6", "Resolved");
        stateKeyValues.put("7", "Closed");
        stateKeyValues.put("8", "Cancelled");



        urgencyORImpactKeyValues = new HashMap<>();
        urgencyORImpactKeyValues.put("1", "High");
        urgencyORImpactKeyValues.put("2", "Medium");
        urgencyORImpactKeyValues.put("3", "Low");
    }

    public IncidentImpl(String sysId, String incidentId, String caller, String assignedTo,
            String state, String shortDescription, String description,
            String impact, String urgency) {
        this.sysId = sysId;
        this.incidentId = incidentId;
        this.caller = caller;
        this.assignedTo = assignedTo;
        this.state = state;
        this.shortDescription = shortDescription;
        this.description = description;
        this.impact = impact;
        this.urgency = urgency;
    }

    public static IncidentImpl create(JSONObject jsonIncident, String caller, String assignedTo) throws JSONException {
        String sysId = jsonIncident.getString("sys_id");
        String incidentId = jsonIncident.getString("number");
        String state = getIncidentState(jsonIncident.getString("incident_state"));
        String shortDescription = jsonIncident.getString("short_description");
        String description = jsonIncident.getString("description");
        String impact = getUrgencyORImpact(jsonIncident.getString("impact"));
        String urgency = getUrgencyORImpact(jsonIncident.getString("urgency"));
        return new IncidentImpl(sysId, incidentId, caller, assignedTo, state,
                shortDescription, description, impact, urgency);
    }

    public static JSONObject getJSON(Incident incident) throws JSONException {
        JSONObject jsonIncident = new JSONObject();
        if(incident.getShortDescription() != null && !incident.getShortDescription().isBlank()){
            jsonIncident.put("short_description", incident.getShortDescription());
        }
        if(incident.getState() != null && !incident.getState().isBlank()){
            jsonIncident.put("state", ServiceNowUtil.getKeyFromValue(stateKeyValues, incident.getState()));
        }
        if(incident.getImpact() != null && !incident.getImpact().isBlank()){
            jsonIncident.put("impact", ServiceNowUtil.getKeyFromValue(urgencyORImpactKeyValues, incident.getImpact()));
        }
        if(incident.getUrgency() != null && !incident.getUrgency().isBlank()){
            jsonIncident.put("urgency", ServiceNowUtil.getKeyFromValue(urgencyORImpactKeyValues, incident.getUrgency()));
        }
        if(incident.getDescription() != null && !incident.getDescription().isBlank()){
            jsonIncident.put("description", incident.getDescription());
        }
        if(incident.getAssignedTo() != null && !incident.getAssignedTo().isBlank()){
            jsonIncident.put("assigned_to", incident.getAssignedTo());
        }
        if(incident.getCaller() != null && !incident.getCaller().isBlank()){
            jsonIncident.put("caller_id", incident.getCaller());
        }
        return jsonIncident;
    }

    @Override
    public String getSysId() {
        return sysId;
    }

    @Override
    public String getIncidentId() {
        return incidentId;
    }

    @Override
    public String getCaller() {
        return caller;
    }

    @Override
    public String getAssignedTo() {
        return assignedTo;
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
    public String getImpact() {
        return impact;
    }

    @Override
    public String getUrgency() {
        return urgency;
    }

    private static String getIncidentState(String number) {
        return stateKeyValues.get(number);
    }

    private static String getUrgencyORImpact(String number) {
        return urgencyORImpactKeyValues.get(number);
    }

}
