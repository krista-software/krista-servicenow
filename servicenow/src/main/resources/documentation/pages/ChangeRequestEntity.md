# Change Request Entity

## Overview

The **Change Request** entity represents a ServiceNow change request record. It provides full CRUD (Create, Read, Update, Delete) operations and advanced search capabilities for managing change requests in ServiceNow.

Change requests are used to track and manage changes to IT infrastructure, applications, and services following ITIL change management processes.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Change Request |
| **Primary Key** | Sys ID |
| **ServiceNow Table** | `change_request` |
| **Searchable** | Yes |
| **Operations** | Create, Get, Search, Delete |

## Fields

| Field Name | Type | Required | Searchable | Description |
|------------|------|----------|------------|-------------|
| **Request ID** | Text | No | Yes | Change request number (e.g., CHG0030001) |
| **Priority** | Pick One | No | Yes | Priority level: Low, Moderate, High, Critical |
| **Risk** | Pick One | No | No | Risk assessment: Low, Moderate, High |
| **Short Description** | Text | No | Yes | Brief description of the change |
| **Model** | Text | No | No | Change model: Normal, Standard, Emergency |
| **Impact** | Pick One | No | No | Impact level: Low, Medium, High |
| **State** | Text | No | Yes | Current state of the change request |
| **Sys ID** | Text | No | Yes | System ID (primary key) |
| **Description** | Text | No | No | Detailed description of the change |
| **Assigned To** | Text | No | Yes | User currently assigned |
| **Assignment Group** | Text | No | Yes | Group currently assigned |
| **Requested By** | Text | No | Yes | User who requested the change |
| **Approval** | Text | No | No | Approval status |

### Field Details

#### Request ID
- **Format**: CHG followed by 7 digits (e.g., `CHG0030001`)
- **Auto-generated**: Yes, by ServiceNow upon creation
- **Unique**: Yes
- **Usage**: Human-readable identifier for the change request

#### Priority
- **Values**: `Low`, `Moderate`, `High`, `Critical`
- **Default**: Based on Impact and Urgency calculation
- **Usage**: Determines processing priority

#### Risk
- **Values**: `Low`, `Moderate`, `High`
- **Default**: None
- **Usage**: Risk assessment for the change

#### State
- **Common Values**: `New`, `Assess`, `Authorize`, `Scheduled`, `Implement`, `Review`, `Closed`, `Cancelled`
- **Workflow**: Follows ServiceNow change management workflow
- **Transitions**: Controlled by business rules and approvals

#### Sys ID
- **Format**: 32-character hexadecimal string
- **Auto-generated**: Yes, by ServiceNow
- **Immutable**: Yes
- **Usage**: Primary key for all operations

## Operations

### Create

Creates a new change request in ServiceNow.

**Syntax**:
```
Create Change Request with:
  Short Description: "Update firewall rules"
  Description: "Add new firewall rules for application XYZ"
  Priority: "High"
  Risk: "Medium"
  Impact: "Medium"
  Assignment Group: "Network Operations"
```

**Required Fields**:
- Short Description (recommended)
- Assignment Group (required if State is not "New")

**Behavior**:
- If State is null, blank, or "New": Creates change request in "New" state
- If State is provided and Assignment Group is set: Creates change request and moves to "Assess" state
- If State is provided but Assignment Group is missing: Throws error

**Returns**: Created Change Request entity with all fields populated

**Example**:
```
Input:
  Short Description: "Database server upgrade"
  Priority: "Critical"
  Risk: "High"
  Assignment Group: "Database Administration"
  State: "Assess"

Output:
  Request ID: CHG0030045
  Sys ID: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  State: Assess
  Priority: Critical
  (all other fields populated)
```

### Get

Retrieves a change request by its System ID.

**Syntax**:
```
Get Change Request by Sys ID: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

**Parameters**:
- **Sys ID** (required): System ID of the change request

**Returns**: Change Request entity with all fields

**Example**:
```
Input:
  Sys ID: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Output:
  Request ID: CHG0030001
  Short Description: Update firewall rules
  State: Scheduled
  Priority: High
  Assignment Group: Network Operations
  (all other fields)
```

### Search

Searches for change requests based on specified criteria.

**Searchable Fields**:
- Request ID
- Priority
- Short Description
- State
- Sys ID
- Assigned To
- Assignment Group
- Requested By

**Search Operators**:
- **Text fields**: `equals`, `contains`, `startswith`, `endswith`
- **Pick One fields**: `equals`

**Syntax**:
```
Search Change Request where:
  Priority equals "High"
  State equals "Assess"
```

**Pagination**:
- **Page Index**: 0-based page number
- **Page Size**: Number of results per page (max 1000)

**Returns**: List of Change Request entities matching criteria

**Examples**:

**Example 1: Search by Request ID**
```
Search where Request ID equals "CHG0030001"
Returns: Single change request with that ID
```

**Example 2: Search by Priority**
```
Search where Priority equals "Critical"
Returns: All critical priority change requests
```

**Example 3: Search by Assignment Group**
```
Search where Assignment Group contains "Network"
Returns: All change requests assigned to groups containing "Network"
```

**Example 4: Search by State**
```
Search where State equals "Assess"
Page Index: 0
Page Size: 50
Returns: First 50 change requests in Assess state
```

### Delete

Deletes a change request by its System ID.

**Syntax**:
```
Delete Change Request by Sys ID: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

