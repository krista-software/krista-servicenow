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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ChangeRequestImpl}.
 * <p>
 * Tests the creation and manipulation of ChangeRequest objects from JSON responses.
 * </p>
 */
class ChangeRequestImplTest {

    @Test
    void testConstructor_WithAllParameters_CreatesChangeRequest() {
        // Arrange
        String sysId = "chg123";
        String requestId = "CHG0001";
        String state = "New";
        String shortDescription = "Database upgrade";
        String description = "Upgrade database to version 12";
        String risk = "Medium";
        String priority = "High";
        String impact = "High";
        String assignmentGroup = "Database Team";
        String assignedTo = "admin123";
        String model = "Standard";
        String requestedBy = "user456";
        String approval = "Approved";

        // Act
        ChangeRequestImpl changeRequest = new ChangeRequestImpl(sysId, requestId, state,
            shortDescription, description, risk, priority, impact, assignmentGroup,
            assignedTo, model, requestedBy, approval);

        // Assert
        assertNotNull(changeRequest);
        assertEquals(sysId, changeRequest.getSysId());
        assertEquals(requestId, changeRequest.getRequestId());
        assertEquals(state, changeRequest.getState());
        assertEquals(shortDescription, changeRequest.getShortDescription());
        assertEquals(description, changeRequest.getDescription());
        assertEquals(risk, changeRequest.getRisk());
        assertEquals(priority, changeRequest.getPriority());
        assertEquals(impact, changeRequest.getImpact());
        assertEquals(assignmentGroup, changeRequest.getAssignmentGroup());
        assertEquals(assignedTo, changeRequest.getAssignedTo());
        assertEquals(model, changeRequest.getModel());
        assertEquals(requestedBy, changeRequest.getRequestedBy());
        assertEquals(approval, changeRequest.getApproval());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        ChangeRequestImpl changeRequest = new ChangeRequestImpl(null, null, null, null,
            null, null, null, null, null, null, null, null, null);

        // Assert
        assertNotNull(changeRequest);
        assertNull(changeRequest.getSysId());
        assertNull(changeRequest.getRequestId());
        assertNull(changeRequest.getState());
    }

    @Test
    void testCreate_WithValidJsonObject_ReturnsChangeRequest() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", createValueObject("chg123"));
        jsonObject.put("number", createValueObject("CHG0001"));
        jsonObject.put("state", createDisplayValueObject("New"));
        jsonObject.put("short_description", createValueObject("Database upgrade"));
        jsonObject.put("description", createValueObject("Upgrade database to version 12"));
        jsonObject.put("risk", createDisplayValueObject("Medium"));
        jsonObject.put("priority", createDisplayValueObject("High"));
        jsonObject.put("impact", createDisplayValueObject("High"));
        jsonObject.put("assignment_group", createDisplayValueObject("Database Team"));
        jsonObject.put("assigned_to", createDisplayValueObject("admin123"));
        jsonObject.put("chg_model", createDisplayValueObject("Standard"));
        jsonObject.put("requested_by", createDisplayValueObject("user456"));
        jsonObject.put("approval", createDisplayValueObject("Approved"));

        // Act
        ChangeRequestImpl changeRequest = ChangeRequestImpl.create(jsonObject);

