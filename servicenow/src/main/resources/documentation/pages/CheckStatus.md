# Check Status

## Overview

The **Check Status** catalog request retrieves the current status and details of a Change Request from ServiceNow. This is a read-only operation that provides complete information about a change request including its state, priority, assignments, and approval status.

**Use Case Example**: A project manager wants to check if a scheduled change request has been approved and is ready for implementation. They use "Check Status" with the Request ID to get the current state and approval status.

## Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Check Status |
| **Area** | Change Management |
| **Type** | CHANGE_SYSTEM |
| **Retry Support** | Yes - Safe to retry, read-only operation |
| **Idempotent** | Yes - Multiple calls return same result |

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

Returns a **Change Request** entity with complete details:

| Field Name | Type | Description | Example |
|------------|------|-------------|---------|
| **Request ID** | Text | Change Request number | `CHG0030001` |
| **Priority** | Pick One | Priority level | `Critical`, `High`, `Moderate`, `Low` |
| **Risk** | Pick One | Risk assessment | `High`, `Medium`, `Low` |
| **Short Description** | Text | Brief description | `Update firewall rules` |
| **Model** | Text | Change model | `Normal`, `Standard`, `Emergency` |
| **Impact** | Pick One | Impact level | `1 - High`, `2 - Medium`, `3 - Low` |
| **State** | Text | Current state | `New`, `Assess`, `Authorize`, `Scheduled`, `Implement`, `Review`, `Closed` |
| **Sys ID** | Text | System ID | `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6` |
| **Description** | Text | Detailed description | Full change description |
| **Assigned To** | Text | Currently assigned user | `John Doe` |
| **Assignment Group** | Text | Currently assigned group | `Network Operations` |
| **Requested By** | Text | Original requester | `Jane Smith` |
| **Approval** | Text | Approval status | `Not Yet Requested`, `Requested`, `Approved`, `Rejected` |

## Validation Rules

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Request ID Required** | Request ID parameter must be provided | "Request ID is required" |
| **Request ID Must Exist** | Change Request with given Request ID must exist | "Unable to retrieve change request. Please verify the Request ID is correct and the change request exists in ServiceNow." |

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
Resolution: Ensure user has 'itil' or 'sn_change_read' role
```

## Usage Examples

### Example 1: Check Status of Scheduled Change

**Scenario**: Verify if a scheduled change request is ready for implementation.

**Input**:
```
Request ID: CHG0030001
```

**Output**:
```
Change Request:
  Request ID: CHG0030001
  Short Description: Update firewall rules for new application
  State: Scheduled
  Priority: High
  Risk: Medium
  Assignment Group: Network Operations
  Assigned To: John Technician
  Approval: Approved
  Model: Normal
  Impact: 2 - Medium
  Requested By: Jane Manager
```

**Interpretation**: Change is approved and scheduled, ready for implementation.

### Example 2: Monitor Change Request Progress

**Scenario**: Track a change request through its lifecycle.

**Input**:
```
Request ID: CHG0030045
```

**Output**:
```
Change Request:
  Request ID: CHG0030045
  Short Description: Database server upgrade
  State: Implement
  Priority: Critical
  Risk: High
  Assignment Group: Database Administration
  Assigned To: Sarah DBA
  Approval: Approved
  Model: Normal
  Impact: 1 - High
```

**Interpretation**: Change is currently being implemented by Sarah DBA.

### Example 3: Check Approval Status

**Scenario**: Determine if a change request has been approved.

**Input**:
```
Request ID: CHG0030099
```

**Output**:
```
Change Request:
  Request ID: CHG0030099
  Short Description: Install security patches
  State: Authorize
  Priority: Moderate
  Risk: Low
  Assignment Group: System Administration
  Assigned To: Mike Admin
  Approval: Requested
  Model: Normal
  Impact: 3 - Low
