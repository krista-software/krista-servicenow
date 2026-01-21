# ServiceNow Extension - Architecture Documentation

## Table of Contents
1. [Overview](#overview)
2. [Five-Layer Architecture](#five-layer-architecture)
3. [Component Diagrams](#component-diagrams)
4. [Authentication Architecture](#authentication-architecture)
5. [Request Flow](#request-flow)
6. [Entity Operations](#entity-operations)
7. [Error Handling](#error-handling)
8. [Performance Considerations](#performance-considerations)
9. [Deployment Architecture](#deployment-architecture)

---

## Overview

The ServiceNow Extension follows a **five-layer architecture** pattern that provides clear separation of concerns, maintainability, and scalability. The extension integrates with ServiceNow's REST APIs to perform IT Service Management (ITSM) operations.

### Key Characteristics
- **Version**: 2.0.6
- **JAXRS ID**: servicenow
- **Architecture Pattern**: Layered Architecture with Dependency Injection
- **Authentication**: OAuth 2.0 and Basic Authentication
- **API Version**: ServiceNow REST API v1
- **Java Version**: 11
- **Framework**: Krista Service APIs 1.0.101

---

## Five-Layer Architecture

The extension is organized into five distinct layers, each with specific responsibilities:

```mermaid
graph TB
    subgraph "Layer 1: Extension Entry Point"
        A[ServiceNowExtension]
        A1[ServiceNowAttributes]
        A2[ServiceNowRequestAuthenticator]
    end
    
    subgraph "Layer 2: Catalog Areas"
        B1[ChangeManagementArea]
        B2[TicketManagementArea]
    end
    
    subgraph "Layer 3: Entity Stores"
        C1[ChangeRequestStore]
        C2[IncidentStore]
        C3[CaseStore]
        C4[CustomerStore]
        C5[UserStore]
        C6[RefreshTokenStore]
        C7[ServiceNowAttributeStore]
    end
    
    subgraph "Layer 4: DAO Layer"
        D1[ChangeRequestDaoImpl]
        D2[IncidentDaoImpl]
        D3[CaseDaoImpl]
        D4[CustomerDaoImpl]
        D5[UserDaoImpl]
    end
    
    subgraph "Layer 5: Authentication & HTTP"
        E1[AuthService]
        E2[AuthServiceFactory]
        E3[RestClient]
        E4[OAuthClient]
    end
    
    subgraph "External"
        F[ServiceNow REST API]
    end
    
    A --> B1
    A --> B2
    A --> A1
    A --> A2
    
    B1 --> C1
    B2 --> C2
    
    C1 --> D1
    C2 --> D2
    C3 --> D3
    C4 --> D4
    C5 --> D5
    
    D1 --> E1
    D2 --> E1
    D3 --> E1
    D4 --> E1
    D5 --> E1
    
    E1 --> E2
    E1 --> E3
    E2 --> E3
    E3 --> E4
    
    E3 --> F
    E4 --> F
    
    C6 -.Token Storage.-> E1
    C7 -.Attribute Storage.-> A
```

### Layer 1: Extension Entry Point

**Purpose**: Extension initialization, configuration management, and lifecycle handling

**Components**:
- **ServiceNowExtension**: Main extension class annotated with `@Extension`
  - Handles extension lifecycle events
  - Provides RequestAuthenticator
  - Validates connection and attributes
  - Manages custom tabs
  
- **ServiceNowAttributes**: Configuration holder
  - Stores authentication credentials
  - Manages instance URL
  - Provides callback URI generation
  
- **ServiceNowRequestAuthenticator**: Authorization handler
  - Implements RequestAuthenticator interface
  - Handles OAuth authorization flow
  - Generates authorization URLs

**Lifecycle Methods**:
- `testConnection()`: Validates connection to ServiceNow
- `validateAttributes()`: Validates configuration attributes
- `attributesUpdated()`: Handles attribute updates
- `customTabs()`: Provides custom documentation tab
- `getRequestAuthenticator()`: Returns authenticator instance

### Layer 2: Catalog Areas

**Purpose**: Business logic for catalog requests

**Components**:
- **ChangeManagementArea**: Change request operations
  - Escalate Issue
  - Check Status
  - Approve Change Request
  
- **TicketManagementArea**: Incident operations
  - Assign User to Incident

**Responsibilities**:
- Input validation and transformation
- Orchestrating entity store operations
- Business rule enforcement
- Logging and error handling

### Layer 3: Entity Stores

**Purpose**: CRUD operations and search functionality for entities

**Components**:
- **ChangeRequestStore**: Change Request entity operations
- **IncidentStore**: Incident entity operations
- **CaseStore**: Case entity operations
- **CustomerStore**: Customer entity operations
- **UserStore**: User entity operations
- **RefreshTokenStore**: OAuth token persistence
- **ServiceNowAttributeStore**: Attribute persistence for OAuth contexts

**Operations**:
- `create(Map<String, Object>)`: Create new entity
- `get(String primaryKey)`: Retrieve entity by ID
- `update(Entity)`: Update existing entity
- `delete(String primaryKey)`: Delete entity
- `search(SearchQuery, pageIndex, pageSize)`: Search entities
- `count(List<SearchCondition>)`: Count entities
- `contains(String primaryKey)`: Check entity existence

### Layer 4: DAO Layer

**Purpose**: Direct interaction with ServiceNow REST APIs

**Components**:
- **ChangeRequestDaoImpl**: Change request API operations
- **IncidentDaoImpl**: Incident API operations
- **CaseDaoImpl**: Case API operations
- **CustomerDaoImpl**: Customer API operations
- **UserDaoImpl**: User API operations

**Responsibilities**:
- HTTP request construction
- JSON payload preparation
- Response parsing
- Error handling and transformation
- Query parameter building

### Layer 5: Authentication & HTTP

**Purpose**: Authentication management and HTTP communication

**Components**:
- **AuthService**: Authentication orchestration
  - Determines auth type (OAuth/BasicAuth)
  - Retrieves and validates tokens
  - Provides RestClient instances
  
- **AuthServiceFactory**: AuthService instance creation
  - Creates AuthService with appropriate configuration
  - Manages default and custom instances
  
- **RestClient**: HTTP client wrapper
  - Supports GET, POST, PUT, PATCH, DELETE
  - Adds authentication headers
  - Handles HTTP responses
  
- **OAuthClient**: OAuth 2.0 flow implementation
  - Generates authorization URLs
  - Exchanges authorization codes for tokens
  - Handles token refresh

---

## Component Diagrams

### Dependency Injection Flow

```mermaid
graph LR
    subgraph "HK2 Container"
        A[Invoker]
        B[AuthorizationContext]
        C[RequestContext]
        D[KeyValueStore]
    end
    
    A --> E[ServiceNowExtension]
    A --> F[ServiceNowAttributes]
    B --> E
    C --> G[AuthService]
    D --> H[RefreshTokenStore]
    D --> I[ServiceNowAttributeStore]
    
    E --> J[AuthServiceFactory]
    F --> J
    H --> J
    I --> J
    C --> J
    B --> J
    
    J --> G
    G --> K[RestClient]
    
    E --> L[ChangeManagementArea]
    E --> M[TicketManagementArea]
    
    L --> N[ChangeRequestStore]
    M --> O[IncidentStore]
    
    N --> P[ChangeRequestDaoImpl]
    O --> Q[IncidentDaoImpl]
    
    P --> G
    Q --> G
```

### Extension Initialization Sequence

```mermaid
sequenceDiagram
    participant K as Krista Platform
    participant E as ServiceNowExtension
    participant A as ServiceNowAttributes
    participant F as AuthServiceFactory
    participant AS as AuthService
    participant R as RestClient
    participant S as ServiceNow API
    
    K->>E: Initialize Extension
    K->>A: Inject Attributes
    K->>F: Inject Factory
    
    K->>E: validateAttributes()
    E->>A: Validate URL
    E->>F: create(attributes)
    F->>AS: new AuthService()
    AS->>R: getRestClientAdmin()
    R->>S: GET /api/global/user_role_inheritance
    S-->>R: 200 OK
    R-->>AS: Success
    AS-->>E: Connection Valid
    E-->>K: Validation Complete
    
    K->>E: testConnection()
    E->>AS: getRestClientAdmin()
    AS->>R: Test API Call
    R->>S: GET /api/global/user_role_inheritance
    S-->>R: 200 OK
    R-->>E: Connection OK
    E-->>K: Test Passed
```

---

## Authentication Architecture

### OAuth 2.0 Flow

```mermaid
sequenceDiagram
    participant U as User
    participant K as Krista
    participant E as Extension
    participant A as AuthenticationResource
    participant O as OAuthClient
    participant S as ServiceNow
    participant T as RefreshTokenStore
    
    U->>K: Execute Request
    K->>E: Catalog Request
    E->>AS: getRestClient()
    AS->>T: get(refreshToken)
    T-->>AS: null (not authorized)
    AS-->>E: MustAuthorizeException
    E-->>K: Authorization Required
    
    K->>U: Show Authorization URL
    U->>S: Click Authorization URL
    S->>U: Login & Grant Permission
    S->>A: Redirect with code
    
    A->>O: getAccessToken(code)
    O->>S: POST /oauth_token.do
    S-->>O: {access_token, refresh_token}
    O-->>A: AccessToken
    
    A->>T: put(key, refreshToken)
    T-->>A: Stored
    A-->>U: Authorization Complete
    
    U->>K: Retry Request
    K->>E: Catalog Request
    E->>AS: getRestClient()
    AS->>T: get(refreshToken)
    T-->>AS: refreshToken
    AS->>R: new RestClient(token)
    R->>S: Validate Token
    S-->>R: Valid
    R-->>AS: RestClient
    AS-->>E: Authenticated Client
    E->>S: Execute API Call
```

### Basic Authentication Flow

```mermaid
sequenceDiagram
    participant U as User
    participant K as Krista
    participant E as Extension
    participant AS as AuthService
    participant R as RestClient
    participant S as ServiceNow
    
    U->>K: Configure Extension
    K->>E: Set Username/Password
    E->>A: Store Credentials
    
    U->>K: Execute Request
    K->>E: Catalog Request
    E->>AS: getRestClient()
    AS->>R: new RestClient(username, password)
    R->>R: Encode Base64(username:password)
    R-->>AS: RestClient
    AS-->>E: Authenticated Client
    
    E->>R: API Call
    R->>R: Add Header: Authorization: Basic <encoded>
    R->>S: HTTP Request
    S-->>R: 200 OK
    R-->>E: Response
    E-->>K: Result
    K-->>U: Success
```

### Token Management

```mermaid
graph TB
    A[Request Initiated] --> B{Auth Type?}
    B -->|OAuth| C[Get User ID]
    B -->|BasicAuth| D[Get Username/Password]
    
    C --> E[Build Token Key]
    E --> F[email#clientId+clientSecret]
    F --> G[Query RefreshTokenStore]
    
    G --> H{Token Exists?}
    H -->|No| I[Throw MustAuthorizeException]
    H -->|Yes| J[Create RestClient with Token]
    
    J --> K[Validate Token]
    K --> L{Valid?}
    L -->|No| I
    L -->|Yes| M[Return RestClient]
    
    D --> N[Create RestClient with Credentials]
    N --> M
    
    M --> O[Execute API Call]
```

---

## Request Flow

### Catalog Request Execution

```mermaid
sequenceDiagram
    participant U as User/Krista
    participant CA as CatalogArea
    participant ES as EntityStore
    participant DAO as DaoImpl
    participant AS as AuthService
    participant RC as RestClient
    participant SN as ServiceNow API
    
    U->>CA: escalateIssue(requestID)
    CA->>CA: Validate Input
    CA->>ES: Get ChangeRequest
    ES->>DAO: getChangeRequestByRequestID(id)
    
    DAO->>AS: getRestClient()
    AS->>AS: Determine Auth Type
    AS->>AS: Get/Validate Token
    AS-->>DAO: RestClient
    
    DAO->>RC: get(url, queryParams)
    RC->>RC: Build Request
    RC->>RC: Add Auth Header
    RC->>SN: GET /api/sn_chg_rest/change/normal?sysparm_query=number=CHG001
    SN-->>RC: 200 OK {result: {...}}
    RC->>RC: handleResponse()
    RC-->>DAO: JSONObject
    
    DAO->>DAO: Parse Response
    DAO-->>ES: ChangeRequest
    ES-->>CA: ChangeRequest
    
    CA->>DAO: escalateChangeRequest(sysId)
    DAO->>DAO: Get Assignment Group
    DAO->>DAO: Get Manager
    DAO->>RC: patch(url, body)
    RC->>SN: PATCH /api/sn_chg_rest/change/normal/{sysId}
    SN-->>RC: 200 OK
    RC-->>DAO: JSONObject
    DAO-->>CA: Updated ChangeRequest
    CA-->>U: ChangeRequest
```

### Entity Create Operation

```mermaid
sequenceDiagram
    participant U as User
    participant ES as EntityStore
    participant DAO as DaoImpl
    participant AS as AuthService
    participant RC as RestClient
    participant SN as ServiceNow
    
    U->>ES: create(fields)
    ES->>ES: Validate Required Fields
    ES->>ES: Transform Field Values
    
    alt Incident with Caller
        ES->>DAO: getUserID(username)
        DAO->>RC: Search User
        RC->>SN: GET /api/now/v1/table/sys_user
        SN-->>RC: User Data
        RC-->>DAO: User SysID
        DAO-->>ES: SysID
        ES->>ES: Replace username with SysID
    end
    
    ES->>DAO: createIncident(fields)
    DAO->>AS: getRestClient()
    AS-->>DAO: RestClient
    
    DAO->>DAO: prepareJSON(fields)
    DAO->>RC: post(url, jsonBody)
    RC->>RC: Add Auth Header
    RC->>SN: POST /api/now/v1/table/incident
    SN-->>RC: 201 Created {result: {...}}
    RC->>RC: handleResponse()
    RC-->>DAO: JSONObject
    
    DAO->>DAO: Parse Response
    DAO->>DAO: Extract SysID
    DAO-->>ES: Incident
    ES->>ES: Log Success
    ES-->>U: Created Incident
```

### Search Operation Flow

```mermaid
graph TB
    A[Search Request] --> B[EntityStore.search]
    B --> C{Validate Conditions}
    C -->|Empty| D[Throw IllegalArgumentException]
    C -->|Valid| E[Build Query String]
    
    E --> F[QueryBuilder.buildQuery]
    F --> G{Field Type?}
    G -->|Numeric| H[Apply Math Operators]
    G -->|String| I[Apply String Operators]
    G -->|PickOne| J[Map to ServiceNow Values]
    
    H --> K[Construct sysparm_query]
    I --> K
    J --> K
    
    K --> L[Add Pagination]
    L --> M[sysparm_limit=pageSize]
    M --> N[sysparm_offset=pageIndex]
    
    N --> O[DaoImpl.getEntities]
    O --> P[AuthService.getRestClient]
    P --> Q[RestClient.get]
    Q --> R[ServiceNow API]
    R --> S[Parse JSON Array]
    S --> T[Map to Entity Objects]
    T --> U[Return List]
```

---

## Entity Operations

### Entity Store Pattern

```mermaid
classDiagram
    class EntityStore~T~ {
        <<interface>>
        +create(Map fields) T
        +get(String primaryKey) T
        +update(T entity) T
        +delete(String primaryKey) void
        +search(SearchQuery, pageIndex, pageSize) List~T~
        +count(List~SearchCondition~) long
        +contains(String primaryKey) boolean
        +lookup(Map fields, List fieldNames) List~String~
    }
    
    class ChangeRequestStore {
        -ChangeRequestDaoImpl dao
        -UserDaoImpl userDao
        +create(Map) ChangeRequest
        +get(String) ChangeRequest
        +search(SearchQuery, long, int) List
        +delete(String) void
    }
    
    class IncidentStore {
        -IncidentDaoImpl dao
        -UserDaoImpl userDao
        +create(Map) Incident
        +get(String) Incident
        +update(Incident) Incident
        +search(SearchQuery, long, int) List
    }
    
    class CaseStore {
        -CaseDaoImpl dao
        +create(Map) Case
        +get(String) Case
        +update(Case) Case
        +search(SearchQuery, long, int) List
    }
    
    EntityStore <|.. ChangeRequestStore
    EntityStore <|.. IncidentStore
    EntityStore <|.. CaseStore
```

### DAO Implementation Pattern

```mermaid
classDiagram
    class ChangeRequestDao {
        <<interface>>
        +createChangeRequest(Map) ChangeRequest
        +moveToAssess(String) ChangeRequest
        +escalateChangeRequest(String) ChangeRequest
        +approveChangeRequest(String) ChangeRequest
        +getChangeRequestByRequestID(String) ChangeRequest
        +getChangeRequestBySysID(String) ChangeRequest
        +getRequests(HashMap) List
        +deleteChangeRequest(String) void
    }
    
    class ChangeRequestDaoImpl {
        -AuthService authService
        -UserDaoImpl userDao
        -ServiceNowAttributes attributes
        +createChangeRequest(Map) ChangeRequest
        +escalateChangeRequest(String) ChangeRequest
        -findManagerOfGroup(String) String
        -prepareJSONRequest(Map) JSONObject
    }
    
    ChangeRequestDao <|.. ChangeRequestDaoImpl
```

---

## Error Handling

### Error Propagation Flow

```mermaid
graph TB
    A[ServiceNow API Error] --> B[RestClient.handleResponse]
    B --> C{HTTP Status}
    
    C -->|200-299| D[Return JSONObject]
    C -->|400| E[Extract Error Message]
    C -->|401| F{Auth Type?}
    C -->|404| G[Extract Error Message]
    C -->|Other| H[Generic Error]
    
    E --> I[Throw IllegalArgumentException]
    F -->|OAuth| J[Return for Re-auth]
    F -->|BasicAuth| I
    G --> I
    H --> I
    
    I --> K[DaoImpl catches]
    K --> L[Log Error]
    L --> M[Wrap with Context]
    M --> N[Throw to EntityStore]
    
    N --> O[EntityStore catches]
    O --> P[Log with Entity Context]
    P --> Q[Throw to CatalogArea]
    
    Q --> R[CatalogArea catches]
    R --> S[Log with Request Context]
    S --> T[Return to User]
    
    J --> U[MustAuthorizeException]
    U --> V[ServiceNowRequestAuthenticator]
    V --> W[Generate OAuth URL]
    W --> X[Return Authorization Response]
```

### Exception Hierarchy

```mermaid
graph TB
    A[Throwable] --> B[Exception]
    B --> C[RuntimeException]
    B --> D[IOException]
    B --> E[JSONException]
    
    C --> F[IllegalArgumentException]
    C --> G[IllegalStateException]
    C --> H[MustAuthorizeException]
    
    F -.Used for.-> I[Invalid Input]
    F -.Used for.-> J[API Errors]
    F -.Used for.-> K[Validation Failures]
    
    G -.Used for.-> L[State Violations]
    G -.Used for.-> M[Missing Assignment Group]
    
    H -.Used for.-> N[Missing Token]
    H -.Used for.-> O[Invalid Token]
    H -.Used for.-> P[Email Mismatch]
    
    E -.Caught and wrapped.-> F
    D -.Propagated.-> Q[EntityStore]
```

### Error Scenarios Matrix

| Error Scenario | Layer | Exception Type | HTTP Status | User-Friendly Message | Recovery Action |
|---|---|---|---|---|---|
| Invalid URL | Extension | IllegalArgumentException | N/A | "Invalid ServiceNow Instance URL. Please provide a valid URL (e.g., https://your-instance.service-now.com)." | Fix configuration with correct URL format |
| Missing Refresh Token | AuthService | MustAuthorizeException | N/A | "Authorization required. Please authorize the extension to access ServiceNow." | Click authorization link and grant permissions |
| Invalid Token | AuthService | MustAuthorizeException | 401 | "Your authorization has expired. Please re-authorize the extension." | Re-authorize through OAuth flow |
| Email Mismatch | AuthResource | IllegalArgumentException | N/A | "Email mismatch. The authorized email doesn't match the configured email." | Update configuration with correct email |
| Missing Assignment Group | ChangeRequestStore | IllegalStateException | N/A | "Assignment Group is required when creating change request with state 'Assess'." | Specify Assignment Group in request |
| Manager Not Found | ChangeRequestDaoImpl | IllegalArgumentException | N/A | "Manager not found for Assignment Group. Please assign a Manager to the group in ServiceNow." | Assign Manager to group in ServiceNow |
| Entity Not Found | DaoImpl | IllegalArgumentException | 404 | "Unable to retrieve [entity]. Please verify the ID is correct and exists in ServiceNow." | Verify ID and check entity exists |
| Invalid Input | DaoImpl | IllegalArgumentException | 400 | "Invalid request data. Please verify all input fields are correct and properly formatted." | Review and fix input field values |
| User Not Found | UserDaoImpl | IllegalArgumentException | 404 | "Unable to find user. Please verify the username is correct and exists in ServiceNow." | Verify username and check user exists |
| Empty Search Conditions | IncidentDaoImpl | IllegalArgumentException | N/A | "Search criteria cannot be empty. Please provide at least one search condition." | Add search conditions to query |
| JSON Parse Error | DaoImpl | IllegalArgumentException | N/A | "Unable to process ServiceNow API response. Please verify your ServiceNow instance is accessible." | Check ServiceNow accessibility and retry |
| Network Timeout | RestClient | (Jersey Exception) | N/A | "Connection timeout. Please check your network connection and ServiceNow instance availability." | Verify network and retry request |
| Create Incident Failed | IncidentDaoImpl | IllegalArgumentException | N/A | "Failed to create incident. Verify all required fields are provided and caller username exists." | Check required fields and caller username |
| Update Incident Failed | IncidentDaoImpl | IllegalArgumentException | N/A | "Failed to update incident. Verify the incident exists and you have permission to modify it." | Check incident exists and permissions |
| Assign User Failed | IncidentDaoImpl | IllegalArgumentException | N/A | "Failed to assign user to incident. Verify both user and incident System IDs are correct." | Verify both System IDs exist |
| Create Change Request Failed | ChangeRequestDaoImpl | IllegalArgumentException | N/A | "Failed to create change request. Ensure Assignment Group is specified and all fields are valid." | Provide Assignment Group and validate fields |
| Escalate Change Request Failed | ChangeRequestDaoImpl | IllegalArgumentException | N/A | "Failed to escalate change request. Verify Assignment Group has a designated Manager." | Assign Manager to Assignment Group |
| Create Case Failed | CaseDaoImpl | IllegalArgumentException | N/A | "Failed to create case. Verify all required fields and ensure Customer/User System IDs exist." | Validate fields and System IDs |
| Customer Not Found | CustomerDaoImpl | IllegalArgumentException | 404 | "Unable to retrieve customer account. Verify the System ID is correct." | Check Customer System ID |

---

## Performance Considerations

### Pagination Strategy

```mermaid
graph LR
    A[Search Request] --> B{Page Index}
    B --> C[Calculate Offset]
    C --> D[offset = pageIndex * pageSize]
    
    D --> E[Build URL]
    E --> F[?sysparm_limit=pageSize]
    F --> G[&sysparm_offset=offset]
    
    G --> H{Ordering?}
    H -->|Yes| I[&ORDERBYfield^direction]
    H -->|No| J[Execute Query]
    I --> J
    
    J --> K[ServiceNow API]
    K --> L[Return Results]
    L --> M[Map to Entities]
    M --> N[Return Page]
```

### Performance Characteristics

| Operation | Default Limit | Max Recommended | Notes |
|---|---|---|---|
| Search Results | 1000 | 1000 | ServiceNow default limit |
| Page Size | Variable | 500 | Optimal for performance |
| Concurrent Requests | N/A | Depends on ServiceNow tier | Rate limited by ServiceNow |
| Token Validation | Per Request | Cached | Validated before each API call |
| Connection Timeout | Jersey Default | Configurable | No explicit timeout set |
| Read Timeout | Jersey Default | Configurable | No explicit timeout set |

### Query Optimization

```mermaid
graph TB
    A[Query Request] --> B{Field Type}
    B -->|Indexed| C[Fast Query]
    B -->|Non-Indexed| D[Slow Query]
    
    C --> E[Sys ID]
    C --> F[Request ID]
    C --> G[Incident ID]
    
    D --> H[Description]
    D --> I[Short Description]
    
    E --> J[Direct Lookup]
    F --> J
    G --> J
    
    H --> K[Full Table Scan]
    I --> K
    
    J --> L[Fast Response]
    K --> M[Slow Response]
    
    style C fill:#90EE90
    style D fill:#FFB6C1
    style L fill:#90EE90
    style M fill:#FFB6C1
```

### Limitations

1. **Rate Limiting**
   - Enforced by ServiceNow instance
   - Varies by subscription tier
   - No client-side throttling
   - Typical: 1000 requests/hour/user

2. **Pagination**
   - Maximum 1000 records per request
   - Page index is zero-based
   - Offset calculated as: `pageIndex * pageSize`

3. **Search Performance**
   - Non-indexed fields cause full table scans
   - Complex queries may timeout
   - Use indexed fields when possible

4. **Token Management**
   - Token validated on every request
   - Failed validation requires re-authorization
   - No automatic token refresh

5. **Supported Operations**
   - Only Normal Change Requests supported
   - No bulk operations
   - No file attachment support
   - No custom field mapping

6. **Network**
   - No explicit timeout configuration
   - Relies on Jersey client defaults
   - Long operations may timeout

---

## Deployment Architecture

### Runtime Components

```mermaid
graph TB
    subgraph "Krista Platform"
        A[Krista Runtime]
        B[HK2 Container]
        C[Extension Manager]
    end
    
    subgraph "ServiceNow Extension"
        D[ServiceNowExtension]
        E[Catalog Areas]
        F[Entity Stores]
        G[DAO Layer]
        H[Auth Layer]
    end
    
    subgraph "Storage"
        I[KeyValueStore]
        J[RefreshTokenStore]
        K[AttributeStore]
    end
    
    subgraph "External"
        L[ServiceNow Instance]
        M[OAuth Server]
    end
    
    A --> B
    B --> C
    C --> D
    
    D --> E
    E --> F
    F --> G
    G --> H
    
    H --> I
    I --> J
    I --> K
    
    H --> L
    H --> M
    
    style A fill:#E6F3FF
    style D fill:#FFE6E6
    style I fill:#E6FFE6
    style L fill:#FFF9E6
```

### Configuration Flow

```mermaid
sequenceDiagram
    participant Admin as Administrator
    participant UI as Krista UI
    participant Ext as Extension
    participant Val as Validator
    participant SN as ServiceNow
    
    Admin->>UI: Configure Extension
    UI->>UI: Enter Credentials
    UI->>Ext: validateAttributes(attributes)
    
    Ext->>Val: Validate URL
    Val-->>Ext: Valid
    
    Ext->>Ext: Create Temp Attributes
    Ext->>Ext: Create AuthService
    Ext->>SN: Test Connection
    SN-->>Ext: 200 OK
    
    Ext-->>UI: Validation Success
    UI->>Ext: Save Configuration
    Ext->>Ext: Store Attributes
    Ext-->>UI: Configuration Saved
    UI-->>Admin: Ready to Use
```

---

## Summary

The ServiceNow Extension implements a robust five-layer architecture that provides:

1. **Clear Separation of Concerns**: Each layer has distinct responsibilities
2. **Dependency Injection**: Managed by HK2 container for loose coupling
3. **Flexible Authentication**: Supports both OAuth 2.0 and Basic Auth
4. **Comprehensive Error Handling**: Multi-layer error propagation with context
5. **Performance Optimization**: Pagination, query building, and caching
6. **Extensibility**: Easy to add new entities and catalog requests

### Key Design Patterns

- **Layered Architecture**: Five distinct layers
- **Factory Pattern**: AuthServiceFactory for creating auth services
- **Store Pattern**: Entity stores for CRUD operations
- **DAO Pattern**: Data access objects for API interaction
- **Dependency Injection**: HK2 for component wiring
- **Strategy Pattern**: Different auth strategies (OAuth/BasicAuth)

### Technology Stack

- **Java 11**: Core language
- **Krista APIs 1.0.101**: Extension framework
- **Jersey Client 2.35**: HTTP client
- **HK2 2.6.1**: Dependency injection
- **ScribeJava 8.3.1**: OAuth implementation
- **Log4j 2.20.0**: Logging
- **Commons Validator 1.7**: URL validation

---

## Troubleshooting

### Common Error Patterns

#### Authentication Errors

**Pattern:** MustAuthorizeException thrown from AuthService

**Root Causes:**
1. Refresh token not found in RefreshTokenStore
2. Token expired or invalid
3. Email mismatch between configuration and authorization

**Resolution Flow:**
```
1. Check if token exists in RefreshTokenStore
2. If missing → Trigger OAuth authorization flow
3. If exists but invalid → Clear token and re-authorize
4. If email mismatch → Update configuration
```

#### Validation Errors

**Pattern:** IllegalArgumentException with validation message

**Root Causes:**
1. Required fields missing (e.g., Assignment Group)
2. Invalid field values (e.g., wrong State for new incident)
3. Referenced entities don't exist (e.g., invalid username)

**Resolution Flow:**
```
1. Parse error message for specific field
2. Validate field value against ServiceNow constraints
3. For references, verify entity exists using search
4. Correct input and retry
```

#### API Response Errors

**Pattern:** IllegalArgumentException from RestClient.handleResponse()

**Root Causes:**
1. HTTP 400: Invalid request data
2. HTTP 401: Authentication failure
3. HTTP 404: Entity not found
4. JSON parsing failure

**Resolution Flow:**
```
1. Check HTTP status code
2. Extract ServiceNow error message from response
3. For 401 with OAuth → Return for re-auth
4. For 401 with BasicAuth → Throw error
5. For 400/404 → Extract and throw ServiceNow message
6. For JSON errors → Throw parsing error
```

### Debugging Tips

1. **Enable Detailed Logging:**
   - Check Log4j configuration
   - Review logs at INFO and DEBUG levels
   - Look for error stack traces

2. **Verify ServiceNow Configuration:**
   - Test API endpoints directly using Postman
   - Verify user permissions in ServiceNow
   - Check plugin installations

3. **Validate Input Data:**
   - Use ServiceNow UI to verify entity IDs
   - Test field values in ServiceNow first
   - Ensure referenced entities exist

4. **Check Network Connectivity:**
   - Verify ServiceNow instance is accessible
   - Check for firewall or proxy issues
   - Test connection using Test Connection button

5. **Review Token Management:**
   - Check RefreshTokenStore for token presence
   - Verify token key format: `email#clientId+clientSecret`
   - Clear and re-authorize if token issues persist

### Error Message Improvements

All error messages in the extension follow these principles:

1. **User-Friendly:** Clear, non-technical language
2. **Actionable:** Specific steps to resolve the issue
3. **Contextual:** Include relevant IDs or field names
4. **Helpful:** Suggest next steps or alternatives

**Example Transformation:**

❌ **Old:** "Error occurred while fetching Incident for the given Sys Id."

✅ **New:** "Unable to retrieve incident. Please verify the System ID is correct and the incident exists in ServiceNow. If the problem persists, check your connection and permissions."

### Best Practices for Error Handling

1. **Catch Specific Exceptions:** Handle JSONException, IllegalArgumentException separately
2. **Add Context:** Wrap exceptions with operation context
3. **Log Before Throwing:** Log errors with full context before propagating
4. **Preserve Cause:** Always include original exception as cause
5. **Fail Fast:** Validate inputs early before making API calls

---

## Additional Resources

### Documentation

- **User Documentation:** `servicenow/src/main/resources/documentation/pages/`
  - Overview.md - Extension overview and quick start
  - serviceNow.md - ServiceNow setup instructions
  - configureExtension.md - Extension configuration guide
  - requests.md - Complete API reference
  - troubleshooting.md - Detailed troubleshooting guide
  - releaseNotes.md - Version history

- **Architecture Documentation:** `servicenow/docs/Architecture.md` (this document)

### ServiceNow Resources

- [ServiceNow REST API Documentation](https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/c_RESTAPI.html)
- [ServiceNow Change Management](https://docs.servicenow.com/bundle/tokyo-it-service-management/page/product/change-management/concept/c_ITILChangeManagement.html)
- [ServiceNow OAuth Setup](https://docs.servicenow.com/bundle/tokyo-platform-administration/page/administer/security/task/t_SettingUpOAuth.html)

### Support

For issues or questions:
1. Review the troubleshooting guide
2. Check ServiceNow system logs
3. Verify extension configuration
4. Contact support with complete error messages and context

---

**Document Version**: 1.0
**Last Updated**: December 2025
**Extension Version**: 2.0.6

