# Authentication

## Overview

The Krista ServiceNow Extension supports two authentication methods to connect to your ServiceNow instance:

1. **OAuth 2.0** (Recommended) - Token-based authentication for user-specific access
2. **Basic Authentication** - Username/password authentication for service accounts

This guide explains how each authentication method works, when to use them, and how tokens are managed.

## Authentication Methods Comparison

| Feature | OAuth 2.0 | Basic Authentication |
|---------|-----------|---------------------|
| **Security** | High - Token-based | Medium - Credential-based |
| **User Context** | Yes - User-specific permissions | No - Service account permissions |
| **Token Expiration** | Yes - Automatic refresh | N/A |
| **Revocation** | Easy - Revoke tokens without password change | Requires password change |
| **Setup Complexity** | Medium - Requires OAuth app creation | Low - Just username/password |
| **Best For** | User-based workflows, production | Service accounts, testing |
| **Audit Trail** | User-specific actions | Service account actions |

## OAuth 2.0 Authentication

### Overview

OAuth 2.0 provides secure, token-based authentication that allows users to authorize the Krista extension to access ServiceNow on their behalf without sharing passwords. The extension uses the **Authorization Code Grant** flow with refresh tokens for long-term access.

### OAuth Scopes

The extension requests the following OAuth scope:

| Scope | Description | Purpose |
|-------|-------------|---------|
| `useraccopunt` | User account access | Grants access to ServiceNow APIs with user's permissions |

> **📝 Note**: The scope name `useraccopunt` is ServiceNow's standard scope for user account access. This is not a typo.

### OAuth Endpoints

The extension uses the following ServiceNow OAuth endpoints:

| Endpoint | URL | Purpose |
|----------|-----|---------|
| **Authorization** | `{instance_url}/oauth_auth.do` | User authorization and consent |
| **Token** | `{instance_url}/oauth_token.do` | Token exchange and refresh |

### OAuth Flow Diagram

```mermaid
sequenceDiagram
    participant User
    participant Krista
    participant Extension
    participant ServiceNow
    participant TokenStore
    
    User->>Krista: Execute Catalog Request
    Krista->>Extension: Process Request
    Extension->>TokenStore: Check for Refresh Token
    TokenStore-->>Extension: Token Not Found
    Extension-->>Krista: MustAuthorizeException
    
    Krista->>User: Display Authorization URL
    User->>ServiceNow: Click URL & Login
    ServiceNow->>User: Show Consent Screen
    User->>ServiceNow: Grant Permission
    
    ServiceNow->>Extension: Redirect with Authorization Code
    Extension->>ServiceNow: Exchange Code for Tokens
    ServiceNow-->>Extension: Access Token + Refresh Token
    
    Extension->>TokenStore: Store Refresh Token
    Extension->>ServiceNow: API Call with Access Token
    ServiceNow-->>Extension: API Response
    Extension-->>Krista: Success
    Krista-->>User: Request Complete
```

### Authorization Code Grant Flow

#### Step 1: Authorization Request

When a user executes a catalog request and no valid token exists, the extension generates an authorization URL:

**URL Format:**
```
https://{instance_url}/oauth_auth.do?
  response_type=code&
  client_id={client_id}&
  client_secret={client_secret}&
  state={user_id}&
  redirect_uri={callback_uri}&
  access_type=offline&
  approval_prompt=force
```

**Parameters:**
- `response_type=code` - Requests authorization code
- `client_id` - OAuth application client ID
- `client_secret` - OAuth application client secret
- `state` - User identifier for security (format: `{email}#{authContextId}`)
- `redirect_uri` - Callback URL (format: `{krista_url}/extension/api/{extension_id}/servicenow/callback`)
- `access_type=offline` - Requests refresh token for offline access
- `approval_prompt=force` - Forces consent screen display

#### Step 2: User Authorization

1. User clicks the authorization URL
2. Redirected to ServiceNow login page
3. User logs in with ServiceNow credentials
4. ServiceNow displays consent screen showing requested permissions
5. User clicks "Allow" to grant access

#### Step 3: Authorization Code Exchange

After user grants permission:

1. ServiceNow redirects to callback URL with authorization code:
   ```
   {callback_uri}?code={authorization_code}&state={user_id}
   ```

