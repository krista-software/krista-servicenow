# User Entity

## Overview

The **User** entity represents a ServiceNow user record. It provides read-only operations for retrieving and searching user information in ServiceNow.

Users represent individuals who have access to the ServiceNow system, including employees, contractors, customers, and administrators.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | User |
| **Primary Key** | Sys Id |
| **ServiceNow Table** | `sys_user` |
| **Searchable** | Yes |
| **Operations** | Get, Search, Delete |

## Fields

| Field Name | Type | Required | Searchable | Description |
|------------|------|----------|------------|-------------|
| **Sys Id** | Text | Yes | No | System ID (primary key) |
| **First Name** | Text | Yes | Yes | User's first name |
| **Last Name** | Text | Yes | Yes | User's last name |
| **Email** | Email | Yes | Yes | User's email address |

### Field Details

#### Sys Id
- **Format**: 32-character hexadecimal string
- **Auto-generated**: Yes, by ServiceNow
- **Immutable**: Yes
- **Usage**: Primary key for all operations, used in assignments

#### First Name
- **Format**: Text string
- **Usage**: User's given name
- **Searchable**: Yes

#### Last Name
- **Format**: Text string
- **Usage**: User's family name
- **Searchable**: Yes

#### Email
- **Format**: Valid email address
- **Unique**: Yes (typically)
- **Usage**: Primary contact method, authentication identifier
- **Searchable**: Yes

## Operations

### Get

Retrieves a user by their System ID.

**Syntax**:
```
Get User by Sys Id: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

**Parameters**:
- **Sys Id** (required): System ID of the user

**Returns**: User entity with all fields

**Example**:
```
Input:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Output:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  First Name: John
  Last Name: Technician
  Email: john.technician@company.com
```

### Search

Searches for users based on specified criteria.

**Searchable Fields**:
- First Name
- Last Name
- Email

**Search Operators**:
- **Text fields**: `equals`, `contains`, `startswith`, `endswith`

**Syntax**:
```
Search User where:
  Email equals "john.technician@company.com"
```

**Pagination**:
- **Page Index**: 0-based page number
- **Page Size**: Number of results per page (max 1000)

**Returns**: List of User entities matching criteria

**Examples**:

**Example 1: Search by Email**
```
Search where Email equals "john.technician@company.com"
Returns: Single user with that email
```

**Example 2: Search by Last Name**
```
Search where Last Name equals "Smith"
Returns: All users with last name Smith
```

**Example 3: Search by First Name (Partial)**
```
Search where First Name contains "John"
Returns: All users with "John" in their first name
```

**Example 4: Search by Email Domain**
```
Search where Email contains "@company.com"
Returns: All users with company.com email addresses
```

### Delete

Deletes a user by their System ID.

**Syntax**:
```
Delete User by Sys Id: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

**Parameters**:
- **Sys Id** (required): System ID of the user to delete

**Returns**: void (no return value)

**Warning**: This operation is permanent and cannot be undone. Use with extreme caution.

**Example**:
```
Input:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Result: User is permanently deleted from ServiceNow
```

## Validation Rules

### Get Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys Id Required** | System ID must be provided | Error thrown |
| **User Must Exist** | User with given Sys Id must exist | "Unable to retrieve user. Please verify the System ID is correct and the user exists in ServiceNow." |

### Search Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Valid Search Criteria** | At least one search condition must be provided | "Search criteria cannot be empty. Please provide at least one search condition and try again." |
| **Valid Field Names** | Field names must be searchable | Search returns empty list |

### Delete Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys Id Required** | System ID must be provided | Error thrown |
| **User Must Exist** | User must exist to be deleted | Error thrown |

## Error Handling

### Get Errors

**Error: User Not Found**
```
Error: Unable to retrieve user. Please verify the System ID is correct and the user exists in ServiceNow.
Cause: Invalid Sys Id or user doesn't exist
Resolution: Verify Sys Id is correct and user exists
```

### Search Errors

**Error: Empty Search Criteria**
```
Error: Search criteria cannot be empty. Please provide at least one search condition and try again.
Cause: No search conditions provided
Resolution: Provide at least one search condition
```

**Error: Search Failed**
```
Error: Failed to search for users. Please verify your search criteria and try again.
Cause: Invalid search criteria or ServiceNow API error
Resolution: Verify search criteria are valid and retry
```

### Delete Errors

**Error: Delete Failed**
```
Error: Unable to delete user
Cause: Invalid Sys Id or ServiceNow API error
Resolution: Verify Sys Id is correct and retry
```

## Usage Examples

