# Customer Entity

## Overview

The **Customer** entity represents a ServiceNow customer account record. It provides read-only operations for retrieving and searching customer information in ServiceNow.

Customers represent organizations or individuals who receive services, typically used in customer service management and case management processes.

## Entity Details

| Property | Value |
|----------|-------|
| **Entity Name** | Customer |
| **Primary Key** | Sys Id |
| **ServiceNow Table** | `customer_account` |
| **Searchable** | Yes |
| **Operations** | Get, Search |

## Fields

| Field Name | Type | Required | Searchable | Description |
|------------|------|----------|------------|-------------|
| **Name** | Text | Yes | Yes | Customer name |
| **Customer Id** | Identifier | No | Yes | Customer account number |
| **Phone** | Text | No | Yes | Customer phone number |
| **City** | City | No | Yes | Customer city |
| **Zip Code** | PostalCode | No | Yes | Customer postal/zip code |
| **Sys Id** | Text | No | Yes | System ID (primary key) |

### Field Details

#### Name
- **Format**: Text string
- **Usage**: Customer's full name or company name
- **Searchable**: Yes
- **Required**: Yes

#### Customer Id
- **Format**: Identifier string
- **Usage**: Customer account number or reference ID
- **Searchable**: Yes
- **Unique**: Typically yes

#### Phone
- **Format**: Phone number string
- **Usage**: Primary contact phone number
- **Searchable**: Yes

#### City
- **Format**: City name
- **Usage**: Customer's city location
- **Searchable**: Yes

#### Zip Code
- **Format**: Postal/ZIP code
- **Usage**: Customer's postal code
- **Searchable**: Yes

#### Sys Id
- **Format**: 32-character hexadecimal string
- **Auto-generated**: Yes, by ServiceNow
- **Immutable**: Yes
- **Usage**: Primary key for all operations
- **Searchable**: Yes

## Operations

### Get

Retrieves a customer by their System ID.

**Syntax**:
```
Get Customer by Sys Id: "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
```

**Parameters**:
- **Sys Id** (required): System ID of the customer

**Returns**: Customer entity with all fields

**Example**:
```
Input:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Output:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Name: Acme Corporation
  Customer Id: CUST-12345
  Phone: +1-555-0100
  City: San Francisco
  Zip Code: 94105
```

### Search

Searches for customers based on specified criteria.

**Searchable Fields**:
- Name
- Customer Id
- Phone
- City
- Zip Code
- Sys Id

**Search Operators**:
- **Text fields**: `equals`, `contains`, `startswith`, `endswith`

**Syntax**:
```
Search Customer where:
  Name contains "Acme"
```

**Pagination**:
- **Page Index**: 0-based page number
- **Page Size**: Number of results per page (max 1000)

**Returns**: List of Customer entities matching criteria

**Examples**:

**Example 1: Search by Name**
```
Search where Name equals "Acme Corporation"
Returns: Single customer with that name
```

**Example 2: Search by Customer Id**
```
Search where Customer Id equals "CUST-12345"
Returns: Single customer with that ID
```

**Example 3: Search by City**
```
Search where City equals "San Francisco"
Returns: All customers in San Francisco
```

**Example 4: Search by Zip Code**
```
Search where Zip Code equals "94105"
Returns: All customers in that zip code
```

**Example 5: Search by Phone**
```
Search where Phone contains "555-0100"
Returns: Customers with matching phone number
```

**Example 6: Search by Name (Partial)**
```
Search where Name contains "Corp"
Page Index: 0
Page Size: 50
Returns: First 50 customers with "Corp" in name
```

## Validation Rules

### Get Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Sys Id Required** | System ID must be provided | Error thrown |
| **Customer Must Exist** | Customer with given Sys Id must exist | "Unable to retrieve customer. Please verify the System ID is correct and the customer exists in ServiceNow." |

### Search Operation

| Rule | Description | Error Message |
|------|-------------|---------------|
| **Valid Search Criteria** | At least one search condition must be provided | "Search criteria cannot be empty. Please provide at least one search condition and try again." |
| **Valid Field Names** | Field names must be searchable | Search returns empty list |

## Error Handling

### Get Errors

**Error: Customer Not Found**
```
Error: Unable to retrieve customer. Please verify the System ID is correct and the customer exists in ServiceNow.
Cause: Invalid Sys Id or customer doesn't exist
Resolution: Verify Sys Id is correct and customer exists
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
Error: Failed to search for customers. Please verify your search criteria and try again.
Cause: Invalid search criteria or ServiceNow API error
Resolution: Verify search criteria are valid and retry
```

## Usage Examples

