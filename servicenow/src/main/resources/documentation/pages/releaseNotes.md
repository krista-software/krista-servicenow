# Release Notes - Krista ServiceNow Extension

## Version 2.0.7

**Release Date**: December 2025
**Developer**: Amruta Pawar

### 📦 Version Information

| Component | Version      |
|-----------|--------------|
| **ServiceNow Extension** | 2.0.7        |
| **Krista Service APIs Java** | 1.0.118      |
| **Global Catalog Version** | GC.2025.12.1 |
| **Java Version** | 21           |

### ✨ New Features

#### Enhanced Documentation
- **Comprehensive Documentation Rewrite**: Complete rewrite of all documentation following new guidelines
- **Catalog Request Pages**: Separate detailed pages for each of the 4 catalog requests
- **Entity Documentation**: Complete documentation for all 5 entities (Change Request, Incident, Case, User, Customer)
- **Getting Started Guides**: Step-by-step guides for configuration, authentication, and OAuth setup
- **Usage Examples**: Real-world examples and use cases for every operation
- **Troubleshooting Guide**: Comprehensive troubleshooting with common issues and solutions

#### Improved Error Messages
- **User-Friendly Messages**: All error messages rewritten to be clear and actionable
- **Detailed Error Handling**: Each catalog request and entity includes error scenarios with resolutions
- **Validation Feedback**: Clear validation error messages with specific guidance

#### Documentation Structure
- **Organized Navigation**: Documentation organized by functional categories
- **Cross-References**: Extensive cross-linking between related pages
- **Visual Aids**: Renamed images following naming conventions for better organization
- **Search-Friendly**: Improved structure for better searchability

### 🐛 Bug Fixes

- **[KE-1774]**: Fixed error message when creating change request
  - **Issue**: Users encountered unclear error messages during change request creation
  - **Resolution**: Improved error handling and validation with clear, actionable error messages
  - **Impact**: Better user experience with specific guidance on resolving issues

### 🔧 Improvements

#### Code Quality
- **Error Constants**: Centralized error messages in `Errors.java` for consistency
- **Validation**: Enhanced input validation with clear error messages
- **Logging**: Improved logging for better debugging and troubleshooting

#### Documentation Quality
- **Consistency**: All documentation follows the same structure and format
- **Completeness**: Every catalog request includes all 16 required sections
- **Accuracy**: All documentation verified against source code
- **Examples**: Multiple usage examples for each operation

### 📋 Catalog Requests

This version includes 4 catalog requests:

1. **Escalate Issue** - Assign change request to assignment group manager
2. **Check Status** - Retrieve current status of change request
3. **Approve Change Request** - Approve change request as current user
4. **Assign User to Incident** - Assign incident to a specific user

### 📊 Entities

This version supports 5 entities:

1. **Change Request** - Create, Get, Search, Delete operations
2. **Incident** - Create, Get, Update, Search operations
3. **Case** - Create, Get, Update, Search operations
4. **User** - Get, Search, Delete operations
5. **Customer** - Get, Search operations

### 🔒 Security

- **OAuth 2.0 Support**: Secure token-based authentication
- **Basic Authentication**: Username/password authentication option
- **Token Management**: Automatic token refresh and secure storage
- **Scope Management**: Proper OAuth scope configuration

### 📚 Documentation Pages

#### Getting Started
- Extension Configuration
- Authentication
- Creating ServiceNow OAuth Application

#### Catalog Requests
- Escalate Issue
- Check Status
- Approve Change Request
- Assign User to Incident

#### Entities
- Change Request Entity
- Incident Entity
- Case Entity
- User Entity
- Customer Entity

#### Additional Resources
- Troubleshooting
- Release Notes (this page)

### ⚠️ Known Limitations

1. **Case and Customer Entities**: Require ServiceNow Customer Service Portal plugin
2. **Delete Operations**: Limited delete support for some entities
3. **Pagination**: Maximum 1000 results per page for search operations
4. **Token Expiration**: Access tokens expire after ~1 hour (automatic refresh supported)

### 🔄 Migration Notes

If upgrading from a previous version:

1. **Review New Documentation**: Familiarize yourself with the updated documentation structure
2. **Check Error Handling**: Update any custom error handling to work with new error messages
3. **Verify Permissions**: Ensure ServiceNow user has required permissions for all operations
4. **Test Workflows**: Test existing workflows to ensure compatibility

### 📖 Documentation

Complete documentation is available in the `servicenow/src/main/resources/documentation/` directory:

- **README.md** - Main landing page with overview and quick start
- **_sidebar.md** - Navigation structure
- **pages/** - Individual documentation pages for each feature

### 🆘 Support

For issues or questions:

1. Check the [Troubleshooting Guide](pages/troubleshooting.md)
2. Review the specific catalog request or entity documentation
3. Verify ServiceNow configuration and permissions
4. Check ServiceNow system logs for API errors
5. Contact Krista support with detailed error messages

### 🙏 Acknowledgments

- **Developer**: Aradhya Gudadhe
- **Documentation**: Comprehensive rewrite following new guidelines
- **Testing**: Verified against ServiceNow REST API v1

---

## Previous Versions

### Version 2.0.5 and Earlier

For information about previous versions, please contact Krista support or refer to the Git commit history.

---

**Last Updated**: December 2024
**Next Review**: Q1 2025