2. Extension receives the callback and extracts the code

3. Extension exchanges code for tokens by calling token endpoint:
   ```
   POST https://{instance_url}/oauth_token.do
   Content-Type: application/x-www-form-urlencoded
   
   grant_type=authorization_code&
   code={authorization_code}&
   redirect_uri={callback_uri}&
   client_id={client_id}&
   client_secret={client_secret}
   ```

4. ServiceNow responds with tokens:
   ```json
   {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "refresh_token": "v1.MR3fJKe8xH2kL9mN0pQ...",
     "token_type": "Bearer",
     "expires_in": 3600,
     "scope": "useraccopunt"
   }
   ```

#### Step 4: Token Storage

The extension stores the refresh token securely:

**Token Key Format:**
```
{email}#{client_id}{client_secret}
```

**Example:**
```
john.doe@company.com#a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

The refresh token is stored in Krista's secure KeyValueStore and used for subsequent API calls.

### Token Management

#### Access Token

- **Lifetime**: Typically 1 hour (3600 seconds)
- **Usage**: Sent with every API request in Authorization header
- **Format**: `Authorization: Bearer {access_token}`
- **Refresh**: Automatically refreshed using refresh token when expired

#### Refresh Token

- **Lifetime**: Long-lived (until revoked)
- **Usage**: Used to obtain new access tokens
- **Storage**: Encrypted in Krista's KeyValueStore
- **Key**: `{email}#{client_id}{client_secret}`

#### Token Refresh Flow

```mermaid
sequenceDiagram
    participant Extension
    participant TokenStore
    participant ServiceNow
    
    Extension->>TokenStore: Get Refresh Token
    TokenStore-->>Extension: Refresh Token
    Extension->>ServiceNow: API Call with Access Token
    ServiceNow-->>Extension: 401 Unauthorized (Token Expired)
    
    Extension->>ServiceNow: POST /oauth_token.do (Refresh)
    Note over Extension,ServiceNow: grant_type=refresh_token<br/>refresh_token={token}
    ServiceNow-->>Extension: New Access Token
    
    Extension->>ServiceNow: Retry API Call
    ServiceNow-->>Extension: Success
```

### Token Expiration Scenarios

#### Scenario 1: Access Token Expired

**What Happens:**
- API call returns 401 Unauthorized
- Extension automatically uses refresh token to get new access token
- Original request is retried with new token
- User experiences no interruption

**User Action Required:** None

#### Scenario 2: Refresh Token Expired or Revoked

**What Happens:**
- Refresh token is invalid or revoked
- Extension throws `MustAuthorizeException`
- User is prompted to re-authorize

**User Action Required:** Click authorization link and grant permission again

**Common Causes:**
- User changed password in ServiceNow
- User revoked application access
- Token manually deleted from ServiceNow
- OAuth application was modified or deleted

#### Scenario 3: Email Mismatch

**What Happens:**
- User authorizes with different email than configured
- Extension detects mismatch and rejects authorization
- Error message displayed: "Unauthorised user. User email '{email}' configured in the setup does not match with authenticated user."

**User Action Required:** 
- Authorize with correct email, OR
- Update extension configuration with correct email

### Token Revocation

#### How to Revoke Access

**Method 1: From ServiceNow**
1. Log in to ServiceNow
2. Navigate to **All → System OAuth → Active Access Tokens**
3. Find tokens for the Krista application
4. Click **Revoke** on each token

**Method 2: From Krista**
1. Reconfigure the extension with new credentials
2. Old tokens are automatically invalidated

**Method 3: Change Password**
1. Change your ServiceNow password
2. All existing tokens are automatically revoked
3. Re-authorization required

#### When Tokens Are Automatically Revoked

- User password is changed in ServiceNow
- OAuth application is deleted or deactivated
- User account is deactivated
- Application access is revoked by user or admin

## Basic Authentication

### Overview

Basic Authentication uses a username and password to authenticate API requests. Credentials are sent with every request using HTTP Basic Authentication.

### How It Works

1. **Configuration**: Username and password are configured in extension settings
2. **Storage**: Credentials are encrypted and stored securely in Krista
3. **Authentication**: Each API request includes credentials in Authorization header
4. **Format**: `Authorization: Basic {base64(username:password)}`