### Example 1: Find Customer by Name

**Scenario**: Find a customer's System ID by their name for case creation.

```
Search Customer:
  Name equals "Acme Corporation"

Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Name: Acme Corporation
  Customer Id: CUST-12345
  Phone: +1-555-0100
  City: San Francisco
  Zip Code: 94105
```

**Use Case**: Use the Sys Id to create cases linked to this customer.

### Example 2: Find Customers in Region

**Scenario**: Find all customers in a specific city for regional analysis.

```
Search Customer:
  City equals "San Francisco"
  Page Index: 0
  Page Size: 100

Result: List of all customers in San Francisco
```

### Example 3: Get Customer Details

**Scenario**: Retrieve full details of a customer by their System ID.

```
Get Customer:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Name: Acme Corporation
  Customer Id: CUST-12345
  Phone: +1-555-0100
  City: San Francisco
  Zip Code: 94105
```

### Example 4: Find Customer by Account Number

**Scenario**: Look up customer using their account number.

```
Search Customer:
  Customer Id equals "CUST-12345"

Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Name: Acme Corporation
  Customer Id: CUST-12345
  (all other fields)
```

### Example 5: Workflow Integration - Create Case

**Scenario**: Find customer and create case for them.

```
Step 1: Search Customer
  Name equals "Acme Corporation"
  
Result:
  Sys Id: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6

Step 2: Create Case
  Short Description: "Billing inquiry"
  Customer: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
  Priority: "Moderate"
  Channel: "Phone"
  
Result: Case created and linked to customer
```

## Best Practices

### Searching Customers

1. **Search by Customer Id**: Most reliable for finding specific customers
2. **Search by Name**: Use exact match when possible
3. **Use Contains**: For partial matches or fuzzy searches
4. **Search by Location**: Use City or Zip Code for regional queries
5. **Implement Pagination**: For large result sets
6. **Cache Results**: Store frequently used customer sys_ids

### Using Customer System IDs

1. **Store Sys IDs**: Keep customer sys_ids for case creation
2. **Validate Before Use**: Verify customer exists before creating cases
3. **Handle Not Found**: Implement error handling for missing customers
4. **Map Account Numbers**: Create lookup tables for common customers
5. **Use in Case Creation**: Required for linking cases to customers

### Data Quality

1. **Verify Customer Data**: Ensure customer information is accurate
2. **Check for Duplicates**: Search before creating new customers
3. **Standardize Formats**: Use consistent phone and address formats
4. **Update Regularly**: Keep customer information current
5. **Validate Zip Codes**: Ensure postal codes are valid

## Limitations

1. **No Create Operation**: Cannot create customers through entity (use ServiceNow UI or API)
2. **No Update Operation**: Cannot update customer information (use ServiceNow UI or API)
3. **No Delete Operation**: Cannot delete customers through entity (use ServiceNow UI)
4. **Limited Fields**: Only 6 fields available
5. **Search Pagination**: Maximum 1000 results per page
6. **No Address Details**: Limited address information (only city and zip code)
7. **No Contact Details**: Limited to single phone number
8. **Read-Only**: Entity is primarily for lookup purposes

## Common Use Cases

### Use Case 1: Customer Lookup for Case Creation

**Scenario**: Find customer System ID for creating service cases.

**Workflow**:
1. Search Customer by name or customer ID
2. Extract Sys Id from result
3. Use Sys Id in case creation
4. Verify case linked to correct customer

### Use Case 2: Regional Customer Analysis

**Scenario**: Find all customers in a specific region.

**Workflow**:
1. Search Customer by City or Zip Code
2. Retrieve list of regional customers
3. Extract Sys Ids for reporting
4. Use in regional campaigns or analysis

### Use Case 3: Customer Validation

**Scenario**: Verify customer exists before creating cases.

**Workflow**:
1. Search Customer by name or customer ID
2. Check if results returned
3. If found, proceed with case creation
4. If not found, handle error or create customer

### Use Case 4: Account Number Lookup

**Scenario**: Find customer details using account number.

**Workflow**:
1. Search Customer by Customer Id
2. Retrieve full customer details
3. Use Sys Id for case operations
4. Display customer information to agent

## Related Entities

- **[Case Entity](pages/CaseEntity.md)** - Uses customer Sys Id for case creation
- **[User Entity](pages/UserEntity.md)** - User search and retrieval

## See Also

- [Case Entity](pages/CaseEntity.md) - Customer service case management
- [Troubleshooting](pages/troubleshooting.md) - Common issues and solutions
- [Extension Configuration](pages/ExtensionConfiguration.md) - Setup and configuration

