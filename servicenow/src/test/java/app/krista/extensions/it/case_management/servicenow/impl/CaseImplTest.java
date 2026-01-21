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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CaseImpl}.
 * <p>
 * Tests the creation and manipulation of Case objects from JSON responses.
 * </p>
 */
class CaseImplTest {

    @Test
    void testConstructor_WithAllParameters_CreatesCase() {
        // Arrange
        String caseId = "CS0001";
        String channel = "Email";
        String priority = "Critical";
        String accountId = "ACC123";
        String assignedTo = "user123";
        String shortDescription = "System outage";
        String sysId = "case123";
        String state = "New";

        // Act
        CaseImpl caseObj = new CaseImpl(caseId, channel, priority, accountId, 
            assignedTo, shortDescription, sysId, state);

        // Assert
        assertNotNull(caseObj);
        assertEquals(caseId, caseObj.getCaseId());
        assertEquals(channel, caseObj.getChannel());
        assertEquals(priority, caseObj.getPriority());
        assertEquals(accountId, caseObj.getAccountId());
        assertEquals(assignedTo, caseObj.getAssignedTo());
        assertEquals(shortDescription, caseObj.getShortDescription());
        assertEquals(sysId, caseObj.getSysId());
        assertEquals(state, caseObj.getState());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        CaseImpl caseObj = new CaseImpl(null, null, null, null, null, null, null, null);

        // Assert
        assertNotNull(caseObj);
        assertNull(caseObj.getCaseId());
        assertNull(caseObj.getChannel());
        assertNull(caseObj.getPriority());
    }

