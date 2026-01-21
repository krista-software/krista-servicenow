# Approve Change Request

## Overview

The **Approve Change Request** catalog request allows the currently authenticated user to approve a change request in ServiceNow. This operation updates the approval status for the change request, marking it as approved by the current user.

**Use Case Example**: A change manager receives a notification about a pending change request. After reviewing the details, they use "Approve Change Request" to approve it, allowing the change to proceed to the next stage.

## Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Approve Change Request |
| **Area** | Change Management |
| **Type** | CHANGE_SYSTEM |
| **Retry Support** | Yes - Safe to retry if request fails |
| **Idempotent** | Yes - Multiple approvals by same user have same effect |

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

Returns a **Change Request** entity with updated approval status:

| Field Name | Type | Description | Example |
|------------|------|-------------|---------|
| **Request ID** | Text | Change Request number | `CHG0030001` |
| **Priority** | Pick One | Priority level | `Critical`, `High`, `Moderate`, `Low` |
| **Risk** | Pick One | Risk assessment | `High`, `Medium`, `Low` |
| **Short Description** | Text | Brief description | `Update firewall rules` |
| **Model** | Text | Change model | `Normal`, `Standard`, `Emergency` |
| **Impact** | Pick One | Impact level | `1 - High`, `2 - Medium`, `3 - Low` |
| **State** | Text | Current state | `Authorize`, `Scheduled`, etc. |
| **Sys ID** | Text | System ID | `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6` |
| **Description** | Text | Detailed description | Full change description |
| **Assigned To** | Text | Currently assigned user | `John Doe` |
| **Assignment Group** | Text | Currently assigned group | `Network Operations` |
| **Requested By** | Text | Original requester | `Jane Smith` |
| **Approval** | Text | Approval status (updated) | `Approved` |

## Validation Rules

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Request ID Required** | Request ID parameter must be provided | "Request ID is required" |
| **Request ID Must Exist** | Change Request with given Request ID must exist | "Unable to retrieve change request. Please verify the Request ID is correct and the change request exists in ServiceNow." |
| **User Must Be Approver** | Current user must be in the approval list | "You are not authorized to approve this change request" |
| **Approval Record Must Exist** | Approval record must exist for the change request | "No approval record found for this change request" |

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

**Scenario 3: Invalid Request Data**
```
Error: Invalid request data. Please verify all input fields are correct and properly formatted.
Cause: Request data is malformed or contains invalid values
Resolution:
- Verify Request ID format is correct
- Check ServiceNow instance is accessible
- Retry the request
```

### LOGIC_ERROR

**Scenario: User Not in Approval List**
```
Error: You are not authorized to approve this change request
Cause: Current user is not listed as an approver for this change request
Resolution:
- Verify you are assigned as an approver in ServiceNow
- Check the change request's approval list
- Contact the change manager to add you as an approver
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
Resolution: Ensure user has 'itil' and 'sn_change_write' roles
```

## Usage Examples

### Example 1: Approve Standard Change Request

**Scenario**: A change manager approves a standard change request for firewall updates.

**Input**:
```
Request ID: CHG0030001
```

**Process**:
1. Request retrieves Change Request CHG0030001
2. Finds approval record for current user
3. Updates approval status to "Approved"
4. Returns updated Change Request

**Output**:
```
Change Request:
  Request ID: CHG0030001
  Short Description: Update firewall rules for new application
  State: Authorize
  Priority: High
  Risk: Medium
  Assignment Group: Network Operations
  Approval: Approved
  Requested By: Jane Technician
```

### Example 2: Approve Critical Change

**Scenario**: Manager approves a critical infrastructure change.

**Input**:
```
Request ID: CHG0030045
```

**Output**:
```
Change Request:
  Request ID: CHG0030045
  Short Description: Emergency database server upgrade
  State: Authorize
  Priority: Critical
  Risk: High
  Assignment Group: Database Administration
  Approval: Approved
```

### Example 3: Error - User Not Approver

**Scenario**: User attempts to approve a change request they're not authorized for.

**Input**:
```
Request ID: CHG0030099
```

**Error**:
```
You are not authorized to approve this change request
```

**Resolution**:
1. Contact the change manager
2. Request to be added as an approver
3. Wait for approval assignment
4. Retry the approval

## Business Rules

### Approval Logic

1. **Retrieve Change Request**: Fetches the change request by Request ID
2. **Find Approval Record**: Searches for approval record matching current user
3. **Validate Approver**: Ensures current user is authorized to approve
4. **Update Approval**: Sets approval state to "Approved"
5. **Return Updated Record**: Returns the updated change request

### User Context

- **Current User**: Approval is recorded under the currently authenticated user
- **OAuth**: Uses the email of the OAuth-authenticated user
- **Basic Auth**: Uses the username from Basic Authentication
- **Audit Trail**: ServiceNow records who approved and when

### State Transitions

- Approval may trigger automatic state transitions based on ServiceNow workflow
- Common transitions:
  - `Authorize` → `Scheduled` (when all approvals complete)
  - May remain in `Authorize` if additional approvals needed

### Notifications

ServiceNow may send notifications based on configured rules:
- Change requester receives "Change Approved" notification
- Other approvers may be notified
- Change manager receives approval confirmation

## Limitations

1. **Single User Approval**: Approves only for current user, not on behalf of others
2. **No Rejection**: This request only approves, cannot reject (use ServiceNow UI)
3. **No Comments**: Cannot add approval comments (use ServiceNow UI)
4. **No Delegation**: Cannot delegate approval to another user
5. **Requires Approval Record**: User must be in the approval list

## Best Practices

### When to Use Approve Change Request

✅ **Use When**:
- You are assigned as an approver
- You have reviewed the change request details
- You want to approve from Krista workflow
- Automating approval based on conditions
- Integrating approval into custom workflows

