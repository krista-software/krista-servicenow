# Troubleshooting Guide

This guide helps you resolve common issues when using the ServiceNow extension.

---

## Table of Contents

1. [Connection and Authentication Issues](#connection-and-authentication-issues)
2. [Incident Errors](#incident-errors)
3. [Change Request Errors](#change-request-errors)
4. [Case Errors](#case-errors)
5. [User and Customer Errors](#user-and-customer-errors)
6. [Search and Query Errors](#search-and-query-errors)
7. [General Tips](#general-tips)

---

## Connection and Authentication Issues

### Invalid ServiceNow Instance URL

**Error Message:**
```
Invalid ServiceNow Instance URL: '<your-url>'. Please provide a valid URL (e.g., https://your-instance.service-now.com).
```

**Cause:** The Instance URL provided is not in a valid format.

**Solution:**
1. Verify your Instance URL follows the format: `https://your-instance.service-now.com`
2. Do not include trailing slashes or additional paths
3. Ensure you're using HTTPS, not HTTP
4. Check for typos in the URL

**Example of correct URL:**
- ✅ `https://dev12345.service-now.com`
- ❌ `http://dev12345.service-now.com/` (HTTP and trailing slash)
- ❌ `dev12345.service-now.com` (missing protocol)

---

### Authorization Failed

**Error Message:**
```
Authorization failed. Please verify your OAuth credentials (Client ID, Client Secret) and ensure the redirect URL is correctly configured in ServiceNow.
```

**Cause:** OAuth credentials are incorrect or the redirect URL is not properly configured.

**Solution:**

**For OAuth Authentication:**
1. Verify your Client ID and Client Secret in ServiceNow:
   - Navigate to **All → System OAuth → Application Registry**
   - Open your application and verify the credentials
2. Check the Redirect URL:
   - It should be: `https://<your-krista-url>/extension/api/<extension-id>/servicenow/callback`
   - Get the extension base URL from the extension details page in Krista
3. Ensure the application is marked as "Active" in ServiceNow
4. Verify the email matches the one used in ServiceNow

**For Basic Authentication:**
1. Verify your username and password are correct
2. Ensure the user account is active in ServiceNow
3. Check that the user has appropriate permissions

---

### Re-authorization Required

**Error Message:**
```
Got error for Authentication. So sending for re authentication
```

**Cause:** Your OAuth token has expired or become invalid.

**Solution:**
1. Click the authorization link when prompted
2. Log in to ServiceNow and grant permissions
3. Wait for the "Authorization Complete" message
4. Retry your request

---

## Incident Errors

### Unable to Retrieve Incident by System ID

**Error Message:**
```
Unable to retrieve incident. Please verify the System ID is correct and the incident exists in ServiceNow. If the problem persists, check your connection and permissions.
```

**Cause:** The System ID provided doesn't exist or you don't have permission to access it.

**Solution:**
1. Verify the System ID (32-character hexadecimal string) is correct
2. Check that the incident exists in ServiceNow
3. Ensure you have read permissions for the incident
4. Try searching for the incident by Incident ID instead

---

### Unable to Retrieve Incident by Incident ID

**Error Message:**
```
Unable to retrieve incident. Please verify the Incident ID (e.g., INC0010001) is correct and exists in ServiceNow. Check your connection and permissions if the issue continues.
```

**Cause:** The Incident ID doesn't exist or is incorrectly formatted.

**Solution:**
1. Verify the Incident ID format (e.g., INC0010001)
2. Check that the incident exists in your ServiceNow instance
3. Ensure you have read permissions
4. Verify your connection to ServiceNow is active

---

### Failed to Create Incident

**Error Message:**
```
Failed to create incident. Please verify all required fields are provided and valid. Ensure the caller username exists in ServiceNow if specified.
```

**Cause:** Required fields are missing or invalid, or the caller username doesn't exist.

**Solution:**
1. Check that all required fields are provided
2. If specifying a Caller, verify the username exists in ServiceNow (e.g., "beth.anglin", "admin")
3. Ensure State is one of: New, In Progress, or On Hold (for new incidents)
4. Verify Impact and Urgency values are: Low, Medium, or High
5. Check that field values match expected formats

---

### Failed to Update Incident

**Error Message:**
```
Failed to update incident. Please verify the incident exists and you have permission to modify it. Check that all field values are valid.
```

**Cause:** The incident doesn't exist, you lack permissions, or field values are invalid.

**Solution:**
1. Verify the incident System ID is correct
2. Ensure you have update permissions for the incident
3. Check that all field values are valid
4. Verify State transitions are allowed (e.g., can't go from Closed to New)
5. Ensure assigned users exist in ServiceNow

---

### Failed to Assign User to Incident

**Error Message:**
```
Failed to assign user to incident. Please verify both the user System ID and incident System ID are correct and exist in ServiceNow: <details>
```

**Cause:** Either the user or incident System ID is incorrect or doesn't exist.

**Solution:**
1. Verify the user System ID is correct (32-character hex string)
2. Verify the incident System ID is correct
3. Ensure both the user and incident exist in ServiceNow
4. Check that you have permission to assign incidents
5. Use the User entity search to find the correct user System ID

---

## Change Request Errors

### Failed to Create Change Request - Missing Assignment Group

**Error Message:**
```
Assignment Group is required. When creating a change request with state 'Assess', you must specify an Assignment Group. Please add an Assignment Group and try again.
```

**Cause:** Creating a change request with state "Assess" requires an Assignment Group.

**Solution:**
1. Specify an Assignment Group when creating the change request
2. Use a valid Assignment Group name from your ServiceNow instance (e.g., "IT Securities", "Network")
3. If you don't want to specify state, leave it blank (defaults to "New")
4. Verify the Assignment Group exists in ServiceNow

---

### Failed to Escalate Change Request - No Manager

**Error Message:**
```
Manager not found for Assignment Group. To escalate this change request, please assign a Manager to the Assignment Group in ServiceNow.
```

**Cause:** The Assignment Group doesn't have a designated Manager.

**Solution:**
1. In ServiceNow, navigate to **All → User Administration → Groups**
2. Find the Assignment Group
3. Assign a Manager to the group
4. Save the changes
5. Retry the escalation

---

### Failed to Move Change Request to Assess Stage

**Error Message:**
```
Failed to move change request to Assess stage. Please verify the change request exists and has a valid Assignment Group assigned.
```

**Cause:** The change request doesn't have an Assignment Group or doesn't exist.

**Solution:**
1. Verify the change request exists
2. Ensure an Assignment Group is assigned to the change request
3. Update the change request to add an Assignment Group if missing
4. Retry the operation

---

### Unable to Retrieve Change Request

**Error Message:**
```
Unable to retrieve change request. Please verify the Request ID (e.g., CHG0030001) or System ID is correct and exists in ServiceNow.
```

**Cause:** The Request ID or System ID doesn't exist or is incorrectly formatted.

**Solution:**
1. Verify the Request ID format (e.g., CHG0030001)
2. Check that the change request exists in ServiceNow
3. Ensure you have read permissions
4. Try using the System ID instead of Request ID

---

## Case Errors

### Unable to Retrieve Case

**Error Message:**
```
Unable to retrieve case. Please verify the Case ID or System ID is correct and the case exists in ServiceNow.
```

**Cause:** The Case ID or System ID doesn't exist.

**Solution:**
1. Verify the Case ID or System ID is correct
2. Check that the case exists in ServiceNow
3. Ensure you have read permissions for cases
4. Verify the Customer Service Portal plugin is installed

---

### Failed to Create Case

**Error Message:**
```
Failed to create case. Please verify all required fields are provided and valid. Ensure Customer System ID and Assigned To System ID exist if specified.
```

**Cause:** Required fields are missing or System IDs don't exist.

**Solution:**
1. If specifying Customer Sys ID, verify it exists using the Customer entity search
2. If specifying Assigned To Sys ID, verify the user exists using the User entity search
3. Ensure State is one of: New, In Progress, or On Hold (for new cases)
4. Verify Priority is one of: Critical, High, Moderate, or Low
5. Check Channel is valid: Web, Phone, Email, Chat, Social, Community, Alert, or Virtual Agent

---

### Failed to Update Case

**Error Message:**
```
Failed to update case. Please verify the case exists and you have permission to modify it. Check that all field values are valid.
```

**Cause:** The case doesn't exist, you lack permissions, or field values are invalid.

**Solution:**
1. Verify the case System ID is correct
2. Ensure you have update permissions
3. Check that all field values are valid
4. Verify State transitions are allowed
5. Ensure referenced System IDs (Customer, Assigned To) exist

---

## User and Customer Errors

### Unable to Find User

**Error Message:**
```
Unable to find user in ServiceNow. Please verify the username is correct and the user exists in your ServiceNow instance.
```

**Cause:** The username doesn't exist in ServiceNow.

**Solution:**
1. Verify the username is correct (e.g., "beth.anglin", "admin")
2. Check that the user exists in ServiceNow: **All → User Administration → Users**
3. Ensure the user account is active
4. Use the exact username, not the display name

---

### Unable to Retrieve User System ID

**Error Message:**
```
Unable to retrieve user System ID. Please verify the username is correct and exists in ServiceNow.
```

**Cause:** The username doesn't exist or the query failed.

**Solution:**
1. Verify the username is correct
2. Use the User entity search to find users
3. Check your connection to ServiceNow
4. Ensure you have permission to query users

---

### Unable to Retrieve Customer Account

**Error Message:**
```
Unable to retrieve customer account. Please verify the System ID is correct and the customer account exists in ServiceNow.
```

**Cause:** The Customer System ID doesn't exist.

**Solution:**
1. Verify the Customer System ID is correct
2. Use the Customer entity search to find customers
3. Ensure the Customer Service Portal plugin is installed
4. Check that you have read permissions for customer accounts

---

## Search and Query Errors

### Search Criteria Cannot Be Empty

**Error Message:**
```
Search criteria cannot be empty. Please provide at least one search condition and try again.
```

**Cause:** No search conditions were provided.

**Solution:**
1. Add at least one search condition
2. Select a searchable field (e.g., Incident ID, State, Priority)
3. Provide a value for the search condition
4. Ensure the search query is properly formatted

---

### Unable to Process ServiceNow API Response

**Error Message:**
```
Unable to process ServiceNow API response. The response format may be invalid or unexpected. Please verify your ServiceNow instance is accessible and try again.
```

**Cause:** The API response from ServiceNow is malformed or unexpected.

**Solution:**
1. Check that your ServiceNow instance is accessible
2. Verify your ServiceNow instance is not undergoing maintenance
3. Ensure required plugins are installed (Customer Service Portal)
4. Check ServiceNow system logs for errors
5. Retry the operation after a few moments

---

## General Tips

### Best Practices

1. **Use System IDs for References:**
   - When assigning users or linking entities, always use System IDs (32-character hex strings)
   - Use entity searches to find System IDs

2. **Verify Permissions:**
   - Ensure your ServiceNow user has appropriate roles
   - Required roles: itil, sn_change_read, sn_change_write, sn_customerservice_agent

3. **Check Plugin Installation:**
   - The Customer Service Portal plugin must be installed for Case and Customer entities
   - Navigate to **All → Plugins** in ServiceNow to verify

4. **Use Correct Field Values:**
   - Priority: Critical, High, Moderate, Low
   - Impact/Urgency: High, Medium, Low
   - State (new incidents/cases): New, In Progress, On Hold

5. **Assignment Groups:**
   - Always specify Assignment Group when creating change requests
   - Ensure Assignment Groups have designated Managers for escalation

### Getting More Help

If you continue to experience issues:

1. **Check ServiceNow Logs:**
   - Navigate to **All → System Logs → System Log → All** in ServiceNow
   - Look for errors related to your requests

2. **Verify Extension Configuration:**
   - Review your Instance URL, credentials, and authentication type
   - Test the connection using the "Test Connection" button

3. **Review ServiceNow Documentation:**
   - [ServiceNow REST API Documentation](https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/c_RESTAPI.html)
   - [ServiceNow Change Management](https://docs.servicenow.com/bundle/tokyo-it-service-management/page/product/change-management/concept/c_ITILChangeManagement.html)

4. **Contact Support:**
   - Provide the complete error message
   - Include the operation you were attempting
   - Share relevant System IDs or Request IDs

---

**Last Updated:** December 2025  
**Extension Version:** 2.0.6

