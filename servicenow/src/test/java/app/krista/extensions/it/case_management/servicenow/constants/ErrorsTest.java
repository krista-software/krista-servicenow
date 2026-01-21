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

package app.krista.extensions.it.case_management.servicenow.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Errors}.
 * <p>
 * Tests all error message constants for correctness and completeness.
 * </p>
 */
class ErrorsTest {

    @Test
    void testIncidentErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_ASSIGNING_USER_TO_INCIDENT);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENTS);
    }

    @Test
    void testUserErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_SYSTEM_ID_OF_THE_USER);
    }

    @Test
    void testCustomerErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNTS);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID);
    }

    @Test
    void testCaseErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_THE_CASE);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_UPDATING_THE_CASE);
    }

    @Test
    void testChangeRequestErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST);
        assertNotNull(Errors.ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST);
    }

    @Test
    void testValidationErrorMessages_AreNotNull() {
        // Assert
        assertNotNull(Errors.GROUP_SYS_ID_CANNOT_BE_EMPTY_OR_NULL);
        assertNotNull(Errors.MANAGER_CANNOT_BE_EMPTY_OR_NULL);
        assertNotNull(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST);
    }

    @Test
    void testGenericErrorConstant_IsNotNull() {
        // Assert
        assertNotNull(Errors.ERROR);
        assertEquals("error", Errors.ERROR);
    }

    @Test
    void testAllErrorMessages_AreNotEmpty() {
        // Assert - Incident errors
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_ASSIGNING_USER_TO_INCIDENT.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENTS.isEmpty());

        // User errors
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_SYSTEM_ID_OF_THE_USER.isEmpty());

        // Customer errors
        assertFalse(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNTS.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID.isEmpty());

        // Case errors
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_THE_CASE.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_UPDATING_THE_CASE.isEmpty());

        // Change Request errors
        assertFalse(Errors.ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST.isEmpty());
        assertFalse(Errors.ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST.isEmpty());

        // Validation errors
        assertFalse(Errors.GROUP_SYS_ID_CANNOT_BE_EMPTY_OR_NULL.isEmpty());
        assertFalse(Errors.MANAGER_CANNOT_BE_EMPTY_OR_NULL.isEmpty());
        assertFalse(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST.isEmpty());
    }

    @Test
    void testIncidentErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID.contains("System ID"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID.contains("Incident ID"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT.contains("create"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT.contains("update"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_ASSIGNING_USER_TO_INCIDENT.contains("assign"));
    }

    @Test
    void testUserErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER.contains("user"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_SYSTEM_ID_OF_THE_USER.contains("System ID"));
    }

    @Test
    void testCustomerErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNTS.contains("customer"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID.contains("System ID"));
    }

    @Test
    void testCaseErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_THE_CASE.contains("case"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES.contains("cases"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE.contains("create"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_UPDATING_THE_CASE.contains("update"));
    }

    @Test
    void testChangeRequestErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST.contains("change request"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE.contains("Assess"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST.contains("escalate"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST.contains("change request"));
    }

    @Test
    void testValidationErrorMessages_ContainHelpfulInformation() {
        // Assert
        assertTrue(Errors.GROUP_SYS_ID_CANNOT_BE_EMPTY_OR_NULL.contains("Assignment Group"));
        assertTrue(Errors.MANAGER_CANNOT_BE_EMPTY_OR_NULL.contains("Manager"));
        assertTrue(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST.contains("Assignment Group"));
    }

    @Test
    void testErrorMessages_ProvideActionableGuidance() {
        // Assert - Check that error messages provide guidance
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID.contains("verify"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT.contains("verify"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST.contains("Manager"));
    }

    @Test
    void testErrorMessages_AreProfessional() {
        // Assert - Check that error messages are professional and user-friendly
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID.contains("Please"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE.contains("Please"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT.contains("Please"));
    }

    @Test
    void testAllErrorMessages_AreDistinct() {
        // Assert - Verify that all error messages are unique
        assertNotEquals(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID,
            Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID);
        assertNotEquals(Errors.ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT,
            Errors.ERROR_OCCURRED_WHILE_UPDATING_INCIDENT);
        assertNotEquals(Errors.ERROR_OCCURRED_WHILE_FETCHING_THE_CASE,
            Errors.ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES);
    }

    @Test
    void testGenericErrorConstant_HasCorrectValue() {
        // Assert
        assertEquals("error", Errors.ERROR);
    }

    @Test
    void testErrorMessages_MentionServiceNow() {
        // Assert - Check that error messages reference ServiceNow
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID.contains("ServiceNow"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FINDING_THE_USER.contains("ServiceNow"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID.contains("ServiceNow"));
    }

    @Test
    void testChangeRequestErrorMessages_MentionAssignmentGroup() {
        // Assert
        assertTrue(Errors.ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST.contains("Assignment Group"));
        assertTrue(Errors.ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE.contains("Assignment Group"));
        assertTrue(Errors.AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST.contains("Assignment Group"));
    }

    @Test
    void testIncidentErrorMessages_ProvideExamples() {
        // Assert - Check that error messages provide examples where appropriate
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID.contains("INC"));
    }

    @Test
    void testChangeRequestErrorMessages_ProvideExamples() {
        // Assert - Check that error messages provide examples where appropriate
        assertTrue(Errors.ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST.contains("CHG"));
    }
}