**Parameters**:
- **Sys ID** (required): System ID of the change request to delete

**Returns**: void (no return value)

**Warning**: This operation is permanent and cannot be undone.

**Example**:
```
Input:
  Sys ID: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Result: Change request is permanently deleted from ServiceNow
```

## Validation Rules

### Create Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Assignment Group Required for State Change** | If State is not "New", Assignment Group must be provided | "An assignment group is required to change the state of the change request. Please assign a group before moving to a different state." |
| **Valid Field Values** | All field values must be valid | "Failed to create change request. Please verify all required fields are provided, especially the Assignment Group." |

### Get Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys ID Required** | System ID must be provided | "Invalid input" |
| **Change Request Must Exist** | Change request with given Sys ID must exist | "Invalid input" |

### Search Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Valid Search Criteria** | At least one search condition must be provided | "Search criteria cannot be empty. Please provide at least one search condition and try again." |
| **Valid Field Names** | Field names must be searchable | Search returns empty list |

### Delete Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys ID Required** | System ID must be provided | Error thrown |
| **Change Request Must Exist** | Change request must exist to be deleted | Error thrown |

## Error Handling

### Create Errors

**Error: Assignment Group Required**
```
Error: An assignment group is required to change the state of the change request. Please assign a group before moving to a different state.
Cause: Attempting to create change request with State other than "New" without Assignment Group
Resolution: Provide Assignment Group or set State to "New"
```

**Error: Creation Failed**
```
Error: Failed to create change request. Please verify all required fields are provided, especially the Assignment Group.
Cause: Invalid field values or ServiceNow API error
Resolution: Verify all field values are valid and retry
```

### Get Errors

**Error: Invalid Input**
```
Error: Invalid input
Cause: Invalid Sys ID or change request doesn't exist
Resolution: Verify Sys ID is correct and change request exists
```

### Search Errors

**Error: Empty Search Criteria**
```
Error: Search criteria cannot be empty. Please provide at least one search condition and try again.
Cause: No search conditions provided
Resolution: Provide at least one search condition
```

### Delete Errors

**Error: Delete Failed**
```
Error: Unable to delete change request
Cause: Invalid Sys ID or ServiceNow API error
Resolution: Verify Sys ID is correct and retry
```

## Usage Examples

### Example 1: Create Standard Change Request

**Scenario**: Create a new change request for routine maintenance.

```
Create Change Request:
  Short Description: "Monthly server patching"
  Description: "Apply security patches to production servers"
  Priority: "Moderate"
  Risk: "Low"
  Impact: "Low"
  Assignment Group: "System Administration"
  State: "Assess"

Result:
  Request ID: CHG0030050
  Sys ID: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
  State: Assess
  Priority: Moderate
```

### Example 2: Search for High Priority Changes

**Scenario**: Find all high-priority change requests in Assess state.

```
Search Change Request:
  Priority equals "High"
  State equals "Assess"
  Page Index: 0
  Page Size: 100

Result: List of high-priority change requests awaiting assessment
```

### Example 3: Get Change Request Details

**Scenario**: Retrieve full details of a specific change request.

```
Get Change Request:
  Sys ID: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Result:
  Request ID: CHG0030001
  Short Description: Update firewall rules
  State: Scheduled
  Priority: High
  Risk: Medium
  Assignment Group: Network Operations
  Assigned To: John Technician
  Approval: Approved
```

### Example 4: Delete Cancelled Change Request

**Scenario**: Remove a cancelled change request from the system.

```
Delete Change Request:
  Sys ID: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7

Result: Change request permanently deleted
```

## Best Practices

### Creating Change Requests

1. **Always Provide Short Description**: Makes change requests easily identifiable
2. **Set Appropriate Priority**: Based on business impact
3. **Include Detailed Description**: Helps reviewers understand the change
4. **Assign to Correct Group**: Ensures proper routing
5. **Set Realistic Risk Level**: Aids in approval process

### Searching Change Requests

1. **Use Specific Criteria**: Narrow results for better performance
2. **Implement Pagination**: For large result sets
3. **Search by Request ID**: When you know the specific change
4. **Filter by State**: To find changes in specific workflow stages
5. **Use Assignment Group**: To find team-specific changes

### Managing Change Requests

1. **Use Get for Details**: Before making updates
2. **Verify Before Delete**: Deletion is permanent
3. **Track Sys ID**: For all operations after creation
4. **Monitor State Transitions**: Ensure proper workflow progression
5. **Check Approval Status**: Before proceeding with implementation

## Limitations

1. **No Update Operation**: Update method returns null (use catalog requests or ServiceNow UI)
2. **Search Pagination**: Maximum 1000 results per page
3. **Limited State Control**: State transitions follow ServiceNow workflow rules
4. **No Bulk Operations**: Operations are performed one at a time
5. **No Attachment Support**: Cannot manage attachments through entity

## Related Catalog Requests

- **[Escalate Issue](pages/EscalateIssue.md)** - Escalate change request to manager
- **[Check Status](pages/CheckStatus.md)** - Check current status of change request
- **[Approve Change Request](pages/ApproveChangeRequest.md)** - Approve change request

## See Also

- [Incident Entity](pages/IncidentEntity.md) - Incident management entity
- [Case Entity](pages/CaseEntity.md) - Case management entity
- [User Entity](pages/UserEntity.md) - User search and retrieval
- [Troubleshooting](pages/troubleshooting.md) - Common issues and solutions