### Example 1: Find User by Email

**Scenario**: Find a user's System ID by their email address for assignment purposes.

```
Search User:
  Email equals "john.technician@company.com"

Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  First Name: John
  Last Name: Technician
  Email: john.technician@company.com
```

**Use Case**: Use the Sys Id to assign incidents or change requests.

### Example 2: Find All Users in Department

**Scenario**: Find all users with a specific last name (e.g., team members).

```
Search User:
  Last Name equals "Operations"
  Page Index: 0
  Page Size: 100

Result: List of all users with last name "Operations"
```

### Example 3: Get User Details

**Scenario**: Retrieve full details of a user by their System ID.

```
Get User:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  First Name: John
  Last Name: Technician
  Email: john.technician@company.com
```

### Example 4: Find Users by Email Domain

**Scenario**: Find all users from a specific company domain.

```
Search User:
  Email contains "@contractor.com"

Result: List of all contractor users
```

### Example 5: Workflow Integration - Assign Incident

**Scenario**: Find user and assign incident to them.

```
Step 1: Search User
  Email equals "senior.tech@company.com"
  
Result:
  Sys Id: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7

Step 2: Assign User to Incident
  Sys ID User: b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7
  Sys ID Incident: c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
  
Result: Incident assigned successfully
```

## Best Practices

### Searching Users

1. **Search by Email**: Most reliable for finding specific users
2. **Use Exact Matches**: When you know the exact value
3. **Use Contains**: For partial matches or fuzzy searches
4. **Implement Pagination**: For large result sets
5. **Cache Results**: Store frequently used user sys_ids

### Using User System IDs

1. **Store Sys IDs**: Keep user sys_ids for assignments
2. **Validate Before Use**: Verify user exists before assignment
3. **Handle Not Found**: Implement error handling for missing users
4. **Use in Assignments**: Required for Assign User to Incident
5. **Map Email to Sys ID**: Create lookup tables for common users

### Security Considerations

1. **Limit Delete Operations**: Only delete users when absolutely necessary
2. **Verify Before Delete**: Confirm user should be deleted
3. **Check Dependencies**: Ensure user is not assigned to active records
4. **Audit Deletions**: Log all user deletion operations
5. **Use Deactivation**: Consider deactivating instead of deleting

## Limitations

1. **No Create Operation**: Cannot create users through entity (use ServiceNow UI or API)
2. **No Update Operation**: Cannot update user information (use ServiceNow UI or API)
3. **Limited Fields**: Only 4 fields available (sys_id, first name, last name, email)
4. **Search Pagination**: Maximum 1000 results per page
5. **No Role Information**: Cannot retrieve user roles or permissions
6. **No Group Information**: Cannot retrieve user group memberships
7. **Delete Caution**: Delete operation is permanent

## Common Use Cases

### Use Case 1: User Lookup for Assignment

**Scenario**: Find user System ID for assigning work items.

**Workflow**:
1. Search User by email or name
2. Extract Sys Id from result
3. Use Sys Id in assignment operations
4. Verify assignment succeeded

### Use Case 2: Team Member Discovery

**Scenario**: Find all members of a team by last name or email domain.

**Workflow**:
1. Search User by Last Name or Email contains
2. Retrieve list of team members
3. Extract Sys Ids for bulk operations
4. Use in assignment or notification workflows

### Use Case 3: User Validation

**Scenario**: Verify user exists before performing operations.

**Workflow**:
1. Search User by email
2. Check if results returned
3. If found, proceed with operation
4. If not found, handle error appropriately

### Use Case 4: Email-to-SysID Mapping

**Scenario**: Build a mapping of email addresses to System IDs for quick lookups.

**Workflow**:
1. Search all users (paginated)
2. Build map: email → sys_id
3. Cache map for quick lookups
4. Refresh periodically

## Related Catalog Requests

- **[Assign User to Incident](pages/AssignUserToIncident.md)** - Requires user Sys Id
- **[Escalate Issue](pages/EscalateIssue.md)** - May involve user lookup

## Related Entities

- **[Incident Entity](pages/IncidentEntity.md)** - Uses user Sys Id for assignments
- **[Change Request Entity](pages/ChangeRequestEntity.md)** - Uses user Sys Id for assignments
- **[Case Entity](pages/CaseEntity.md)** - Uses user Sys Id for assignments

## See Also

- [Customer Entity](pages/CustomerEntity.md) - Customer account information
- [Troubleshooting](pages/troubleshooting.md) - Common issues and solutions
- [Authentication](pages/Authentication.md) - User authentication and permissions