    @Test
    void testCreate_WithValidJsonObject_ReturnsCase() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "case123");
        jsonObject.put("number", "CS0001");
        jsonObject.put("state", "1");
        jsonObject.put("short_description", "System outage");
        jsonObject.put("priority", "1");
        jsonObject.put("contact_type", "email");
        jsonObject.put("consumer", "consumer123");
        jsonObject.put("assigned_to", "user123");
        
        JSONObject accountObj = new JSONObject();
        accountObj.put("name", "ACME Corp");
        jsonObject.put("account", accountObj);

        // Act
        CaseImpl caseObj = CaseImpl.create(jsonObject);

        // Assert
        assertNotNull(caseObj);
        assertEquals("CS0001", caseObj.getCaseId());
        assertEquals("New", caseObj.getState());
        assertEquals("System outage", caseObj.getShortDescription());
        assertEquals("Critical", caseObj.getPriority());
        assertEquals("Email", caseObj.getChannel());
        assertEquals("ACME Corp", caseObj.getAccountId());
        assertEquals("user123", caseObj.getAssignedTo());
    }

    @Test
    void testCreate_WithNullAccount_HandlesGracefully() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "case123");
        jsonObject.put("number", "CS0001");
        jsonObject.put("state", "1");
        jsonObject.put("short_description", "Issue");
        jsonObject.put("priority", "2");
        jsonObject.put("contact_type", "phone");
        jsonObject.put("consumer", "consumer123");
        jsonObject.put("assigned_to", "user123");
        jsonObject.put("account", JSONObject.NULL);

        // Act
        CaseImpl caseObj = CaseImpl.create(jsonObject);

        // Assert
        assertNotNull(caseObj);
        assertEquals("", caseObj.getAccountId());
    }

    @Test
    void testStateMapping_AllStates_MapCorrectly() {
        // Arrange & Act
        CaseImpl newCase = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "Description", "sys123", "New");
        CaseImpl closedCase = new CaseImpl("CS0002", "Phone", "High", "ACC124", 
            "user124", "Description", "sys124", "Closed");
        CaseImpl resolvedCase = new CaseImpl("CS0003", "Web", "Moderate", "ACC125", 
            "user125", "Description", "sys125", "Resolved");
        CaseImpl openCase = new CaseImpl("CS0004", "Chat", "Low", "ACC126", 
            "user126", "Description", "sys126", "Open");
        CaseImpl awaitingCase = new CaseImpl("CS0005", "Social", "Critical", "ACC127", 
            "user127", "Description", "sys127", "Awaiting Info");

        // Assert
        assertEquals("New", newCase.getState());
        assertEquals("Closed", closedCase.getState());
        assertEquals("Resolved", resolvedCase.getState());
        assertEquals("Open", openCase.getState());
        assertEquals("Awaiting Info", awaitingCase.getState());
    }

    @Test
    void testChannelMapping_AllChannels_MapCorrectly() {
        // Arrange & Act
        CaseImpl emailCase = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "Description", "sys123", "New");
        CaseImpl phoneCase = new CaseImpl("CS0002", "Phone", "High", "ACC124", 
            "user124", "Description", "sys124", "Open");
        CaseImpl webCase = new CaseImpl("CS0003", "Web", "Moderate", "ACC125", 
            "user125", "Description", "sys125", "New");
        CaseImpl chatCase = new CaseImpl("CS0004", "Chat", "Low", "ACC126", 
            "user126", "Description", "sys126", "New");
        CaseImpl socialCase = new CaseImpl("CS0005", "Social", "Critical", "ACC127", 
            "user127", "Description", "sys127", "New");
        CaseImpl communityCase = new CaseImpl("CS0006", "Community", "High", "ACC128", 
            "user128", "Description", "sys128", "New");
        CaseImpl alertCase = new CaseImpl("CS0007", "Alert", "Moderate", "ACC129", 
            "user129", "Description", "sys129", "New");
        CaseImpl virtualAgentCase = new CaseImpl("CS0008", "Virtual Agent", "Low", "ACC130", 
            "user130", "Description", "sys130", "New");

        // Assert
        assertEquals("Email", emailCase.getChannel());
        assertEquals("Phone", phoneCase.getChannel());
        assertEquals("Web", webCase.getChannel());
        assertEquals("Chat", chatCase.getChannel());
        assertEquals("Social", socialCase.getChannel());
        assertEquals("Community", communityCase.getChannel());
        assertEquals("Alert", alertCase.getChannel());
        assertEquals("Virtual Agent", virtualAgentCase.getChannel());
    }

    @Test
    void testGetJSON_WithValidCase_ReturnsJsonObject() throws JSONException {
        // Arrange
        CaseImpl caseObj = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "System outage", "sys123", "New");

        // Act
        JSONObject jsonObject = CaseImpl.getJSON(caseObj);

        // Assert
        assertNotNull(jsonObject);
        assertEquals("System outage", jsonObject.getString("short_description"));
        assertEquals("1", jsonObject.getString("state"));
        assertEquals("1", jsonObject.getString("priority"));
        assertEquals("email", jsonObject.getString("contact_type"));
        assertEquals("user123", jsonObject.getString("assigned_to"));
        assertEquals("ACC123", jsonObject.getString("account"));
    }

    @Test
    void testGetJSON_WithDifferentPriorities_MapsCorrectly() throws JSONException {
        // Arrange
        CaseImpl criticalCase = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "Description", "sys123", "New");
        CaseImpl highCase = new CaseImpl("CS0002", "Phone", "High", "ACC124", 
            "user124", "Description", "sys124", "Open");
        CaseImpl moderateCase = new CaseImpl("CS0003", "Web", "Moderate", "ACC125", 
            "user125", "Description", "sys125", "New");
        CaseImpl lowCase = new CaseImpl("CS0004", "Chat", "Low", "ACC126", 
            "user126", "Description", "sys126", "New");

        // Act
        JSONObject criticalJson = CaseImpl.getJSON(criticalCase);
        JSONObject highJson = CaseImpl.getJSON(highCase);
        JSONObject moderateJson = CaseImpl.getJSON(moderateCase);
        JSONObject lowJson = CaseImpl.getJSON(lowCase);

        // Assert
        assertEquals("1", criticalJson.getString("priority"));
        assertEquals("2", highJson.getString("priority"));
        assertEquals("3", moderateJson.getString("priority"));
        assertEquals("4", lowJson.getString("priority"));
    }

    @Test
    void testGetJSON_WithDifferentStates_MapsCorrectly() throws JSONException {
        // Arrange
        CaseImpl newCase = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "Description", "sys123", "New");
        CaseImpl closedCase = new CaseImpl("CS0002", "Phone", "High", "ACC124", 
            "user124", "Description", "sys124", "Closed");
        CaseImpl resolvedCase = new CaseImpl("CS0003", "Web", "Moderate", "ACC125", 
            "user125", "Description", "sys125", "Resolved");

        // Act
        JSONObject newJson = CaseImpl.getJSON(newCase);
        JSONObject closedJson = CaseImpl.getJSON(closedCase);
        JSONObject resolvedJson = CaseImpl.getJSON(resolvedCase);

        // Assert
        assertEquals("1", newJson.getString("state"));
        assertEquals("3", closedJson.getString("state"));
        assertEquals("6", resolvedJson.getString("state"));
    }

    @Test
    void testGetJSON_WithDifferentChannels_MapsCorrectly() throws JSONException {
        // Arrange
        CaseImpl emailCase = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "Description", "sys123", "New");
        CaseImpl phoneCase = new CaseImpl("CS0002", "Phone", "High", "ACC124", 
            "user124", "Description", "sys124", "Open");
        CaseImpl webCase = new CaseImpl("CS0003", "Web", "Moderate", "ACC125", 
            "user125", "Description", "sys125", "New");

        // Act
        JSONObject emailJson = CaseImpl.getJSON(emailCase);
        JSONObject phoneJson = CaseImpl.getJSON(phoneCase);
        JSONObject webJson = CaseImpl.getJSON(webCase);

        // Assert
        assertEquals("email", emailJson.getString("contact_type"));
        assertEquals("phone", phoneJson.getString("contact_type"));
        assertEquals("web", webJson.getString("contact_type"));
    }

    @Test
    void testCreate_WithAllChannelTypes_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "case123");
        jsonObject.put("number", "CS0001");
        jsonObject.put("state", "1");
        jsonObject.put("short_description", "Issue");
        jsonObject.put("priority", "1");
        jsonObject.put("contact_type", "virtual_agent");
        jsonObject.put("consumer", "consumer123");
        jsonObject.put("assigned_to", "user123");
        jsonObject.put("account", JSONObject.NULL);

        // Act
        CaseImpl caseObj = CaseImpl.create(jsonObject);

        // Assert
        assertEquals("Virtual Agent", caseObj.getChannel());
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        CaseImpl caseObj = new CaseImpl("CS0001", "Email", "Critical", "ACC123", 
            "user123", "System outage", "sys123", "New");

        // Act & Assert
        assertEquals("CS0001", caseObj.getCaseId());
        assertEquals("Email", caseObj.getChannel());
        assertEquals("Critical", caseObj.getPriority());
        assertEquals("ACC123", caseObj.getAccountId());
        assertEquals("user123", caseObj.getAssignedTo());
        assertEquals("System outage", caseObj.getShortDescription());
        assertEquals("sys123", caseObj.getSysId());
        assertEquals("New", caseObj.getState());
    }

    @Test
    void testStateKeyValues_ContainsAllExpectedStates() {
        // Assert
        assertEquals("New", CaseImpl.stateKeyValues.get("1"));
        assertEquals("Closed", CaseImpl.stateKeyValues.get("3"));
        assertEquals("Resolved", CaseImpl.stateKeyValues.get("6"));
        assertEquals("Open", CaseImpl.stateKeyValues.get("10"));
        assertEquals("Awaiting Info", CaseImpl.stateKeyValues.get("18"));
    }

    @Test
    void testChannelKeyValues_ContainsAllExpectedChannels() {
        // Assert
        assertEquals("Virtual Agent", CaseImpl.channelKeyValues.get("virtual_agent"));
        assertEquals("Phone", CaseImpl.channelKeyValues.get("phone"));
        assertEquals("Community", CaseImpl.channelKeyValues.get("community"));
        assertEquals("Web", CaseImpl.channelKeyValues.get("web"));
        assertEquals("Social", CaseImpl.channelKeyValues.get("social"));
        assertEquals("Email", CaseImpl.channelKeyValues.get("email"));
        assertEquals("Chat", CaseImpl.channelKeyValues.get("chat"));
        assertEquals("Alert", CaseImpl.channelKeyValues.get("alert"));
    }
}

