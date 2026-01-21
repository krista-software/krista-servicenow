# Escalate Issue

## Overview

The **Escalate Issue** catalog request assigns a Change Request to the manager of its current assignment group. This is useful when a change request requires management approval or oversight, or when the assigned group cannot resolve the issue and needs escalation to their manager.

**Use Case Example**: A change request assigned to the "Network Operations" group encounters complications. The team lead uses "Escalate Issue" to automatically assign it to the Network Operations manager for review and guidance.

## Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Escalate Issue |
| **Area** | Change Management |
| **Type** | CHANGE_SYSTEM |
| **Retry Support** | Yes - Safe to retry if request fails |
| **Idempotent** | No - Each execution reassigns to manager |

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| **Request ID** | Text | Yes | The Change Request number (not System ID) | `CHG0030001` |

### Parameter Details

#### Request ID
- **Format**: ServiceNow Change Request number (e.g., `CHG0030001`)
- **Not**: System ID (sys_id) - use the human-readable request number
- **Case Sensitive**: No
- **Validation**: Must exist in ServiceNow
- **Where to Find**: Visible in ServiceNow Change Request list or detail page

## Output Parameters

Returns a **Change Request** entity with the following fields:

| Field Name | Type | Description | Example |
|------------|------|-------------|---------|
| **Request ID** | Text | Change Request number | `CHG0030001` |
| **Priority** | Pick One | Priority level | `Critical`, `High`, `Moderate`, `Low` |
| **Risk** | Pick One | Risk assessment | `High`, `Medium`, `Low` |
| **Short Description** | Text | Brief description | `Update firewall rules` |
| **Model** | Text | Change model | `Normal` |
| **Impact** | Pick One | Impact level | `1 - High`, `2 - Medium`, `3 - Low` |
| **State** | Text | Current state | `Assess`, `Authorize`, `Scheduled`, etc. |
| **Sys ID** | Text | System ID | `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6` |
| **Description** | Text | Detailed description | Full change description |
| **Assigned To** | Text | User assigned (now manager) | `John Manager` |
| **Assignment Group** | Text | Group assigned | `Network Operations` |
| **Requested By** | Text | Original requester | `Jane Doe` |
| **Approval** | Text | Approval status | `Requested`, `Approved`, `Rejected` |

## Validation Rules

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Request ID Required** | Request ID parameter must be provided | "Request ID is required" |
| **Request ID Must Exist** | Change Request with given Request ID must exist | "Unable to retrieve change request. Please verify the Request ID is correct and the change request exists in ServiceNow." |
| **Assignment Group Required** | Change Request must have an assignment group | "Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager." |
| **Manager Must Exist** | Assignment group must have a designated manager | "Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager. Assign a manager to the group in ServiceNow if needed." |

## Error Handling

### INPUT_ERROR

**Scenario 1: Request ID Not Provided**
```
Error: Request ID is required
Cause: Request ID parameter is null or empty
Resolution: Provide a valid Change Request number (e.g., CHG0030001)
```

**Scenario 2: Change Request Not Found**
```
Error: Unable to retrieve change request. Please verify the Request ID is correct and the change request exists in ServiceNow.
Cause: No Change Request found with the provided Request ID
Resolution: 
- Verify the Request ID is correct (e.g., CHG0030001, not sys_id)
- Check the Change Request exists in ServiceNow
- Ensure you have permission to view the Change Request
```

### LOGIC_ERROR

**Scenario 1: No Assignment Group**
```
Error: Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager.
Cause: Change Request does not have an assignment group assigned
Resolution:
- Open the Change Request in ServiceNow
- Assign an Assignment Group
- Retry the escalation
```

**Scenario 2: Assignment Group Has No Manager**
```
Error: Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager. Assign a manager to the group in ServiceNow if needed.
Cause: The assignment group does not have a designated manager
Resolution:
- In ServiceNow, navigate to User Administration → Groups
- Find the assignment group
- Set the Manager field to a valid user
- Retry the escalation
```

### SYSTEM_ERROR

**Scenario: ServiceNow API Error**
```
Error: Unable to process ServiceNow API response. The response format may be invalid or unexpected.
Cause: ServiceNow API returned unexpected response or error
Resolution:
- Check ServiceNow instance is accessible
- Verify your authentication is valid
- Check ServiceNow system logs for errors
- Retry the request
```

### Authorization Errors

