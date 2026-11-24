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

package app.krista.extensions.it.case_management.servicenow.catalog.entities;

import app.krista.extensions.it.case_management.servicenow.impl.CaseImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Case} entity.
 * <p>
 * Tests Case entity creation, field mapping, and conversion methods.
 * </p>
 */
class CaseEntityTest {

    @Test
    void testConstructor_WithEmptyMap_CreatesCase() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act
        Case caseEntity = new Case(fields);

        // Assert
        assertNotNull(caseEntity);
    }

    @Test
    void testConstructor_WithAllFields_CreatesCase() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Case.CASE_ID, "CS0001");
        fields.put(Case.CHANNEL, "Web");
        fields.put(Case.PRIORITY, "High");
        fields.put(Case.CUSTOMER, "customer123");
        fields.put(Case.ASSIGNED_TO, "user456");
        fields.put(Case.SHORT_DESCRIPTION, "Test case");
        fields.put(Case.SYS_ID, "sys123");
        fields.put(Case.STATE, "Open");

        // Act
        Case caseEntity = new Case(fields);

        // Assert
        assertEquals("CS0001", caseEntity.caseId);
        assertEquals("Web", caseEntity.channel);
        assertEquals("High", caseEntity.priority);
        assertEquals("customer123", caseEntity.customer);
        assertEquals("user456", caseEntity.assignedToSysId);
        assertEquals("Test case", caseEntity.shortDescription);
        assertEquals("sys123", caseEntity.sysId);
        assertEquals("Open", caseEntity.state);
    }

    @Test
    void testDefaultConstructor_CreatesCase() {
        // Act
        Case caseEntity = new Case();

        // Assert
        assertNotNull(caseEntity);
        assertNull(caseEntity.caseId);
        assertNull(caseEntity.channel);
        assertNull(caseEntity.priority);
    }

    @Test
    void testConstants_HaveCorrectValues() {
        // Assert
        assertEquals("Case Id", Case.CASE_ID);
        assertEquals("Channel", Case.CHANNEL);
        assertEquals("Priority", Case.PRIORITY);
        assertEquals("Customer", Case.CUSTOMER);
        assertEquals("Assigned To Sys Id", Case.ASSIGNED_TO);
        assertEquals("Short Description", Case.SHORT_DESCRIPTION);
        assertEquals("Sys Id", Case.SYS_ID);
        assertEquals("State", Case.STATE);
    }

    @Test
    void testCreateServiceNowCase_WithValidCase_ReturnsServiceCase() {
        // Arrange
        Case caseEntity = new Case();
        caseEntity.caseId = "CS0001";
        caseEntity.channel = "Phone";
        caseEntity.priority = "Critical";
        caseEntity.customer = "customer123";
        caseEntity.assignedToSysId = "user456";
        caseEntity.shortDescription = "Urgent issue";
        caseEntity.sysId = "sys123";
        caseEntity.state = "New";

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Case serviceCase =
            Case.createServiceNowCase(caseEntity);

        // Assert
        assertNotNull(serviceCase);
        assertEquals("CS0001", serviceCase.getCaseId());
        assertEquals("Phone", serviceCase.getChannel());
        assertEquals("Critical", serviceCase.getPriority());
        assertEquals("customer123", serviceCase.getAccountId());
        assertEquals("user456", serviceCase.getAssignedTo());
        assertEquals("Urgent issue", serviceCase.getShortDescription());
        assertEquals("sys123", serviceCase.getSysId());
        assertEquals("New", serviceCase.getState());
    }

    @Test
    void testCreate_WithServiceCase_ReturnsEntity() {
        // Arrange
        CaseImpl serviceCase = new CaseImpl("CS0002", "Email", "Moderate",
            "customer789", "user123", "Test description", "sys456", "Resolved");

        // Act
        Case caseEntity = Case.create(serviceCase);

        // Assert
        assertNotNull(caseEntity);
        assertEquals("CS0002", caseEntity.caseId);
        assertEquals("Email", caseEntity.channel);
        assertEquals("Moderate", caseEntity.priority);
        assertEquals("customer789", caseEntity.customer);
        assertEquals("user123", caseEntity.assignedToSysId);
        assertEquals("Test description", caseEntity.shortDescription);
        assertEquals("sys456", caseEntity.sysId);
        assertEquals("Resolved", caseEntity.state);
    }

    @Test
    void testConstructor_WithPartialFields_CreatesCase() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Case.CASE_ID, "CS0003");
        fields.put(Case.PRIORITY, "Low");

        // Act
        Case caseEntity = new Case(fields);

        // Assert
        assertEquals("CS0003", caseEntity.caseId);
        assertEquals("Low", caseEntity.priority);
        assertNull(caseEntity.channel);
        assertNull(caseEntity.customer);
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Case.CASE_ID, null);
        fields.put(Case.CHANNEL, null);

        // Act
        Case caseEntity = new Case(fields);

        // Assert
        assertNotNull(caseEntity);
        assertNull(caseEntity.caseId);
        assertNull(caseEntity.channel);
    }

    @Test
    void testCreateServiceNowCase_WithNullFields_HandlesGracefully() {
        // Arrange
        Case caseEntity = new Case();

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Case serviceCase =
            Case.createServiceNowCase(caseEntity);

        // Assert
        assertNotNull(serviceCase);
        assertNull(serviceCase.getCaseId());
        assertNull(serviceCase.getChannel());
    }

    @Test
    void testCreate_WithNullFields_HandlesGracefully() {
        // Arrange
        CaseImpl serviceCase = new CaseImpl(null, null, null, null, null, null, null, null);

        // Act
        Case caseEntity = Case.create(serviceCase);

        // Assert
        assertNotNull(caseEntity);
        assertNull(caseEntity.caseId);
        assertNull(caseEntity.channel);
    }

    @Test
    void testConstructor_WithAllChannelValues_CreatesCase() {
        // Arrange
        String[] channels = {"Web", "Phone", "Email", "Chat", "Social", "Community", "Alert", "Virtual Agent"};

        for (String channel : channels) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Case.CHANNEL, channel);

            // Act
            Case caseEntity = new Case(fields);

            // Assert
            assertEquals(channel, caseEntity.channel);
        }
    }

    @Test
    void testConstructor_WithAllPriorityValues_CreatesCase() {
        // Arrange
        String[] priorities = {"Critical", "High", "Moderate", "Low"};

        for (String priority : priorities) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Case.PRIORITY, priority);

            // Act
            Case caseEntity = new Case(fields);

            // Assert
            assertEquals(priority, caseEntity.priority);
        }
    }

    @Test
    void testConstructor_WithAllStateValues_CreatesCase() {
        // Arrange
        String[] states = {"New", "Open", "Awaiting Info", "Resolved", "Closed"};

        for (String state : states) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Case.STATE, state);

            // Act
            Case caseEntity = new Case(fields);

            // Assert
            assertEquals(state, caseEntity.state);
        }
    }

    @Test
    void testRoundTrip_EntityToServiceAndBack_PreservesData() {
        // Arrange
        Map<String, Object> originalFields = new HashMap<>();
        originalFields.put(Case.CASE_ID, "CS0004");
        originalFields.put(Case.CHANNEL, "Chat");
        originalFields.put(Case.PRIORITY, "High");
        originalFields.put(Case.CUSTOMER, "customer999");
        originalFields.put(Case.ASSIGNED_TO, "user888");
        originalFields.put(Case.SHORT_DESCRIPTION, "Round trip test");
        originalFields.put(Case.SYS_ID, "sys999");
        originalFields.put(Case.STATE, "Open");

        Case originalCase = new Case(originalFields);

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Case serviceCase =
            Case.createServiceNowCase(originalCase);
        Case resultCase = Case.create(serviceCase);

        // Assert
        assertEquals(originalCase.caseId, resultCase.caseId);
        assertEquals(originalCase.channel, resultCase.channel);
        assertEquals(originalCase.priority, resultCase.priority);
        assertEquals(originalCase.customer, resultCase.customer);
        assertEquals(originalCase.assignedToSysId, resultCase.assignedToSysId);
        assertEquals(originalCase.shortDescription, resultCase.shortDescription);
        assertEquals(originalCase.sysId, resultCase.sysId);
        assertEquals(originalCase.state, resultCase.state);
    }
}

