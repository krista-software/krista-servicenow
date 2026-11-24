/*
 * Servicenow Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.it.case_management.servicenow;

import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.executor.Invoker;
import app.krista.extension.impl.anno.*;
import app.krista.extensions.it.case_management.servicenow.authentication.RestClient;
import app.krista.extensions.it.case_management.servicenow.authentication.ServiceNowRequestAuthenticator;
import app.krista.extensions.it.case_management.servicenow.catalog.store.ServiceNowAttributeStore;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthServiceFactory;
import app.krista.ksdk.context.AuthorizationContext;
import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * ServiceNow Extension for Krista Platform.
 * <p>
 * This extension provides seamless integration between Krista and ServiceNow, enabling automation
 * of IT Service Management (ITSM) operations including change management, incident management,
 * case management, and customer service operations.
 * </p>
 * <p>
 * <b>Supported Authentication Methods:</b>
 * <ul>
 *   <li>OAuth 2.0 - Secure token-based authentication with automatic token refresh</li>
 *   <li>Basic Authentication - Username/password authentication for service accounts</li>
 * </ul>
 * </p>
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *   <li>Change Management - Create, update, search, and approve change requests</li>
 *   <li>Incident Management - Create, update, search, and assign incidents</li>
 *   <li>Case Management - Create, update, and search customer service cases</li>
 *   <li>User & Customer Management - Search and retrieve user and customer information</li>
 * </ul>
 * </p>
 *
 * @version 2.0.6
 * @since 1.0.0
 */
@Extension(name = "ServiceNow", jaxrsId = ServiceNowConstants.JAXRS_ID, version = "2.0.7")

@Field.PickOne(name = ServiceNowConstants.AUTH_TYPE, values = {"Oauth", "BasicAuth"})
@Field.Text(name = ServiceNowConstants.EMAIL)
@Field.Text(value = ServiceNowConstants.CLIENT_ID, required = false)
@Field.Text(value = ServiceNowConstants.CLIENT_SECRET, isSecured = true, required = false)
@Field.Text(value = ServiceNowConstants.INSTANCE_URL)
@Field.Text(value = ServiceNowConstants.USERNAME, required = false)
@Field.Text(value = ServiceNowConstants.PASSWORD, isSecured = true, required = false)
@Java(version = Java.Version.JAVA_21)
@StaticResource(path = "docs", file = "documentation")
public final class ServiceNowExtension {

    private final Invoker invoker;
    private final ServiceNowAttributes serviceNowAttributes;
    private final ServiceNowAttributeStore serviceNowAttributeStore;
    private final AuthServiceFactory clientProviderFactory;


    @Inject
    public ServiceNowExtension(@Named("self") Invoker invoker, AuthorizationContext authorizationContext, ServiceNowAttributes serviceNowAttributes, ServiceNowAttributeStore serviceNowAttributeStore, AuthServiceFactory clientProviderFactory) {
        this.invoker = invoker;
        this.serviceNowAttributes = serviceNowAttributes;
        this.serviceNowAttributeStore = serviceNowAttributeStore;
        this.clientProviderFactory = clientProviderFactory;
    }

    /**
     * Provides the request authenticator for ServiceNow API calls.
     * <p>
     * This method returns an authenticator that handles OAuth 2.0 or Basic Authentication
     * based on the configured authentication type.
     * </p>
     *
     * @return the {@link RequestAuthenticator} instance for ServiceNow authentication
     */
    @InvokerRequest(InvokerRequest.Type.AUTHENTICATOR)
    public RequestAuthenticator getRequestAuthenticator() {
        return new ServiceNowRequestAuthenticator(serviceNowAttributeStore, serviceNowAttributes);
    }

    /**
     * Tests the connection to ServiceNow instance.
     * <p>
     * This method validates that the extension can successfully connect to the ServiceNow
     * instance using the configured credentials. It performs a test API call to verify
     * connectivity and authentication.
     * </p>
     *
     * @throws IllegalArgumentException if the connection test fails
     */
    @InvokerRequest(InvokerRequest.Type.TEST_CONNECTION)
    public void testConnection() {
        validateAndTestConnectionOfOauth(serviceNowAttributes);
    }

    /**
     * Validates the extension configuration attributes.
     * <p>
     * This method validates all required configuration parameters including:
     * <ul>
     *   <li>Instance URL format and validity</li>
     *   <li>Authentication credentials (OAuth or Basic Auth)</li>
     *   <li>Connection to ServiceNow instance</li>
     * </ul>
     * </p>
     *
     * @param connectionAttributes the map of configuration attributes to validate
     * @throws IllegalArgumentException if any attribute is invalid or connection fails
     */
    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateAttributes(Map<String, Object> connectionAttributes) {
        ServiceNowAttributes tempServiceNowAttributes = ServiceNowAttributes.create(invoker,
                connectionAttributes);
        UrlValidator defaultValidator = new UrlValidator();
        if (!defaultValidator.isValid(tempServiceNowAttributes.getInstanceURL())) {
            throw new IllegalArgumentException(
                    "Invalid ServiceNow Instance URL: '" + tempServiceNowAttributes.getInstanceURL() +
                    "'. Please provide a valid URL (e.g., https://your-instance.service-now.com).");
        }
        if (tempServiceNowAttributes.getAuthType().equals(ServiceNowConstants.OAUTH)) {
            validateAndTestConnectionOfOauth(tempServiceNowAttributes);
        } else {
            validateAndTestConnectionOfBasicAuth(tempServiceNowAttributes);
        }
    }

    /**
     * Handles updates to extension configuration attributes.
     * <p>
     * This method is called when the extension configuration is updated in Krista.
     * It updates the internal ServiceNow attributes with the new values.
     * </p>
     *
     * @param oldAttributes the previous configuration attributes
     * @param newAttributes the new configuration attributes to apply
     */
    @InvokerRequest(InvokerRequest.Type.INVOKER_UPDATED)
    public void attributesUpdated(Map<String, Object> oldAttributes, Map<String, Object> newAttributes) {
        serviceNowAttributes.update(newAttributes);
    }

    /**
     * Provides custom tabs for the extension UI.
     * <p>
     * This method returns a map of custom tab names and their corresponding paths.
     * Currently provides a "Documentation" tab that displays the extension documentation.
     * </p>
     *
     * @return a map containing custom tab names and their paths
     */
    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> customTabs() {
        return Map.of("Documentation", "static/docs");
    }

    private void validateAndTestConnectionOfBasicAuth(ServiceNowAttributes serviceNowAttributes) {

        testApiCall(clientProviderFactory.create(serviceNowAttributes).getRestClientAdmin());
    }

    private void testApiCall(RestClient restClient) {
        String url = "/api/global/user_role_inheritance";
        restClient.get(url, new HashMap<>());
    }

    private void validateAndTestConnectionOfOauth(ServiceNowAttributes serviceNowAttributes) {
        testApiCall(clientProviderFactory.create(serviceNowAttributes).getRestClientAdmin());
    }

}