### Basic Auth Flow

```mermaid
sequenceDiagram
    participant User
    participant Krista
    participant Extension
    participant ServiceNow
    
    User->>Krista: Execute Catalog Request
    Krista->>Extension: Process Request
    Extension->>Extension: Encode Credentials
    Note over Extension: Base64(username:password)
    Extension->>ServiceNow: API Call with Basic Auth
    Note over Extension,ServiceNow: Authorization: Basic {encoded}
    ServiceNow-->>Extension: API Response
    Extension-->>Krista: Success
    Krista-->>User: Request Complete
```

### Credential Encoding

Credentials are encoded using Base64:

**Format:**
```
Authorization: Basic {base64(username:password)}
```

**Example:**
```
Username: integration.user
Password: MySecurePassword123

Encoded: aW50ZWdyYXRpb24udXNlcjpNeVNlY3VyZVBhc3N3b3JkMTIz
Header: Authorization: Basic aW50ZWdyYXRpb24udXNlcjpNeVNlY3VyZVBhc3N3b3JkMTIz
```

### Security Considerations

#### Advantages
- Simple setup - no OAuth application required
- No token expiration - credentials work until changed
- Suitable for service accounts with fixed permissions

#### Disadvantages
- Less secure than OAuth - credentials sent with every request
- No automatic expiration - must manually rotate credentials
- Harder to revoke - requires password change
- No user-specific context - all actions appear as service account

### Best Practices for Basic Auth

1. **Use Dedicated Service Account**
   - Create a dedicated ServiceNow user for integration
   - Don't use personal accounts
   - Name clearly (e.g., `krista.integration`)

2. **Apply Least Privilege**
   - Grant only required roles
   - Avoid admin privileges unless necessary

3. **Secure Password**
   - Use strong, unique password
   - Follow organization's password policy
   - Store securely (Krista encrypts automatically)

4. **Regular Rotation**
   - Change password periodically
   - Update in Krista configuration after change

5. **Monitor Usage**
   - Review ServiceNow audit logs
   - Watch for unusual activity
   - Set up alerts for failed authentication

## Choosing Authentication Method

### Use OAuth 2.0 When:

✅ You need user-specific permissions and audit trails
✅ Multiple users will use the extension
✅ You want automatic token refresh
✅ Security is a top priority
✅ You need ability to revoke access without password changes
✅ You're deploying to production

### Use Basic Authentication When:

✅ You're using a dedicated service account
✅ You need simple setup for testing/development
✅ You have a single integration user
✅ Your organization prefers credential-based auth
✅ You don't need user-specific context

## Troubleshooting Authentication

### OAuth Issues

#### Authorization Failed

**Symptoms:**
- Authorization URL doesn't work
- Redirect fails after login
- "Authorization failed" error

**Solutions:**
1. Verify Client ID and Client Secret are correct
2. Check redirect URI matches in both Krista and ServiceNow
3. Ensure OAuth application is active in ServiceNow
4. Verify email matches ServiceNow user

#### Token Expired

**Symptoms:**
- Requests fail with "Must authorize" error
- Previously working requests now require re-authorization

**Solutions:**
1. Click authorization link to re-authorize
2. Check if password was changed in ServiceNow
3. Verify OAuth application still exists
4. Check if tokens were manually revoked

### Basic Auth Issues

#### Authentication Failed

**Symptoms:**
- "Connection test failed" error
- 401 Unauthorized responses
- "Invalid username or password" error

**Solutions:**
1. Verify username and password are correct
2. Check user account is active in ServiceNow
3. Verify user has required roles
4. Test login directly in ServiceNow web interface

#### Permission Denied

**Symptoms:**
- Authentication succeeds but operations fail
- "Insufficient permissions" errors

**Solutions:**
1. Verify user has required roles:
   - `itil`
   - `sn_change_read` and `sn_change_write`
   - `sn_customerservice_agent` (for cases)
2. Check ServiceNow ACLs for specific tables
3. Review user's role assignments in ServiceNow

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure authentication settings
- [Creating ServiceNow OAuth Application](pages/CreatingServiceNowApp.md) - Set up OAuth app
- [Troubleshooting](pages/troubleshooting.md) - Additional troubleshooting guidance