```

**Interpretation**: Change is awaiting approval, currently in Authorize state.

## Business Rules

### Read-Only Operation

- **No Modifications**: This request only retrieves data, does not modify anything
- **Safe to Retry**: Can be called multiple times without side effects
- **No State Changes**: Does not trigger any state transitions
- **No Notifications**: Does not send any notifications

### Data Freshness

- **Real-Time**: Always retrieves current data from ServiceNow
- **No Caching**: Each call fetches fresh data
- **Immediate Reflection**: Shows changes made by other users immediately

### Permission Requirements

- **Read Access**: User must have read access to change requests
- **Role Required**: `itil` or `sn_change_read` role
- **Record-Level Security**: Respects ServiceNow ACLs and security rules

## Limitations

1. **Read-Only**: Cannot modify change request data
2. **Single Request**: Retrieves one change request at a time
3. **No Bulk Operations**: Cannot check status of multiple requests in one call
4. **No History**: Returns current state only, not historical changes
5. **No Related Records**: Does not include related tasks, approvals, or attachments

## Best Practices

### When to Use Check Status

✅ **Use When**:
- Monitoring change request progress
- Verifying approval status before proceeding
- Checking current assignment
- Validating change request state in workflows
- Displaying change request details to users
- Polling for state changes

❌ **Don't Use When**:
- You need to modify the change request (use Update Change Request entity)
- You need historical data (use ServiceNow reporting)
- You need related records (use entity search with filters)
- You want to trigger state transitions (use appropriate catalog requests)

### Polling Best Practices

If polling for status changes:

1. **Use Reasonable Intervals**: Poll every 30-60 seconds, not more frequently
2. **Implement Timeout**: Set maximum polling duration (e.g., 30 minutes)
3. **Handle Errors**: Implement retry logic with exponential backoff
4. **Cache Results**: Store previous state to detect changes
5. **Limit Iterations**: Set maximum number of polling attempts

### Error Handling

1. **Validate Input**: Check Request ID format before calling
2. **Handle Not Found**: Gracefully handle non-existent requests
3. **Retry on Failure**: Implement retry logic for transient errors
4. **Log Errors**: Log all errors for troubleshooting
5. **User-Friendly Messages**: Display clear error messages to users

## Common Use Cases

### Use Case 1: Automated Status Monitoring

**Scenario**: Monitor change requests and send notifications when state changes.

**Workflow**:
1. Maintain list of change requests to monitor
2. Every 5 minutes:
   - For each Request ID, call Check Status
   - Compare current state with previous state
   - If state changed, send notification
   - Update stored state

**Example**:
```
Previous State: Authorize
Current State: Scheduled
Action: Send email "Change CHG0030001 has been approved and scheduled"
```

### Use Case 2: Pre-Implementation Validation

**Scenario**: Verify change request is approved before starting implementation.

**Workflow**:
1. User initiates implementation workflow
2. Call Check Status with Request ID
3. Verify:
   - State is "Scheduled" or "Implement"
   - Approval is "Approved"
   - Assignment matches current user
4. If valid, proceed with implementation
5. If not valid, display error and stop

### Use Case 3: Dashboard Display

**Scenario**: Display change request details in a Krista dashboard.

**Workflow**:
1. User selects change request from list
2. Call Check Status with selected Request ID
3. Display all fields in dashboard:
   - Status badge (color-coded by state)
   - Priority indicator
   - Assignment information
   - Approval status
   - Description and details
4. Provide "Refresh" button to update data

### Use Case 4: Conditional Workflow Routing

**Scenario**: Route workflow based on change request state.

**Workflow**:
1. Call Check Status with Request ID
2. Based on State field:
   - If "New" → Route to assessment team
   - If "Assess" → Route to technical review
   - If "Authorize" → Route to approval workflow
   - If "Scheduled" → Route to implementation team
   - If "Implement" → Route to monitoring
   - If "Review" → Route to closure workflow

## Related Catalog Requests

- **[Escalate Issue](pages/EscalateIssue.md)** - Escalate change request after checking status
- **[Approve Change Request](pages/ApproveChangeRequest.md)** - Approve change request if status shows pending approval
- **[Change Request Entity](pages/ChangeRequestEntity.md)** - Full CRUD operations on change requests

## Technical Implementation

### Helper Class

**Package**: `app.krista.extensions.it.case_management.servicenow.catalog`

**Class**: `ChangeManagementArea`

**Method**: `checkStatus(String requestID)`

### Implementation Details

```java
@CatalogRequest(
    id = "localDomainRequest_07d51e76-7757-4a02-80c5-5f29e0278d14",
    name = "Check Status",
    description = "Accepts the Request ID of the 'Change Request' and returns the current status.",
    area = "Change Management",
    type = CatalogRequest.Type.CHANGE_SYSTEM)
public ChangeRequest checkStatus(@Field(name = "Request ID", type = "Text") String requestID)
```

### API Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/sn_chg_rest/change/normal` | GET | Retrieve change request by Request ID |

### Telemetry Metrics

The extension logs the following metrics:

- **Operation**: `checkStatus`
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
- Use correct Request ID format (CHG prefix + number)
- Ensure change request exists in ServiceNow
- Check ServiceNow permissions (itil or sn_change_read role)

### Issue: Outdated Data Returned

**Symptoms**:
- Status shows old information
- Recent changes not reflected

**Diagnosis**:
- Check if caching is enabled in your workflow
- Verify ServiceNow instance is accessible
- Check for network delays

**Solution**:
- Disable caching for Check Status calls
- Ensure direct API calls to ServiceNow
- Add small delay (1-2 seconds) after updates before checking status

### Issue: Permission Denied

**Symptoms**:
- "Insufficient permissions" error
- Cannot retrieve change request

**Diagnosis**:
- Check user's ServiceNow roles
- Verify ACL rules on change_request table
- Check if change request is restricted

**Solution**:
1. Verify user has `itil` or `sn_change_read` role
2. Check ServiceNow ACL rules for change_request table
3. Ensure change request is not restricted to specific groups
4. Contact ServiceNow administrator if needed

## See Also

- [Escalate Issue](pages/EscalateIssue.md) - Escalate change request to manager
- [Approve Change Request](pages/ApproveChangeRequest.md) - Approve pending change requests
- [Change Request Entity](pages/ChangeRequestEntity.md) - Full change request entity operations
- [Troubleshooting](pages/troubleshooting.md) - General troubleshooting guide
- [Authentication](pages/Authentication.md) - Authentication and permissions