❌ **Don't Use When**:
- You want to reject a change request (use ServiceNow UI)
- You need to add approval comments (use ServiceNow UI)
- You're not in the approval list (request to be added first)
- You want to approve on behalf of someone else (not supported)

### Preparation Checklist

Before approving a change request:

- [ ] Verify you are assigned as an approver
- [ ] Review change request details using Check Status
- [ ] Verify change request is in appropriate state (usually "Authorize")
- [ ] Ensure you have authority to approve
- [ ] Check you have required permissions (itil, sn_change_write)

### Error Prevention

1. **Verify Approver Status**: Check you're in the approval list before attempting
2. **Review Before Approving**: Use Check Status to review details first
3. **Handle Errors Gracefully**: Implement error handling in workflows
4. **Log Approvals**: Track approvals for audit purposes
5. **Validate State**: Ensure change request is in correct state for approval

## Common Use Cases

### Use Case 1: Automated Approval Workflow

**Scenario**: Automatically approve low-risk change requests that meet criteria.

**Workflow**:
1. Trigger on new change request notification
2. Call Check Status to get details
3. Evaluate criteria:
   - Risk = "Low"
   - Impact = "3 - Low"
   - Requester is trusted user
4. If criteria met:
   - Call Approve Change Request
   - Log approval action
   - Send confirmation notification

### Use Case 2: Manager Approval Dashboard

**Scenario**: Provide managers with a dashboard to review and approve pending changes.

**Workflow**:
1. Display list of pending change requests
2. Manager selects a change request
3. Display full details using Check Status
4. Manager clicks "Approve" button
5. Call Approve Change Request with Request ID
6. Display success message
7. Refresh pending list

### Use Case 3: Conditional Approval

**Scenario**: Approve change requests only during maintenance windows.

**Workflow**:
1. Check current time
2. If within maintenance window:
   - Call Approve Change Request
   - Log approval with timestamp
3. If outside maintenance window:
   - Queue for later approval
   - Send notification to manager

### Use Case 4: Multi-Stage Approval

**Scenario**: Implement multi-stage approval process with different approvers.

**Workflow**:
1. Stage 1: Technical lead approves (using this request)
2. Call Check Status to verify approval
3. If approved, notify change manager
4. Stage 2: Change manager approves (using this request)
5. Call Check Status to verify all approvals
6. If all approved, proceed to scheduling

## Related Catalog Requests

- **[Check Status](pages/CheckStatus.md)** - Review change request details before approving
- **[Escalate Issue](pages/EscalateIssue.md)** - Escalate to manager if approval needed
- **[Change Request Entity](pages/ChangeRequestEntity.md)** - Full change request operations

## Technical Implementation

### Helper Class

**Package**: `app.krista.extensions.it.case_management.servicenow.catalog`

**Class**: `ChangeManagementArea`

**Method**: `approveChangeRequest(String requestID)`

### Implementation Details

```java
@CatalogRequest(
    id = "localDomainRequest_d27e1744-5a22-44b4-9f09-189c83816054",
    name = "Approve Change Request",
    description = "Currently logged in user approves the change request matching to the input Request ID.",
    area = "Change Management",
    type = CatalogRequest.Type.CHANGE_SYSTEM)
public ChangeRequest approveChangeRequest(@Field(name = "Request ID", type = "Text") String requestID)
```

### API Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/sn_chg_rest/change/normal` | GET | Retrieve change request by Request ID |
| `/api/now/table/change_request/{sys_id}` | PATCH | Update approval status |
| `/api/now/v1/table/sysapproval_approver` | GET | Find approval record for user |

### Telemetry Metrics

The extension logs the following metrics:

- **Operation**: `approveChangeRequest`
- **Success/Failure**: Logged
- **Request ID**: Included in logs
- **User**: Current user email/username
- **Execution Time**: Tracked

## Troubleshooting

### Issue: "Not Authorized to Approve" Error

**Symptoms**:
- Error message about not being authorized
- Approval fails even though you should be an approver

**Diagnosis**:
1. Check if you're in the approval list in ServiceNow
2. Verify approval record exists
3. Check if approval was already completed

**Solution**:
1. In ServiceNow, open the change request
2. Go to "Approvals" related list
3. Verify your name appears as an approver
4. Check approval state is "Requested" not "Approved"
5. If not in list, contact change manager to add you

### Issue: Approval Succeeds But State Doesn't Change

**Symptoms**:
- Approval completes successfully
- Change request state remains "Authorize"

**Diagnosis**:
- Check if multiple approvals are required
- Verify ServiceNow workflow configuration
- Check for approval conditions

**Solution**:
1. Use Check Status to verify approval was recorded
2. Check if other approvers are pending
3. Review ServiceNow workflow rules
4. Wait for all required approvals
5. Contact ServiceNow administrator if workflow issue

### Issue: "Invalid Request Data" Error

**Symptoms**:
- Error about invalid request data
- Approval fails with generic error

**Diagnosis**:
- Check Request ID format
- Verify ServiceNow instance accessibility
- Check for API errors in ServiceNow logs

**Solution**:
1. Verify Request ID is correct format (CHG0030001)
2. Test ServiceNow instance accessibility
3. Check ServiceNow system logs for errors
4. Verify authentication is valid
5. Retry the request

## See Also

- [Check Status](pages/CheckStatus.md) - Review change request before approving
- [Escalate Issue](pages/EscalateIssue.md) - Escalate for management approval
- [Change Request Entity](pages/ChangeRequestEntity.md) - Full change request operations
- [Troubleshooting](pages/troubleshooting.md) - General troubleshooting guide
- [Authentication](pages/Authentication.md) - Authentication and permissions

