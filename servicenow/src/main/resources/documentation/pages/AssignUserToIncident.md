# Assign User to Incident

## Overview

The **Assign User to Incident** catalog request assigns a ServiceNow incident to a specific user. This operation updates the "Assigned To" field of an incident, transferring responsibility to the specified user.

**Use Case Example**: A help desk manager receives an incident that requires specialized expertise. They use "Assign User to Incident" to assign it to a senior technician with the necessary skills.

## Request Details

| Property | Value |
|----------|-------|
| **Request Name** | Assign User to Incident |
| **Area** | Ticket Management |
| **Type** | CHANGE_SYSTEM |
| **Retry Support** | Yes - Safe to retry if request fails |
| **Idempotent** | Yes - Multiple assignments to same user have same effect |

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| **Sys ID User** | Text | Yes | System ID (sys_id) of the user to assign | `a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6` |
| **Sys ID Incident** | Text | Yes | System ID (sys_id) of the incident | `b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7` |

### Parameter Details

#### Sys ID User
- **Format**: ServiceNow System ID (32-character hexadecimal string)
- **Not**: Username or email - must be the sys_id
- **Case Sensitive**: No
- **Validation**: User must exist in ServiceNow
- **Where to Find**: 
  - Use User Entity search to find user by name/email
  - In ServiceNow: Right-click user field → Copy sys_id
  - In ServiceNow URL when viewing user: `sys_user.do?sys_id=<sys_id>`

#### Sys ID Incident
- **Format**: ServiceNow System ID (32-character hexadecimal string)
- **Not**: Incident number (INC0010001) - must be the sys_id
- **Case Sensitive**: No
- **Validation**: Incident must exist in ServiceNow
- **Where to Find**:
  - Use Incident Entity search to find incident
  - In ServiceNow: Right-click incident → Copy sys_id
  - In ServiceNow URL when viewing incident: `incident.do?sys_id=<sys_id>`

## Output Parameters

Returns a **Boolean** value indicating success or failure:

| Value | Description |
|-------|-------------|
| `true` | Incident successfully assigned to user |
| `false` | Assignment failed (user doesn't exist or incident not found) |

## Validation Rules

| Rule | Description | Error Behavior |
|------|-------------|----------------|
| **Sys ID User Required** | User System ID must be provided | Returns `false` |
| **Sys ID Incident Required** | Incident System ID must be provided | Returns `false` |
| **User Must Exist** | User with given sys_id must exist in ServiceNow | Returns `false` |
| **Incident Must Exist** | Incident with given sys_id must exist in ServiceNow | Returns `false` |

## Error Handling

### INPUT_ERROR

**Scenario 1: Sys ID User Not Provided**
```
Result: false
Cause: Sys ID User parameter is null or empty
Resolution: Provide a valid user System ID
```

**Scenario 2: Sys ID Incident Not Provided**
```
Result: false
Cause: Sys ID Incident parameter is null or empty
Resolution: Provide a valid incident System ID
```

**Scenario 3: User Does Not Exist**
```
Result: false
Cause: No user found with the provided System ID
Resolution: 
- Verify the user System ID is correct
- Use User Entity search to find the correct sys_id
- Ensure user account is active in ServiceNow
```

**Scenario 4: Incident Does Not Exist**
```
Result: false
Cause: No incident found with the provided System ID
Resolution:
- Verify the incident System ID is correct
- Use Incident Entity search to find the correct sys_id
- Ensure incident exists in ServiceNow
```

### SYSTEM_ERROR

**Scenario: ServiceNow API Error**
```
Result: false
Cause: ServiceNow API returned error or unexpected response
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
Resolution: Ensure user has 'itil' role
```

## Usage Examples

### Example 1: Assign Incident to Specialist

**Scenario**: Assign a network-related incident to a network specialist.

**Input**:
```
Sys ID User: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
Sys ID Incident: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
```

**Process**:
1. Validate user exists (sys_id: a1b2c3d4...)
2. Update incident's "Assigned To" field
3. Return success status

**Output**:
```
true
```

**Result**: Incident is now assigned to the specified user.

### Example 2: Reassign Incident to Different Technician

**Scenario**: Reassign an incident from one technician to another.

**Input**:
```
Sys ID User: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
Sys ID Incident: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
```

**Output**:
```
true
```

**Result**: Incident is reassigned to the new technician.

### Example 3: Error - User Does Not Exist

**Scenario**: Attempting to assign incident to non-existent user.

**Input**:
```
Sys ID User: invalid_sys_id_12345
Sys ID Incident: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
```

**Output**:
```
false
```

**Resolution**:
1. Use User Entity search to find correct user
2. Get the correct sys_id
3. Retry with valid sys_id

### Example 4: Workflow Integration

**Scenario**: Automatically assign high-priority incidents to senior technicians.

**Workflow**:
1. Search for incidents with Priority = "1 - High" and Assigned To = empty
2. For each incident:
   - Get senior technician sys_id from User Entity
   - Call Assign User to Incident
   - If result = true, log success
   - If result = false, log error and notify manager

## Business Rules

### Assignment Logic

1. **Validate User**: Checks if user exists in ServiceNow
2. **Update Incident**: Sets "Assigned To" field to specified user
3. **Return Status**: Returns true if successful, false if failed

### User Validation

- User must exist in ServiceNow sys_user table
- User account must be active
- No role validation - any user can be assigned

### State Transitions

- Assignment does **not** change incident state
- Incident remains in current state (New, In Progress, etc.)
- Only "Assigned To" field is updated

### Notifications

ServiceNow may send notifications based on configured rules:
- Assigned user receives "Incident Assigned" notification
- Previous assignee may receive "Incident Reassigned" notification
- Caller may be notified of assignment

## Limitations

1. **Requires System IDs**: Must use sys_id, not incident number or username
2. **No Validation of User Skills**: Does not check if user has required skills
3. **No Group Assignment**: Assigns to individual user only, not groups
4. **No State Change**: Does not automatically change incident state
5. **Boolean Return Only**: Returns true/false, not updated incident details
6. **No Assignment Reason**: Cannot specify reason for assignment

## Best Practices

### When to Use Assign User to Incident

✅ **Use When**:
- Assigning incident to specific user
- Reassigning incident to different technician
- Automating incident assignment based on skills/availability
- Load balancing incidents across team members
- Escalating incident to senior technician

❌ **Don't Use When**:
- You want to assign to a group (use Incident Entity update instead)
- You need to change incident state (use Incident Entity update)
- You want to add assignment comments (use Incident Entity update)
- You need full incident details in response (use Incident Entity get after assignment)

### Preparation Checklist

Before assigning an incident:

- [ ] Obtain user System ID (use User Entity search)
- [ ] Obtain incident System ID (use Incident Entity search)
- [ ] Verify user is active and available
- [ ] Check user has appropriate skills for the incident
- [ ] Ensure you have permission to assign incidents (itil role)

### Error Prevention

1. **Always Use System IDs**: Never use incident numbers or usernames
2. **Validate Before Assignment**: Check user and incident exist first
3. **Handle False Returns**: Implement logic for failed assignments
4. **Log Assignments**: Track all assignments for audit purposes
5. **Verify After Assignment**: Use Incident Entity get to confirm assignment

## Common Use Cases

### Use Case 1: Skill-Based Routing

**Scenario**: Automatically assign incidents based on required skills.

**Workflow**:
1. Search for unassigned incidents
2. For each incident:
   - Determine required skill from category/description
   - Search User Entity for users with that skill
   - Select available user with lowest workload
   - Call Assign User to Incident
   - If true, log assignment
   - If false, escalate to manager

### Use Case 2: Round-Robin Assignment

**Scenario**: Distribute incidents evenly across team members.

**Workflow**:
1. Maintain list of team member sys_ids
2. Track last assigned index
3. When new incident arrives:
   - Get next user in rotation
   - Call Assign User to Incident
   - If true, increment index
   - If false, try next user in list

### Use Case 3: Priority-Based Assignment

**Scenario**: Assign high-priority incidents to senior technicians.

**Workflow**:
1. Search for new incidents
2. For each incident:
   - Check priority level
   - If Priority = "1 - High":
     - Get senior technician sys_id
     - Call Assign User to Incident
   - If Priority = "2 - Medium" or "3 - Low":
     - Get junior technician sys_id
     - Call Assign User to Incident

### Use Case 4: Reassignment After Timeout

**Scenario**: Reassign incidents that haven't been updated in 24 hours.

**Workflow**:
1. Search for incidents:
   - State = "In Progress"
   - Last Updated > 24 hours ago
2. For each incident:
   - Get current assignee's manager sys_id
   - Call Assign User to Incident (assign to manager)
   - Add comment: "Reassigned due to inactivity"
   - Send notification

## Related Catalog Requests

- **[Escalate Issue](pages/EscalateIssue.md)** - Similar escalation for change requests
- **[Check Status](pages/CheckStatus.md)** - Check change request status
- **[Incident Entity](pages/IncidentEntity.md)** - Full incident operations including assignment

## Technical Implementation

### Helper Class

**Package**: `app.krista.extensions.it.case_management.servicenow.catalog`

**Class**: `TicketManagementArea`

**Method**: `assignUserToIncident(String sysIDUser, String sysIDIncident)`

### Implementation Details

```java
@CatalogRequest(
    id = "localDomainRequest_813da8c1-80e7-44d4-87c5-b9294dc3b814",
    name = "Assign user to incident",
    description = "Accepts system user ID, incident ID and assigns the incident to the user",
    area = "Ticket Management",
    type = CatalogRequest.Type.CHANGE_SYSTEM)
public Boolean assignUserToIncident(
    @Field(name = "Sys ID user", type = "Text") String sysIDUser,
    @Field(name = "Sys ID Incident", type = "Text") String sysIDIncident)
```

### API Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/now/v1/table/sys_user/{sys_id}` | GET | Validate user exists |
| `/api/now/v1/table/incident/{sys_id}` | PUT | Update incident assignment |

### Telemetry Metrics

The extension logs the following metrics:

- **Operation**: `assignUserToIncident`
- **Success/Failure**: Logged
- **User Sys ID**: Included in logs
- **Incident Sys ID**: Included in logs
- **Execution Time**: Tracked

## Troubleshooting

### Issue: Always Returns False

**Symptoms**:
- Assignment always fails
- Returns false even with valid IDs

**Diagnosis**:
1. Verify you're using System IDs, not incident numbers or usernames
2. Check user exists in ServiceNow
3. Check incident exists in ServiceNow
4. Verify authentication is valid

**Solution**:
1. Use User Entity search to get correct user sys_id
2. Use Incident Entity search to get correct incident sys_id
3. Verify both IDs are 32-character hexadecimal strings
4. Test with known valid IDs

### Issue: Assignment Succeeds But User Not Notified

**Symptoms**:
- Returns true
- Incident shows correct assignment in ServiceNow
- User doesn't receive notification

**Diagnosis**:
- Check ServiceNow notification rules
- Verify user's email address
- Check notification preferences

**Solution**:
1. In ServiceNow, navigate to **System Notification → Email → Notifications**
2. Find "Incident Assigned" notification
3. Verify it's active
4. Check user's notification preferences
5. Test notification manually

### Issue: Cannot Find System IDs

**Symptoms**:
- Don't know how to get sys_id values
- Using incident numbers instead of sys_id

**Solution**:
1. **For User Sys ID**:
   - Use User Entity search with email or name
   - Get sys_id from search results
   
2. **For Incident Sys ID**:
   - Use Incident Entity search with incident number
   - Get sys_id from search results
   
3. **In ServiceNow UI**:
   - Right-click on record
   - Select "Copy sys_id"

## See Also

- [Incident Entity](pages/IncidentEntity.md) - Full incident operations
- [User Entity](pages/UserEntity.md) - Search and retrieve user information
- [Escalate Issue](pages/EscalateIssue.md) - Escalate change requests
- [Troubleshooting](pages/troubleshooting.md) - General troubleshooting guide
- [Authentication](pages/Authentication.md) - Authentication and permissions

