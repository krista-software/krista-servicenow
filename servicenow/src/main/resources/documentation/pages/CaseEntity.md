# Case Entity

## Overview

The **Case** entity represents a ServiceNow case record. It provides full CRUD (Create, Read, Update, Delete) operations and advanced search capabilities for managing customer service cases in ServiceNow.

Cases are used to track customer inquiries, requests, and issues through various communication channels following customer service management processes.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Case |
| **Primary Key** | Sys Id |
| **ServiceNow Table** | `sn_customerservice_case` |
| **Searchable** | Yes |
| **Operations** | Create, Get, Update, Search |

## Fields

| Field Name | Type | Required | Searchable | Description |
|------------|------|----------|------------|-------------|
| **Case Id** | Identifier | No | Yes | Case number (e.g., CS0001001) |
| **Channel** | Pick One | No | Yes | Communication channel |
| **Priority** | Pick One | No | Yes | Priority level: Critical, High, Moderate, Low |
| **Customer** | Identifier | No | No | Customer System ID |
| **Assigned To Sys Id** | Identifier | No | No | User System ID assigned to the case |
| **Short Description** | Paragraph | No | Yes | Brief description of the case |
| **Sys Id** | Text | No | No | System ID (primary key) |
| **State** | Pick One | No | Yes | Current state: New, Open, Awaiting Info, Resolved, Closed |

### Field Details

#### Case Id
- **Format**: CS followed by 7 digits (e.g., `CS0001001`)
- **Auto-generated**: Yes, by ServiceNow upon creation
- **Unique**: Yes
- **Usage**: Human-readable identifier for the case

#### Channel
- **Values**: `Web`, `Phone`, `Email`, `Chat`, `Social`, `Community`, `Alert`, `Virtual Agent`
- **Default**: Based on how case was created
- **Usage**: Tracks how customer contacted support

#### Priority
- **Values**: `Critical`, `High`, `Moderate`, `Low`
- **Default**: Based on configuration
- **Usage**: Determines processing priority

#### Customer
- **Format**: System ID of customer account
- **Usage**: Links case to customer record
- **Related**: See [Customer Entity](pages/CustomerEntity.md)

#### Assigned To Sys Id
- **Format**: System ID of user
- **Usage**: Identifies who is responsible for the case
- **Related**: See [User Entity](pages/UserEntity.md)

#### State
- **Values**: `New`, `Open`, `Awaiting Info`, `Resolved`, `Closed`
- **Default**: `New`
- **Workflow**: Follows ServiceNow case management workflow
- **Transitions**: Controlled by business rules

#### Sys Id
- **Format**: 32-character hexadecimal string
- **Auto-generated**: Yes, by ServiceNow
- **Immutable**: Yes
- **Usage**: Primary key for all operations

## Operations

### Create

Creates a new case in ServiceNow.

**Syntax**:
```
Create Case with:
  Short Description: "Unable to access account"
  Channel: "Email"
  Priority: "High"
  Customer: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
  State: "New"
```

**Required Fields**:
- Short Description (recommended)
- Customer (recommended)

**Returns**: Created Case entity with all fields populated

**Example**:
```
Input:
  Short Description: "Password reset request"
  Channel: "Phone"
  Priority: "Moderate"
  Customer: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  State: "New"

Output:
  Case Id: CS0001050
  Sys Id: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
  State: New
  Priority: Moderate
  Channel: Phone
```

### Get

Retrieves a case by its System ID.

**Syntax**:
```
Get Case by Sys Id: "c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8"
```

**Parameters**:
- **Sys Id** (required): System ID of the case

**Returns**: Case entity with all fields

**Example**:
```
Input:
  Sys Id: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8

Output:
  Case Id: CS0001050
  Short Description: Password reset request
  State: Open
  Priority: Moderate
  Channel: Phone
  Customer: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Assigned To Sys Id: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
```

### Update

Updates an existing case in ServiceNow.

**Syntax**:
```
Update Case:
  Sys Id: "c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8"
  State: "Resolved"
  Assigned To Sys Id: "b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7"
```

**Required Fields**:
- Sys Id (to identify the case to update)

**Updatable Fields**:
- All fields except Sys Id and Case Id

**Returns**: Updated Case entity with all fields

**Example**:
```
Input:
  Sys Id: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
  State: "Resolved"
  Short Description: "Password reset completed"

Output:
  Case Id: CS0001050
  State: Resolved
  Short Description: Password reset completed
  (all other fields unchanged)
```

### Search

Searches for cases based on specified criteria.

**Searchable Fields**:
- Case Id
- Channel
- Priority
- Short Description
- State

**Search Operators**:
- **Text fields**: `equals`, `contains`, `startswith`, `endswith`
- **Pick One fields**: `equals`

**Syntax**:
```
Search Case where:
  Priority equals "High"
  State equals "Open"
```

**Pagination**:
- **Page Index**: 0-based page number
- **Page Size**: Number of results per page (max 1000)

**Returns**: List of Case entities matching criteria

**Examples**:

**Example 1: Search by Case Id**
```
Search where Case Id equals "CS0001001"
Returns: Single case with that ID
```

**Example 2: Search by Priority**
```
Search where Priority equals "Critical"
Returns: All critical priority cases
```

