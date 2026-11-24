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

import app.krista.extensions.it.case_management.servicenow.impl.IncidentImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Incident} entity.
 * <p>
 * Tests Incident entity creation, field mapping, and conversion methods.
 * </p>
 */
class IncidentEntityTest {

    @Test
    void testConstructor_WithEmptyMap_CreatesIncident() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertNotNull(incident);
    }

    @Test
    void testConstructor_WithAllFields_CreatesIncident() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Incident.SYS_ID, "sys123");
        fields.put(Incident.INCIDENT_ID, "INC0001");
        fields.put(Incident.CALLER, "caller123");
        fields.put(Incident.ASSIGNED_TO, "user456");
        fields.put(Incident.STATE, "New");
        fields.put(Incident.SHORT_DESCRIPTION, "Test incident");
        fields.put(Incident.DESCRIPTION, "Detailed description");
        fields.put(Incident.IMPACT, "High");
        fields.put(Incident.URGENCY, "High");

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertEquals("sys123", incident.sysId);
        assertEquals("INC0001", incident.incidentId);
        assertEquals("caller123", incident.caller);
        assertEquals("user456", incident.assignedTo);
        assertEquals("New", incident.incidentState);
        assertEquals("Test incident", incident.shortDescription);
        assertEquals("Detailed description", incident.description);
        assertEquals("High", incident.impact);
        assertEquals("High", incident.urgency);
    }

    @Test
    void testDefaultConstructor_CreatesIncident() {
        // Act
        Incident incident = new Incident();

        // Assert
        assertNotNull(incident);
        assertNull(incident.sysId);
        assertNull(incident.incidentId);
    }

    @Test
    void testConstants_HaveCorrectValues() {
        // Assert
        assertEquals("Sys Id", Incident.SYS_ID);
        assertEquals("Incident Id", Incident.INCIDENT_ID);
        assertEquals("Caller", Incident.CALLER);
        assertEquals("Assigned To", Incident.ASSIGNED_TO);
        assertEquals("Incident State", Incident.STATE);
        assertEquals("Short Description", Incident.SHORT_DESCRIPTION);
        assertEquals("Description", Incident.DESCRIPTION);
        assertEquals("Impact", Incident.IMPACT);
        assertEquals("Urgency", Incident.URGENCY);
    }

    @Test
    void testCreateServiceNowIncident_WithValidIncident_ReturnsServiceIncident() {
        // Arrange
        Incident incident = new Incident();
        incident.sysId = "sys123";
        incident.incidentId = "INC0001";
        incident.caller = "caller123";
        incident.assignedTo = "user456";
        incident.incidentState = "In Progress";
        incident.shortDescription = "Critical issue";
        incident.description = "Full description";
        incident.impact = "High";
        incident.urgency = "High";

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Incident serviceIncident =
            Incident.createServiceNowIncident(incident);

        // Assert
        assertNotNull(serviceIncident);
        assertEquals("sys123", serviceIncident.getSysId());
        assertEquals("INC0001", serviceIncident.getIncidentId());
        assertEquals("caller123", serviceIncident.getCaller());
        assertEquals("user456", serviceIncident.getAssignedTo());
        assertEquals("In Progress", serviceIncident.getState());
        // Fixed: description and shortDescription are now in correct order
        assertEquals("Critical issue", serviceIncident.getShortDescription());
        assertEquals("Full description", serviceIncident.getDescription());
        assertEquals("High", serviceIncident.getImpact());
        assertEquals("High", serviceIncident.getUrgency());
    }

    @Test
    void testCreate_WithServiceIncident_ReturnsEntity() {
        // Arrange
        IncidentImpl serviceIncident = new IncidentImpl("sys456", "INC0002", "caller789",
            "user123", "Resolved", "Test description", "Full details", "Medium", "Low");

        // Act
        Incident incident = Incident.create(serviceIncident);

        // Assert
        assertNotNull(incident);
        assertEquals("sys456", incident.sysId);
        assertEquals("INC0002", incident.incidentId);
        assertEquals("caller789", incident.caller);
        assertEquals("user123", incident.assignedTo);
        assertEquals("Resolved", incident.incidentState);
        assertEquals("Test description", incident.shortDescription);
        assertEquals("Full details", incident.description);
        assertEquals("Medium", incident.impact);
        assertEquals("Low", incident.urgency);
    }

    @Test
    void testConstructor_WithPartialFields_CreatesIncident() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Incident.INCIDENT_ID, "INC0003");
        fields.put(Incident.IMPACT, "Low");

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertEquals("INC0003", incident.incidentId);
        assertEquals("Low", incident.impact);
        assertNull(incident.caller);
        assertNull(incident.urgency);
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Incident.INCIDENT_ID, null);
        fields.put(Incident.STATE, null);

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertNotNull(incident);
        assertNull(incident.incidentId);
        assertNull(incident.incidentState);
    }

    @Test
    void testCreateServiceNowIncident_WithNullFields_HandlesGracefully() {
        // Arrange
        Incident incident = new Incident();

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Incident serviceIncident =
            Incident.createServiceNowIncident(incident);

        // Assert
        assertNotNull(serviceIncident);
        assertNull(serviceIncident.getIncidentId());
        assertNull(serviceIncident.getCaller());
    }

    @Test
    void testCreate_WithNullFields_HandlesGracefully() {
        // Arrange
        IncidentImpl serviceIncident = new IncidentImpl(null, null, null, null, null, null, null, null, null);

        // Act
        Incident incident = Incident.create(serviceIncident);

        // Assert
        assertNotNull(incident);
        assertNull(incident.sysId);
        assertNull(incident.incidentId);
    }

    @Test
    void testConstructor_WithAllStateValues_CreatesIncident() {
        // Arrange
        String[] states = {"New", "In Progress", "On Hold", "Resolved", "Closed", "Cancelled"};

        for (String state : states) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Incident.STATE, state);

            // Act
            Incident incident = new Incident(fields);

            // Assert
            assertEquals(state, incident.incidentState);
        }
    }

    @Test
    void testConstructor_WithAllImpactValues_CreatesIncident() {
        // Arrange
        String[] impacts = {"High", "Medium", "Low"};

        for (String impact : impacts) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Incident.IMPACT, impact);

            // Act
            Incident incident = new Incident(fields);

            // Assert
            assertEquals(impact, incident.impact);
        }
    }

    @Test
    void testConstructor_WithAllUrgencyValues_CreatesIncident() {
        // Arrange
        String[] urgencies = {"High", "Medium", "Low"};

        for (String urgency : urgencies) {
            Map<String, Object> fields = new HashMap<>();
            fields.put(Incident.URGENCY, urgency);

            // Act
            Incident incident = new Incident(fields);

            // Assert
            assertEquals(urgency, incident.urgency);
        }
    }

    @Test
    void testRoundTrip_EntityToServiceAndBack_PreservesData() {
        // Arrange
        Map<String, Object> originalFields = new HashMap<>();
        originalFields.put(Incident.SYS_ID, "sys999");
        originalFields.put(Incident.INCIDENT_ID, "INC0004");
        originalFields.put(Incident.CALLER, "caller999");
        originalFields.put(Incident.ASSIGNED_TO, "user888");
        originalFields.put(Incident.STATE, "On Hold");
        originalFields.put(Incident.SHORT_DESCRIPTION, "Round trip test");
        originalFields.put(Incident.DESCRIPTION, "Full round trip description");
        originalFields.put(Incident.IMPACT, "High");
        originalFields.put(Incident.URGENCY, "Medium");

        Incident originalIncident = new Incident(originalFields);

        // Act
        app.krista.extensions.it.case_management.servicenow.service.Incident serviceIncident =
            Incident.createServiceNowIncident(originalIncident);
        Incident resultIncident = Incident.create(serviceIncident);

        // Assert
        assertEquals(originalIncident.sysId, resultIncident.sysId);
        assertEquals(originalIncident.incidentId, resultIncident.incidentId);
        assertEquals(originalIncident.caller, resultIncident.caller);
        assertEquals(originalIncident.assignedTo, resultIncident.assignedTo);
        assertEquals(originalIncident.incidentState, resultIncident.incidentState);
        // Fixed: description and shortDescription now preserve correctly after round trip
        assertEquals(originalIncident.description, resultIncident.description);
        assertEquals(originalIncident.shortDescription, resultIncident.shortDescription);
        assertEquals(originalIncident.impact, resultIncident.impact);
        assertEquals(originalIncident.urgency, resultIncident.urgency);
    }

    @Test
    void testConstructor_WithLongDescription_CreatesIncident() {
        // Arrange
        String longDescription = "This is a very long description that contains many details about the incident. ".repeat(10);
        Map<String, Object> fields = new HashMap<>();
        fields.put(Incident.DESCRIPTION, longDescription);

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertEquals(longDescription, incident.description);
    }

    @Test
    void testConstructor_WithSpecialCharacters_CreatesIncident() {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put(Incident.SHORT_DESCRIPTION, "Test with special chars: @#$%^&*()");
        fields.put(Incident.DESCRIPTION, "Description with unicode: 日本語 中文");

        // Act
        Incident incident = new Incident(fields);

        // Assert
        assertEquals("Test with special chars: @#$%^&*()", incident.shortDescription);
        assertEquals("Description with unicode: 日本語 中文", incident.description);
    }
}

