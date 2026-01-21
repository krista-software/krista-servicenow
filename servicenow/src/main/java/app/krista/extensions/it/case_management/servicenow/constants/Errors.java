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

public final class Errors {


    private Errors() {
    }

    // Incident Error Messages
    public static final String ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_THE_GIVEN_SYS_ID =
            "Unable to retrieve incident. Please verify the System ID is correct and the incident exists in ServiceNow. If the problem persists, check your connection and permissions.";

    public static final String ERROR_OCCURRED_WHILE_FETCHING_INCIDENT_FOR_GIVEN_INCIDENT_ID =
            "Unable to retrieve incident. Please verify the Incident ID (e.g., INC0010001) is correct and exists in ServiceNow. Check your connection and permissions if the issue continues.";

    public static final String ERROR_OCCURRED_WHILE_CREATING_A_NEW_INCIDENT =
            "Failed to create incident. Please verify all required fields are provided and valid. Ensure the caller username exists in ServiceNow if specified.";

    public static final String ERROR_OCCURRED_WHILE_UPDATING_INCIDENT =
            "Failed to update incident. Please verify the incident exists and you have permission to modify it. Check that all field values are valid.";

    public static final String ERROR_OCCURRED_WHILE_ASSIGNING_USER_TO_INCIDENT =
            "Failed to assign user to incident. Please verify both the user System ID and incident System ID are correct and exist in ServiceNow: ";

    public static final String ERROR_OCCURRED_WHILE_FETCHING_INCIDENTS =
            "Unable to retrieve incidents. Please verify your search criteria and try again. If the problem persists, check your connection to ServiceNow.";

    // User Error Messages
    public static final String ERROR_OCCURRED_WHILE_FINDING_THE_USER =
            "Unable to find user in ServiceNow. Please verify the username is correct and the user exists in your ServiceNow instance.";

    public static final String ERROR_OCCURRED_WHILE_FINDING_THE_SYSTEM_ID_OF_THE_USER =
            "Unable to retrieve user System ID. Please verify the username is correct and exists in ServiceNow.";

    // Customer Error Messages
    public static final String ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNTS =
            "Unable to retrieve customer accounts. Please verify your search criteria and connection to ServiceNow.";

    public static final String ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID =
            "Unable to retrieve customer account. Please verify the System ID is correct and the customer account exists in ServiceNow.";

    // Case Error Messages
    public static final String ERROR_OCCURRED_WHILE_FETCHING_THE_CASE =
            "Unable to retrieve case. Please verify the Case ID or System ID is correct and the case exists in ServiceNow.";

    public static final String ERROR_OCCURRED_WHILE_FETCHING_ALL_CASES =
            "Unable to retrieve cases. Please verify your search criteria and connection to ServiceNow.";

    public static final String ERROR_OCCURRED_WHILE_CREATING_A_NEW_CASE =
            "Failed to create case. Please verify all required fields are provided and valid. Ensure Customer System ID and Assigned To System ID exist if specified.";

    public static final String ERROR_OCCURRED_WHILE_UPDATING_THE_CASE =
            "Failed to update case. Please verify the case exists and you have permission to modify it. Check that all field values are valid.";

    // Change Request Error Messages
    public static final String ERROR_OCCURRED_WHILE_CREATING_NEW_CHANGE_REQUEST =
            "Failed to create change request. Please verify all required fields are provided, especially the Assignment Group. Ensure all field values are valid.";

    public static final String ERROR_OCCURRED_WHILE_MOVING_THE_CREATED_CHANGE_REQUEST_TO_ASSESS_STAGE =
            "Failed to move change request to Assess stage. Please verify the change request exists and has a valid Assignment Group assigned.";

    public static final String ERROR_OCCURRED_WHILE_ESCALATING_CHANGE_REQUEST =
            "Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager. Assign a manager to the group in ServiceNow if needed.";

    public static final String ERROR_OCCURRED_WHILE_FETCHING_CHANGE_REQUEST =
            "Unable to retrieve change request. Please verify the Request ID (e.g., CHG0030001) or System ID is correct and exists in ServiceNow.";

    public static final String ERROR_OCCURRED_WHILE_CREATING_CHANGE_REQUEST =
            "Failed to create change request. Please ensure the Assignment Group is specified and all required fields are valid.";

    // Validation Error Messages
    public static final String GROUP_SYS_ID_CANNOT_BE_EMPTY_OR_NULL =
            "Assignment Group is required. The change request must have an Assignment Group assigned. Please specify a valid Assignment Group and try again.";

    public static final String MANAGER_CANNOT_BE_EMPTY_OR_NULL =
            "Manager not found for Assignment Group. To escalate this change request, please assign a Manager to the Assignment Group in ServiceNow.";

    public static final String AN_ASSIGNMENT_GROUP_IS_REQUIRED_TO_CHANGE_THE_STATE_OF_THE_CHANGE_REQUEST =
            "Assignment Group is required. When creating a change request with state 'Assess', you must specify an Assignment Group. Please add an Assignment Group and try again.";

    // Generic Error Constant
    public static final String ERROR = "error";


}