        // Assert
        assertNotNull(changeRequest);
        assertEquals("chg123", changeRequest.getSysId());
        assertEquals("CHG0001", changeRequest.getRequestId());
        assertEquals("New", changeRequest.getState());
        assertEquals("Database upgrade", changeRequest.getShortDescription());
        assertEquals("Upgrade database to version 12", changeRequest.getDescription());
        assertEquals("Medium", changeRequest.getRisk());
        assertEquals("High", changeRequest.getPriority());
        assertEquals("High", changeRequest.getImpact());
        assertEquals("Database Team", changeRequest.getAssignmentGroup());
        assertEquals("admin123", changeRequest.getAssignedTo());
        assertEquals("Standard", changeRequest.getModel());
        assertEquals("user456", changeRequest.getRequestedBy());
        assertEquals("Approved", changeRequest.getApproval());
    }

    @Test
    void testGetStateID_AllStates_ReturnCorrectIDs() {
        // Act & Assert
        assertEquals("-5", ChangeRequestImpl.getStateID("New"));
        assertEquals("-4", ChangeRequestImpl.getStateID("Assess"));
        assertEquals("-3", ChangeRequestImpl.getStateID("Authorize"));
        assertEquals("-2", ChangeRequestImpl.getStateID("Scheduled"));
        assertEquals("-1", ChangeRequestImpl.getStateID("Implement"));
        assertEquals("0", ChangeRequestImpl.getStateID("Review"));
    }

    @Test
    void testGetStateID_WithNullState_ReturnsEmptyString() {
        // Act
        String result = ChangeRequestImpl.getStateID(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testGetStateID_WithUnknownState_ReturnsEmptyString() {
        // Act
        String result = ChangeRequestImpl.getStateID("Unknown");

        // Assert
        assertEquals("", result);
    }

    @Test
    void testFindApprovalSysID_WithValidSysId_ReturnsSysId() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "{approval123}");

        // Act
        String sysId = ChangeRequestImpl.findApprovalSysID(jsonObject);

        // Assert
        assertEquals("{approval123}", sysId);
    }

    @Test
    void testFindApprovalSysID_WithNullSysId_ReturnsEmptyString() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", JSONObject.NULL);

        // Act
        String sysId = ChangeRequestImpl.findApprovalSysID(jsonObject);

        // Assert
        assertEquals("", sysId);
    }

    @Test
    void testFindApprovalSysID_WithEmptySysId_ReturnsEmptyString() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "");

        // Act
        String sysId = ChangeRequestImpl.findApprovalSysID(jsonObject);

        // Assert
        assertEquals("", sysId);
    }

    @Test
    void testFindApprovalSysID_WithoutBraces_ReturnsEmptyString() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "approval123");

        // Act
        String sysId = ChangeRequestImpl.findApprovalSysID(jsonObject);

        // Assert
        assertEquals("", sysId);
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        ChangeRequestImpl changeRequest = new ChangeRequestImpl("chg123", "CHG0001", "New",
            "Database upgrade", "Upgrade database", "Medium", "High", "High",
            "Database Team", "admin123", "Standard", "user456", "Approved");

        // Act & Assert
        assertEquals("chg123", changeRequest.getSysId());
        assertEquals("CHG0001", changeRequest.getRequestId());
        assertEquals("New", changeRequest.getState());
        assertEquals("Database upgrade", changeRequest.getShortDescription());
        assertEquals("Upgrade database", changeRequest.getDescription());
        assertEquals("Medium", changeRequest.getRisk());
        assertEquals("High", changeRequest.getPriority());
        assertEquals("High", changeRequest.getImpact());
        assertEquals("Database Team", changeRequest.getAssignmentGroup());
        assertEquals("admin123", changeRequest.getAssignedTo());
        assertEquals("Standard", changeRequest.getModel());
        assertEquals("user456", changeRequest.getRequestedBy());
        assertEquals("Approved", changeRequest.getApproval());
    }

    @Test
    void testCreate_WithDifferentStates_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject assessJson = new JSONObject();
        assessJson.put("sys_id", createValueObject("chg1"));
        assessJson.put("number", createValueObject("CHG0001"));
        assessJson.put("state", createDisplayValueObject("Assess"));
        assessJson.put("short_description", createValueObject("Desc"));
        assessJson.put("description", createValueObject("Full desc"));
        assessJson.put("risk", createDisplayValueObject("Low"));
        assessJson.put("priority", createDisplayValueObject("Medium"));
        assessJson.put("impact", createDisplayValueObject("Low"));
        assessJson.put("assignment_group", createDisplayValueObject("Team"));
        assessJson.put("assigned_to", createDisplayValueObject("admin"));
        assessJson.put("chg_model", createDisplayValueObject("Standard"));
        assessJson.put("requested_by", createDisplayValueObject("user"));
        assessJson.put("approval", createDisplayValueObject("Pending"));

        // Act
        ChangeRequestImpl changeRequest = ChangeRequestImpl.create(assessJson);

        // Assert
        assertEquals("Assess", changeRequest.getState());
    }

    @Test
    void testCreate_WithDifferentRiskLevels_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject highRiskJson = new JSONObject();
        highRiskJson.put("sys_id", createValueObject("chg1"));
        highRiskJson.put("number", createValueObject("CHG0001"));
        highRiskJson.put("state", createDisplayValueObject("New"));
        highRiskJson.put("short_description", createValueObject("Desc"));
        highRiskJson.put("description", createValueObject("Full desc"));
        highRiskJson.put("risk", createDisplayValueObject("High"));
        highRiskJson.put("priority", createDisplayValueObject("Critical"));
        highRiskJson.put("impact", createDisplayValueObject("High"));
        highRiskJson.put("assignment_group", createDisplayValueObject("Team"));
        highRiskJson.put("assigned_to", createDisplayValueObject("admin"));
        highRiskJson.put("chg_model", createDisplayValueObject("Emergency"));
        highRiskJson.put("requested_by", createDisplayValueObject("user"));
        highRiskJson.put("approval", createDisplayValueObject("Approved"));

        // Act
        ChangeRequestImpl changeRequest = ChangeRequestImpl.create(highRiskJson);

        // Assert
        assertEquals("High", changeRequest.getRisk());
        assertEquals("Critical", changeRequest.getPriority());
        assertEquals("Emergency", changeRequest.getModel());
    }

    @Test
    void testCreate_WithDifferentApprovalStates_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject rejectedJson = new JSONObject();
        rejectedJson.put("sys_id", createValueObject("chg1"));
        rejectedJson.put("number", createValueObject("CHG0001"));
        rejectedJson.put("state", createDisplayValueObject("New"));
        rejectedJson.put("short_description", createValueObject("Desc"));
        rejectedJson.put("description", createValueObject("Full desc"));
        rejectedJson.put("risk", createDisplayValueObject("Low"));
        rejectedJson.put("priority", createDisplayValueObject("Low"));
        rejectedJson.put("impact", createDisplayValueObject("Low"));
        rejectedJson.put("assignment_group", createDisplayValueObject("Team"));
        rejectedJson.put("assigned_to", createDisplayValueObject("admin"));
        rejectedJson.put("chg_model", createDisplayValueObject("Standard"));
        rejectedJson.put("requested_by", createDisplayValueObject("user"));
        rejectedJson.put("approval", createDisplayValueObject("Rejected"));

        // Act
        ChangeRequestImpl changeRequest = ChangeRequestImpl.create(rejectedJson);

        // Assert
        assertEquals("Rejected", changeRequest.getApproval());
    }

    @Test
    void testGetStateID_WithAllValidStates_ReturnsCorrectMapping() {
        // Arrange
        String[] states = {"New", "Assess", "Authorize", "Scheduled", "Implement", "Review"};
        String[] expectedIds = {"-5", "-4", "-3", "-2", "-1", "0"};

        // Act & Assert
        for (int i = 0; i < states.length; i++) {
            assertEquals(expectedIds[i], ChangeRequestImpl.getStateID(states[i]),
                "State " + states[i] + " should map to " + expectedIds[i]);
        }
    }

    @Test
    void testCreate_WithComplexChangeRequest_HandlesAllFields() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", createValueObject("chg999"));
        jsonObject.put("number", createValueObject("CHG0999"));
        jsonObject.put("state", createDisplayValueObject("Implement"));
        jsonObject.put("short_description", createValueObject("Major system upgrade"));
        jsonObject.put("description", createValueObject("Comprehensive system upgrade including database, application, and infrastructure changes"));
        jsonObject.put("risk", createDisplayValueObject("High"));
        jsonObject.put("priority", createDisplayValueObject("Critical"));
        jsonObject.put("impact", createDisplayValueObject("High"));
        jsonObject.put("assignment_group", createDisplayValueObject("Infrastructure Team"));
        jsonObject.put("assigned_to", createDisplayValueObject("senior.admin@example.com"));
        jsonObject.put("chg_model", createDisplayValueObject("Emergency"));
        jsonObject.put("requested_by", createDisplayValueObject("cto@example.com"));
        jsonObject.put("approval", createDisplayValueObject("Approved"));

        // Act
        ChangeRequestImpl changeRequest = ChangeRequestImpl.create(jsonObject);

        // Assert
        assertEquals("chg999", changeRequest.getSysId());
        assertEquals("CHG0999", changeRequest.getRequestId());
        assertEquals("Implement", changeRequest.getState());
        assertEquals("Major system upgrade", changeRequest.getShortDescription());
        assertTrue(changeRequest.getDescription().contains("Comprehensive"));
        assertEquals("High", changeRequest.getRisk());
        assertEquals("Critical", changeRequest.getPriority());
        assertEquals("High", changeRequest.getImpact());
        assertEquals("Infrastructure Team", changeRequest.getAssignmentGroup());
        assertEquals("senior.admin@example.com", changeRequest.getAssignedTo());
        assertEquals("Emergency", changeRequest.getModel());
        assertEquals("cto@example.com", changeRequest.getRequestedBy());
        assertEquals("Approved", changeRequest.getApproval());
    }

    /**
     * Helper method to create a JSON object with a "value" field.
     */
    private JSONObject createValueObject(String value) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("value", value);
        return obj;
    }

    /**
     * Helper method to create a JSON object with a "display_value" field.
     */
    private JSONObject createDisplayValueObject(String displayValue) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("display_value", displayValue);
        return obj;
    }
}