**Scenario: Authentication Required**
```
Error: Must authorize
Cause: OAuth token expired or not present
Resolution: Click the authorization link to re-authorize with ServiceNow
```

**Scenario: Insufficient Permissions**
```
Error: Insufficient permissions
Cause: User lacks required ServiceNow roles
Resolution: Ensure user has 'itil', 'sn_change_read', and 'sn_change_write' roles
```

## Usage Examples

### Example 1: Escalate Network Change Request

**Scenario**: A network change request needs manager approval before proceeding.

**Input**:
```
Request ID: CHG0030001
```

**Process**:
1. Request retrieves Change Request CHG0030001
2. Identifies assignment group: "Network Operations"
3. Finds manager of "Network Operations": "John Manager"
4. Updates "Assigned To" field to "John Manager"
5. Returns updated Change Request

**Output**:
```
Change Request:
  Request ID: CHG0030001
  Short Description: Update firewall rules for new application
  Assignment Group: Network Operations
  Assigned To: John Manager (escalated from Jane Technician)
  State: Assess
  Priority: High
```

### Example 2: Escalate Critical Infrastructure Change

**Scenario**: A critical infrastructure change encounters issues and needs immediate management attention.

**Input**:
```
Request ID: CHG0030045
```

**Output**:
```
Change Request:
  Request ID: CHG0030045
  Short Description: Emergency database server upgrade
  Assignment Group: Database Administration
  Assigned To: Sarah Manager (escalated)
  State: Assess
  Priority: Critical
  Risk: High
```

### Example 3: Error - No Manager Assigned

**Scenario**: Attempting to escalate a change request whose assignment group has no manager.

**Input**:
```
Request ID: CHG0030099
```

**Error**:
```
Failed to escalate change request. Please verify the change request has an Assignment Group with a designated Manager. Assign a manager to the group in ServiceNow if needed.
```

**Resolution**:
1. Log in to ServiceNow
2. Navigate to **User Administration → Groups**
3. Find the assignment group (e.g., "Application Support")
4. Click on the group
5. Set the **Manager** field to a valid user
6. Click **Update**
7. Retry the escalation

## Business Rules

### Escalation Logic

1. **Retrieve Change Request**: Fetches the change request by Request ID
2. **Validate Assignment Group**: Ensures change request has an assignment group
3. **Find Group Manager**: Queries ServiceNow for the assignment group's manager
4. **Validate Manager**: Ensures manager exists and is active
5. **Update Assignment**: Sets "Assigned To" field to the manager
6. **Return Updated Record**: Returns the updated change request

### Assignment Group Manager

- The manager is determined by the **Manager** field on the ServiceNow Group record
- If no manager is set, the escalation fails
- The manager must be an active user in ServiceNow

### State Transitions

- Escalation does **not** change the change request state
- The change request remains in its current state (e.g., Assess, Authorize)
- Only the "Assigned To" field is updated

### Notifications

- ServiceNow may send notifications based on your configured notification rules
- Typical notifications:
  - Manager receives "Change Request Assigned" notification
  - Previous assignee may receive "Change Request Reassigned" notification

## Limitations

1. **Single Manager Only**: Escalates to one manager, not multiple
2. **No Multi-Level Escalation**: Does not escalate beyond immediate manager
3. **Requires Manager Field**: Assignment group must have manager configured
4. **No State Change**: Does not automatically move change request to different state
5. **No Approval Creation**: Does not create approval records, only reassigns

## Best Practices

### When to Use Escalate Issue

✅ **Use When**:
- Change request needs management review
- Assigned team cannot resolve and needs guidance
- Change requires higher-level approval
- Incident severity increases and needs management attention
- Standard escalation procedures require manager involvement

❌ **Don't Use When**:
- You want to reassign to a specific user (use Update Change Request entity instead)
- You want to change assignment group (use Update Change Request entity)
- You need multi-level escalation (use multiple escalations or custom workflow)
- Change request has no assignment group (assign group first)

### Preparation Checklist

Before using Escalate Issue:

- [ ] Verify change request exists and you have access
- [ ] Confirm change request has an assignment group
- [ ] Verify assignment group has a designated manager
- [ ] Ensure manager user is active in ServiceNow
- [ ] Check you have required permissions (itil, sn_change_write)

### Error Prevention

1. **Always Validate Request ID**: Verify the change request exists before escalation
2. **Check Assignment Group**: Ensure assignment group is set
3. **Verify Manager Exists**: Confirm group has a manager before escalating
4. **Handle Errors Gracefully**: Implement error handling in your workflow
5. **Log Escalations**: Track escalations for audit purposes

