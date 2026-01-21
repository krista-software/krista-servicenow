# Krista ServiceNow Extension

## Overview

The Krista ServiceNow Extension enables seamless integration between Krista and ServiceNow, allowing you to automate IT Service Management (ITSM) operations directly from your Krista workflows. This extension provides comprehensive support for managing change requests, incidents, cases, and customer information through ServiceNow's REST API.

### What is ServiceNow?

ServiceNow is a cloud-based platform that helps businesses streamline their work tasks and automate routine processes. It provides enterprise-level IT Service Management capabilities including incident management, change management, problem management, and service catalog functionality.

## Key Features

✅ **Change Management**
- Escalate change requests to assignment group managers
- Check status of change requests
- Approve change requests
- Create and update change requests

✅ **Incident Management**
- Create and update incidents
- Assign incidents to users
- Search and retrieve incident information
- Track incident status and resolution

✅ **Case Management**
- Create and manage customer service cases
- Update case information
- Search cases by various criteria
- Track case resolution

✅ **User & Customer Management**
- Search for users in ServiceNow
- Retrieve user information
- Access customer account details
- Search customer records

✅ **Flexible Authentication**
- OAuth 2.0 authentication for secure user-based access
- Basic Authentication for service account integration
- Automatic token refresh and management

✅ **Entity Operations**
- Full CRUD operations on ServiceNow entities
- Advanced search capabilities
- Pagination support for large datasets
- Field-level validation

## Quick Start Guide

### 1. Prerequisites

Before you begin, ensure you have:
- A ServiceNow instance (developer or production)
- ServiceNow user account with appropriate permissions:
  - `itil` - For ITSM operations
  - `sn_change_read` and `sn_change_write` - For change management
  - `sn_customerservice_agent` - For case management (optional)
- Access to Krista platform
- **Customer Service Portal** plugin installed in ServiceNow (required for Case and Customer entities)

### 2. Setup Steps

1. **Create ServiceNow OAuth Application** (for OAuth authentication)
   - Follow the guide: [Creating ServiceNow OAuth Application](pages/CreatingServiceNowApp.md)
   - Obtain Client ID and Client Secret
   - Configure redirect URI

2. **Configure Extension in Krista**
   - Add the ServiceNow extension to your Krista environment
   - Configure authentication settings (OAuth or Basic Auth)
   - See: [Extension Configuration](pages/ExtensionConfiguration.md)

3. **Authenticate**
   - Complete OAuth authorization flow or provide Basic Auth credentials
   - See: [Authentication Guide](pages/Authentication.md)

4. **Start Using**
   - Use catalog requests in your Krista workflows
   - Access ServiceNow entities through the extension

## Documentation Structure

### 📚 Getting Started

- **[Extension Configuration](pages/ExtensionConfiguration.md)** - Configure the extension with your ServiceNow instance
- **[Authentication](pages/Authentication.md)** - Understand OAuth 2.0 and Basic Authentication
- **[Creating ServiceNow OAuth Application](pages/CreatingServiceNowApp.md)** - Step-by-step setup guide

### 🔄 Change Management

- **[Escalate Issue](pages/EscalateIssue.md)** - Assign change request to assignment group manager
- **[Check Status](pages/CheckStatus.md)** - Retrieve current status of change request
- **[Approve Change Request](pages/ApproveChangeRequest.md)** - Approve change request as current user

### 🎫 Incident Management

- **[Assign User to Incident](pages/AssignUserToIncident.md)** - Assign incident to a specific user

### 📋 Entities

- **[Change Request Entity](pages/ChangeRequestEntity.md)** - Create, update, search change requests
- **[Incident Entity](pages/IncidentEntity.md)** - Create, update, search incidents
- **[Case Entity](pages/CaseEntity.md)** - Create, update, search cases
- **[User Entity](pages/UserEntity.md)** - Search and retrieve user information
- **[Customer Entity](pages/CustomerEntity.md)** - Search and retrieve customer accounts

### 🛠️ Additional Resources

- **[Troubleshooting](pages/troubleshooting.md)** - Common issues and solutions
- **[Release Notes](pages/releaseNotes.md)** - Version history and changes

## Support & Resources

### Extension Information

- **Version**: 2.0.6
- **API Version**: ServiceNow REST API v1
- **Java Version**: 11
- **Framework**: Krista Extension Framework 2.0.6-rc1

### ServiceNow Resources

- [ServiceNow REST API Documentation](https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/c_RESTAPI.html)
- [ServiceNow Change Management](https://docs.servicenow.com/bundle/tokyo-it-service-management/page/product/change-management/concept/c_ITILChangeManagement.html)
- [ServiceNow OAuth Setup](https://docs.servicenow.com/bundle/tokyo-platform-administration/page/administer/security/task/t_SettingUpOAuth.html)

### Getting Help

If you encounter issues:

1. Check the **[Troubleshooting Guide](pages/troubleshooting.md)** for common problems and solutions
2. Review the specific catalog request or entity documentation
3. Verify your ServiceNow configuration and permissions
4. Check ServiceNow system logs for API errors
5. Contact Krista support with detailed error messages

## What's New in Version 2.0.6

✨ **Improved Error Messages** - All error messages are now user-friendly and actionable
📖 **Enhanced Documentation** - Comprehensive guides with examples and troubleshooting
🔒 **Better Security** - Improved OAuth token management and validation
🐛 **Bug Fixes** - Various stability and performance improvements

See [Release Notes](pages/releaseNotes.md) for complete version history.

---

**Ready to get started?** Begin with [Extension Configuration](pages/ExtensionConfiguration.md) to set up your ServiceNow connection.