**Example 3: Search by Channel**
```
Search where Channel equals "Email"
Page Index: 0
Page Size: 50
Returns: First 50 cases from email channel
```

**Example 4: Search by State**
```
Search where State equals "Open"
Returns: All open cases
```

## Validation Rules

### Create Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Valid Field Values** | All field values must be valid | "Unable to create case. Please verify all field values are valid and properly formatted." |
| **Valid Customer** | If Customer provided, must be valid customer sys_id | Error thrown |

### Get Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys Id Required** | System ID must be provided | Error thrown |
| **Case Must Exist** | Case with given Sys Id must exist | "Unable to retrieve case. Please verify the System ID is correct and the case exists in ServiceNow." |

### Update Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys Id Required** | System ID must be provided | Error thrown |
| **Case Must Exist** | Case must exist to be updated | "Unable to update case. Please verify the System ID is correct and the case exists in ServiceNow." |
| **Valid Field Values** | All field values must be valid | Error thrown |

### Search Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Valid Search Criteria** | Search conditions must be valid | "Failed to search for cases. Please verify your search criteria and try again." |
| **Valid Field Names** | Field names must be searchable | Search returns empty list |

## Error Handling

### Create Errors

**Error: Creation Failed**
```
Error: Unable to create case. Please verify all field values are valid and properly formatted.
Cause: Invalid field values or ServiceNow API error
Resolution: Verify all field values are valid and retry
```

### Get Errors

**Error: Case Not Found**
```
Error: Unable to retrieve case. Please verify the System ID is correct and the case exists in ServiceNow.
Cause: Invalid Sys Id or case doesn't exist
Resolution: Verify Sys Id is correct and case exists
```

### Update Errors

**Error: Update Failed**
```
Error: Unable to update case. Please verify the System ID is correct and the case exists in ServiceNow.
Cause: Invalid Sys Id, case doesn't exist, or invalid field values
Resolution: Verify Sys Id and field values are correct, then retry
```

### Search Errors

**Error: Search Failed**
```
Error: Failed to search for cases. Please verify your search criteria and try again.
Cause: Invalid search criteria or ServiceNow API error
Resolution: Verify search criteria are valid and retry
```

## Usage Examples

### Example 1: Create Customer Service Case

**Scenario**: Create a new case for a customer inquiry received via email.

```
Create Case:
  Short Description: "Question about billing charges"
  Channel: "Email"
  Priority: "Moderate"
  Customer: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  State: "New"

Result:
  Case Id: CS0001055
  Sys Id: d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9
  State: New
  Priority: Moderate
  Channel: Email
```

### Example 2: Assign Case to Agent

**Scenario**: Assign an open case to a customer service agent.

```
Update Case:
  Sys Id: d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9
  State: "Open"
  Assigned To Sys Id: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7

Result:
  Case Id: CS0001055
  State: Open
  Assigned To Sys Id: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
```

### Example 3: Search for High Priority Cases

**Scenario**: Find all high-priority cases that need attention.

```
Search Case:
  Priority equals "High"
  State equals "Open"
  Page Index: 0
  Page Size: 100

Result: List of high-priority open cases
```

### Example 4: Resolve Case

**Scenario**: Mark case as resolved after addressing customer inquiry.

```
Update Case:
  Sys Id: d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9
  State: "Resolved"
  Short Description: "Billing charges explained. Customer satisfied."

Result:
  Case Id: CS0001055
  State: Resolved
  Short Description: Billing charges explained. Customer satisfied.
```

## Best Practices

### Creating Cases

1. **Provide Clear Short Description**: Makes cases easily identifiable
2. **Set Appropriate Channel**: Tracks communication method
3. **Link to Customer**: Enables customer history tracking
4. **Set Accurate Priority**: Ensures proper handling
5. **Use Appropriate State**: Start with "New" unless workflow requires otherwise

### Updating Cases

1. **Update State Appropriately**: Follow case workflow
2. **Assign to Correct Agent**: Ensure accountability
3. **Update Description**: Keep case history complete
4. **Use Get Before Update**: Verify current state before updating
5. **Track Resolution**: Document how case was resolved

### Searching Cases

1. **Use Specific Criteria**: Narrow results for better performance
2. **Implement Pagination**: For large result sets
3. **Search by Case Id**: When you know the specific case
4. **Filter by State**: To find cases in specific workflow stages
5. **Filter by Channel**: To analyze channel-specific cases

## Limitations

1. **No Delete Operation**: Delete method is not implemented (use ServiceNow UI)
2. **Search Pagination**: Maximum 1000 results per page
3. **Limited Search Fields**: Only 5 fields are searchable
4. **No Bulk Operations**: Operations are performed one at a time
5. **No Attachment Support**: Cannot manage attachments through entity
6. **Requires System IDs**: Customer and Assigned To require sys_id values

## Related Entities

- **[Customer Entity](pages/CustomerEntity.md)** - Customer account information
- **[User Entity](pages/UserEntity.md)** - User search and retrieval for assignment

## See Also

- [Change Request Entity](pages/ChangeRequestEntity.md) - Change request management
- [Incident Entity](pages/IncidentEntity.md) - Incident management
- [Troubleshooting](pages/troubleshooting.md) - Common issues and solutions