## Common Use Cases

### Use Case 1: Automated Escalation After Timeout

**Scenario**: Automatically escalate change requests that haven't been updated in 24 hours.

**Workflow**:
1. Search for change requests in "Assess" state
2. Filter by last updated > 24 hours ago
3. For each change request:
   - Call Escalate Issue with Request ID
   - Log escalation action
   - Send notification to manager

### Use Case 2: High-Priority Change Escalation

**Scenario**: Automatically escalate all "Critical" priority change requests to managers.

**Workflow**:
1. Trigger on new change request creation
2. Check if Priority = "Critical"
3. If yes:
   - Call Escalate Issue with Request ID
   - Add comment: "Auto-escalated due to Critical priority"
   - Notify manager via email

### Use Case 3: Manual Escalation Button

**Scenario**: Provide a button in Krista interface for users to escalate change requests.

**Workflow**:
1. User selects change request from list
2. User clicks "Escalate to Manager" button
3. System calls Escalate Issue with selected Request ID
4. Display success message with manager name
5. Refresh change request details

## Related Catalog Requests

- **[Check Status](pages/CheckStatus.md)** - Check current status of change request after escalation
- **[Approve Change Request](pages/ApproveChangeRequest.md)** - Manager can approve the escalated change request
- **[Assign User to Incident](pages/AssignUserToIncident.md)** - Similar assignment functionality for incidents

## Technical Implementation

### Helper Class

**Package**: `app.krista.extensions.it.case_management.servicenow.catalog`

**Class**: `ChangeManagementArea`

**Method**: `escalateIssue(String requestID)`

### Implementation Details

```java
@CatalogRequest(
    id = "localDomainRequest_19b682cb-ab62-4e44-9cd2-d39d5185c40e",
    name = "Escalate issue",
    description = "Assigns the 'Change Request' to the manager of the assignment group.",
    area = "Change Management",
    type = CatalogRequest.Type.CHANGE_SYSTEM)
public ChangeRequest escalateIssue(@Field(name = "Request ID", type = "Text") String requestID)
```

### API Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/sn_chg_rest/change/normal` | GET | Retrieve change request by sys_id |
| `/api/sn_chg_rest/change/normal/{sys_id}` | PATCH | Update change request assignment |
| `/api/now/v1/table/sys_user_group` | GET | Retrieve assignment group details |

### Telemetry Metrics

The extension logs the following metrics:

- **Operation**: `escalateIssue`
- **Success/Failure**: Logged
- **Request ID**: Included in logs
- **Execution Time**: Tracked

## Troubleshooting

### Issue: "Change Request Not Found" Error

**Symptoms**:
- Error message about change request not existing
- Request fails immediately

**Diagnosis**:
1. Verify Request ID format (CHG0030001, not sys_id)
2. Check change request exists in ServiceNow
3. Verify you have read access to the change request

**Solution**:
- Use correct Request ID format
- Ensure change request exists
- Check ServiceNow permissions

### Issue: "No Manager" Error Persists

**Symptoms**:
- Error about missing manager even after assigning one
- Escalation fails repeatedly

**Diagnosis**:
1. Check manager field is actually saved in ServiceNow
2. Verify manager user is active
3. Check for caching issues

**Solution**:
1. In ServiceNow, open the group record
2. Verify Manager field shows a user
3. Ensure manager user account is active
4. Wait 1-2 minutes for cache refresh
5. Retry escalation

### Issue: Escalation Succeeds But No Notification

**Symptoms**:
- Escalation completes successfully
- Manager doesn't receive notification

**Diagnosis**:
- Check ServiceNow notification rules
- Verify manager's email address
- Check notification preferences

**Solution**:
1. In ServiceNow, navigate to **System Notification → Email → Notifications**
2. Find "Change Request Assigned" notification
3. Verify it's active and configured correctly
4. Check manager's notification preferences
5. Test notification manually

## See Also

- [Check Status](pages/CheckStatus.md) - Verify change request status after escalation
- [Approve Change Request](pages/ApproveChangeRequest.md) - Approve escalated change requests
- [Change Request Entity](pages/ChangeRequestEntity.md) - Full change request entity documentation
- [Troubleshooting](pages/troubleshooting.md) - General troubleshooting guide
- [Authentication](pages/Authentication.md) - Authentication and permissions

