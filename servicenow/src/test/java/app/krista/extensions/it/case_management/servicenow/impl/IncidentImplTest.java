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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link IncidentImpl}.
 * <p>
 * Tests the creation and manipulation of Incident objects from JSON responses.
 * </p>
 */
class IncidentImplTest {

    @Test
    void testConstructor_WithAllParameters_CreatesIncident() {
        // Arrange
        String sysId = "inc123";
        String incidentId = "INC0001";
        String caller = "user123";
        String assignedTo = "admin456";
        String state = "New";
        String shortDescription = "Network issue";
        String description = "Unable to connect to network";
        String impact = "High";
        String urgency = "High";

        // Act
        IncidentImpl incident = new IncidentImpl(sysId, incidentId, caller, assignedTo,
            state, shortDescription, description, impact, urgency);

        // Assert
        assertNotNull(incident);
        assertEquals(sysId, incident.getSysId());
        assertEquals(incidentId, incident.getIncidentId());
        assertEquals(caller, incident.getCaller());
        assertEquals(assignedTo, incident.getAssignedTo());
        assertEquals(state, incident.getState());
        assertEquals(shortDescription, incident.getShortDescription());
        assertEquals(description, incident.getDescription());
        assertEquals(impact, incident.getImpact());
        assertEquals(urgency, incident.getUrgency());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        IncidentImpl incident = new IncidentImpl(null, null, null, null, null, null, null, null, null);

        // Assert
        assertNotNull(incident);
        assertNull(incident.getSysId());
        assertNull(incident.getIncidentId());
        assertNull(incident.getCaller());
    }

    @Test
    void testCreate_WithValidJsonObject_ReturnsIncident() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "inc123");
        jsonObject.put("number", "INC0001");
        jsonObject.put("incident_state", "1");
        jsonObject.put("short_description", "Network issue");
        jsonObject.put("description", "Unable to connect to network");
        jsonObject.put("impact", "1");
        jsonObject.put("urgency", "1");

        // Act
        IncidentImpl incident = IncidentImpl.create(jsonObject, "user123", "admin456");

        // Assert
        assertNotNull(incident);
        assertEquals("inc123", incident.getSysId());
        assertEquals("INC0001", incident.getIncidentId());
        assertEquals("user123", incident.getCaller());
        assertEquals("admin456", incident.getAssignedTo());
        assertEquals("New", incident.getState());
        assertEquals("Network issue", incident.getShortDescription());
        assertEquals("Unable to connect to network", incident.getDescription());
        assertEquals("High", incident.getImpact());
        assertEquals("High", incident.getUrgency());
    }

    @Test
    void testStateMapping_AllStates_MapCorrectly() {
        // Arrange & Act
        IncidentImpl newIncident = new IncidentImpl("sys1", "INC0001", "user1", "admin1",
            "New", "Desc", "Full desc", "High", "High");
        IncidentImpl inProgressIncident = new IncidentImpl("sys2", "INC0002", "user2", "admin2",
            "In Progress", "Desc", "Full desc", "Medium", "Medium");
        IncidentImpl onHoldIncident = new IncidentImpl("sys3", "INC0003", "user3", "admin3",
            "On Hold", "Desc", "Full desc", "Low", "Low");
        IncidentImpl resolvedIncident = new IncidentImpl("sys4", "INC0004", "user4", "admin4",
            "Resolved", "Desc", "Full desc", "High", "Medium");
        IncidentImpl closedIncident = new IncidentImpl("sys5", "INC0005", "user5", "admin5",
            "Closed", "Desc", "Full desc", "Medium", "Low");
        IncidentImpl cancelledIncident = new IncidentImpl("sys6", "INC0006", "user6", "admin6",
            "Cancelled", "Desc", "Full desc", "Low", "High");

        // Assert
        assertEquals("New", newIncident.getState());
        assertEquals("In Progress", inProgressIncident.getState());
        assertEquals("On Hold", onHoldIncident.getState());
        assertEquals("Resolved", resolvedIncident.getState());
        assertEquals("Closed", closedIncident.getState());
        assertEquals("Cancelled", cancelledIncident.getState());
    }

    @Test
    void testUrgencyAndImpactMapping_AllLevels_MapCorrectly() {
        // Arrange & Act
        IncidentImpl highIncident = new IncidentImpl("sys1", "INC0001", "user1", "admin1",
            "New", "Desc", "Full desc", "High", "High");
        IncidentImpl mediumIncident = new IncidentImpl("sys2", "INC0002", "user2", "admin2",
            "New", "Desc", "Full desc", "Medium", "Medium");
        IncidentImpl lowIncident = new IncidentImpl("sys3", "INC0003", "user3", "admin3",
            "New", "Desc", "Full desc", "Low", "Low");

        // Assert
        assertEquals("High", highIncident.getImpact());
        assertEquals("High", highIncident.getUrgency());
        assertEquals("Medium", mediumIncident.getImpact());
        assertEquals("Medium", mediumIncident.getUrgency());
        assertEquals("Low", lowIncident.getImpact());
        assertEquals("Low", lowIncident.getUrgency());
    }

    @Test
    void testGetJSON_WithValidIncident_ReturnsJsonObject() throws JSONException {
        // Arrange
        IncidentImpl incident = new IncidentImpl("sys123", "INC0001", "user123", "admin456",
            "New", "Network issue", "Unable to connect", "High", "High");

        // Act
        JSONObject jsonObject = IncidentImpl.getJSON(incident);

        // Assert
        assertNotNull(jsonObject);
        assertEquals("Network issue", jsonObject.getString("short_description"));
        assertEquals("1", jsonObject.getString("state"));
        assertEquals("1", jsonObject.getString("impact"));
        assertEquals("1", jsonObject.getString("urgency"));
        assertEquals("Unable to connect", jsonObject.getString("description"));
        assertEquals("admin456", jsonObject.getString("assigned_to"));
        assertEquals("user123", jsonObject.getString("caller_id"));
    }

    @Test
    void testGetJSON_WithNullFields_SkipsNullValues() throws JSONException {
        // Arrange
        IncidentImpl incident = new IncidentImpl("sys123", "INC0001", null, null,
            null, null, null, null, null);

        // Act
        JSONObject jsonObject = IncidentImpl.getJSON(incident);

        // Assert
        assertNotNull(jsonObject);
        assertFalse(jsonObject.has("short_description"));
        assertFalse(jsonObject.has("state"));
        assertFalse(jsonObject.has("impact"));
        assertFalse(jsonObject.has("urgency"));
        assertFalse(jsonObject.has("description"));
        assertFalse(jsonObject.has("assigned_to"));
        assertFalse(jsonObject.has("caller_id"));
    }

    @Test
    void testGetJSON_WithBlankFields_SkipsBlankValues() throws JSONException {
        // Arrange
        IncidentImpl incident = new IncidentImpl("sys123", "INC0001", "", "",
            "", "", "", "", "");

        // Act
        JSONObject jsonObject = IncidentImpl.getJSON(incident);

        // Assert
        assertNotNull(jsonObject);
        assertFalse(jsonObject.has("short_description"));
        assertFalse(jsonObject.has("state"));
        assertFalse(jsonObject.has("impact"));
        assertFalse(jsonObject.has("urgency"));
        assertFalse(jsonObject.has("description"));
        assertFalse(jsonObject.has("assigned_to"));
        assertFalse(jsonObject.has("caller_id"));
    }

    @Test
    void testGetJSON_WithDifferentStates_MapsCorrectly() throws JSONException {
        // Arrange
        IncidentImpl newIncident = new IncidentImpl("sys1", "INC0001", "user1", "admin1",
            "New", "Desc", "Full desc", "High", "High");
        IncidentImpl inProgressIncident = new IncidentImpl("sys2", "INC0002", "user2", "admin2",
            "In Progress", "Desc", "Full desc", "Medium", "Medium");
        IncidentImpl resolvedIncident = new IncidentImpl("sys3", "INC0003", "user3", "admin3",
            "Resolved", "Desc", "Full desc", "Low", "Low");

        // Act
        JSONObject newJson = IncidentImpl.getJSON(newIncident);
        JSONObject inProgressJson = IncidentImpl.getJSON(inProgressIncident);
        JSONObject resolvedJson = IncidentImpl.getJSON(resolvedIncident);

        // Assert
        assertEquals("1", newJson.getString("state"));
        assertEquals("2", inProgressJson.getString("state"));
        assertEquals("6", resolvedJson.getString("state"));
    }

    @Test
    void testGetJSON_WithDifferentImpactLevels_MapsCorrectly() throws JSONException {
        // Arrange
        IncidentImpl highImpact = new IncidentImpl("sys1", "INC0001", "user1", "admin1",
            "New", "Desc", "Full desc", "High", "High");
        IncidentImpl mediumImpact = new IncidentImpl("sys2", "INC0002", "user2", "admin2",
            "New", "Desc", "Full desc", "Medium", "Medium");
        IncidentImpl lowImpact = new IncidentImpl("sys3", "INC0003", "user3", "admin3",
            "New", "Desc", "Full desc", "Low", "Low");

        // Act
        JSONObject highJson = IncidentImpl.getJSON(highImpact);
        JSONObject mediumJson = IncidentImpl.getJSON(mediumImpact);
        JSONObject lowJson = IncidentImpl.getJSON(lowImpact);

        // Assert
        assertEquals("1", highJson.getString("impact"));
        assertEquals("2", mediumJson.getString("impact"));
        assertEquals("3", lowJson.getString("impact"));
    }

    @Test
    void testGetJSON_WithDifferentUrgencyLevels_MapsCorrectly() throws JSONException {
        // Arrange
        IncidentImpl highUrgency = new IncidentImpl("sys1", "INC0001", "user1", "admin1",
            "New", "Desc", "Full desc", "High", "High");
        IncidentImpl mediumUrgency = new IncidentImpl("sys2", "INC0002", "user2", "admin2",
            "New", "Desc", "Full desc", "Medium", "Medium");
        IncidentImpl lowUrgency = new IncidentImpl("sys3", "INC0003", "user3", "admin3",
            "New", "Desc", "Full desc", "Low", "Low");

        // Act
        JSONObject highJson = IncidentImpl.getJSON(highUrgency);
        JSONObject mediumJson = IncidentImpl.getJSON(mediumUrgency);
        JSONObject lowJson = IncidentImpl.getJSON(lowUrgency);

        // Assert
        assertEquals("1", highJson.getString("urgency"));
        assertEquals("2", mediumJson.getString("urgency"));
        assertEquals("3", lowJson.getString("urgency"));
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        IncidentImpl incident = new IncidentImpl("sys123", "INC0001", "user123", "admin456",
            "New", "Network issue", "Unable to connect", "High", "High");

        // Act & Assert
        assertEquals("sys123", incident.getSysId());
        assertEquals("INC0001", incident.getIncidentId());
        assertEquals("user123", incident.getCaller());
        assertEquals("admin456", incident.getAssignedTo());
        assertEquals("New", incident.getState());
        assertEquals("Network issue", incident.getShortDescription());
        assertEquals("Unable to connect", incident.getDescription());
        assertEquals("High", incident.getImpact());
        assertEquals("High", incident.getUrgency());
    }

    @Test
    void testStateKeyValues_ContainsAllExpectedStates() {
        // Assert
        assertEquals("New", IncidentImpl.stateKeyValues.get("1"));
        assertEquals("In Progress", IncidentImpl.stateKeyValues.get("2"));
        assertEquals("On Hold", IncidentImpl.stateKeyValues.get("3"));
        assertEquals("Resolved", IncidentImpl.stateKeyValues.get("6"));
        assertEquals("Closed", IncidentImpl.stateKeyValues.get("7"));
        assertEquals("Cancelled", IncidentImpl.stateKeyValues.get("8"));
    }

    @Test
    void testUrgencyOrImpactKeyValues_ContainsAllExpectedLevels() {
        // Assert
        assertEquals("High", IncidentImpl.urgencyORImpactKeyValues.get("1"));
        assertEquals("Medium", IncidentImpl.urgencyORImpactKeyValues.get("2"));
        assertEquals("Low", IncidentImpl.urgencyORImpactKeyValues.get("3"));
    }

    @Test
    void testCreate_WithDifferentStates_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "inc123");
        jsonObject.put("number", "INC0001");
        jsonObject.put("incident_state", "7");
        jsonObject.put("short_description", "Issue");
        jsonObject.put("description", "Description");
        jsonObject.put("impact", "2");
        jsonObject.put("urgency", "3");

        // Act
        IncidentImpl incident = IncidentImpl.create(jsonObject, "user123", "admin456");

        // Assert
        assertEquals("Closed", incident.getState());
        assertEquals("Medium", incident.getImpact());
        assertEquals("Low", incident.getUrgency());
    }

    @Test
    void testGetJSON_WithPartialData_HandlesCorrectly() throws JSONException {
        // Arrange
        IncidentImpl incident = new IncidentImpl("sys123", "INC0001", "user123", "",
            "New", "Network issue", "", "High", "");

        // Act
        JSONObject jsonObject = IncidentImpl.getJSON(incident);

        // Assert
        assertNotNull(jsonObject);
        assertTrue(jsonObject.has("short_description"));
        assertTrue(jsonObject.has("state"));
        assertTrue(jsonObject.has("impact"));
        assertTrue(jsonObject.has("caller_id"));
        assertFalse(jsonObject.has("description"));
        assertFalse(jsonObject.has("urgency"));
        assertFalse(jsonObject.has("assigned_to"));
    }
}

