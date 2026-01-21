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

import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ServiceNowAttributes}.
 * <p>
 * Tests attribute management, OAuth configuration, and callback URI generation.
 * </p>
 */
class ServiceNowAttributesTest {

    @Test
    void testConstructor_WithAllParameters_CreatesAttributes() {
        // Arrange
        String routingUrl = "https://example.com";
        String clientId = "client123";
        String clientSecret = "secret456";
        String email = "user@example.com";
        String instanceUrl = "https://instance.service-now.com";
        String authType = "oauth";
        String username = "admin";
        String password = "pass123";

        // Act
        ServiceNowAttributes attributes = new ServiceNowAttributes(routingUrl, clientId,
            clientSecret, email, instanceUrl, authType, username, password);

        // Assert
        assertNotNull(attributes);
        assertEquals(clientId, attributes.getClientID());
        assertEquals(clientSecret, attributes.getClientSecret());
        assertEquals(email, attributes.getEmail());
        assertEquals(instanceUrl, attributes.getInstanceURL());
        assertEquals(authType, attributes.getAuthType());
        assertEquals(username, attributes.getUsername());
        assertEquals(password, attributes.getPassword());
        assertEquals(routingUrl, attributes.getRoutingUrl());
    }

    @Test
    void testConstructor_WithMapAttributes_CreatesAttributes() {
        // Arrange
        String routingUrl = "https://example.com";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(ServiceNowConstants.CLIENT_ID, "client123");
        attributes.put(ServiceNowConstants.CLIENT_SECRET, "secret456");
        attributes.put(ServiceNowConstants.EMAIL, "user@example.com");
        attributes.put(ServiceNowConstants.INSTANCE_URL, "https://instance.service-now.com");
        attributes.put(ServiceNowConstants.AUTH_TYPE, "oauth");
        attributes.put(ServiceNowConstants.USERNAME, "admin");
        attributes.put(ServiceNowConstants.PASSWORD, "pass123");

        // Act
        ServiceNowAttributes serviceNowAttributes = new ServiceNowAttributes(routingUrl, attributes);

        // Assert
        assertNotNull(serviceNowAttributes);
        assertEquals("client123", serviceNowAttributes.getClientID());
        assertEquals("secret456", serviceNowAttributes.getClientSecret());
        assertEquals("user@example.com", serviceNowAttributes.getEmail());
        assertEquals("https://instance.service-now.com", serviceNowAttributes.getInstanceURL());
        assertEquals("oauth", serviceNowAttributes.getAuthType());
        assertEquals("admin", serviceNowAttributes.getUsername());
        assertEquals("pass123", serviceNowAttributes.getPassword());
    }

    @Test
    void testGetCallBackUri_ReturnsCorrectFormat() {
        // Arrange
        String routingUrl = "https://example.com";
        ServiceNowAttributes attributes = new ServiceNowAttributes(routingUrl, "client", "secret",
            "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        // Act
        String callbackUri = attributes.getCallBackUri();

        // Assert
        assertNotNull(callbackUri);
        assertTrue(callbackUri.startsWith(routingUrl));
        assertTrue(callbackUri.contains(ServiceNowConstants.JAXRS_ID));
        assertTrue(callbackUri.endsWith("/callback"));
    }

    @Test
    void testGetCallBackUri_ContainsJaxrsId() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        // Act
        String callbackUri = attributes.getCallBackUri();

        // Assert
        assertTrue(callbackUri.contains(ServiceNowConstants.JAXRS_ID));
    }

    @Test
    void testUpdate_WithNewAttributes_UpdatesAllFields() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "oldClient",
            "oldSecret", "old@test.com", "https://old.service-now.com", "basic", "oldUser", "oldPass");

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(ServiceNowConstants.CLIENT_ID, "newClient");
        newAttributes.put(ServiceNowConstants.CLIENT_SECRET, "newSecret");
        newAttributes.put(ServiceNowConstants.EMAIL, "new@test.com");
        newAttributes.put(ServiceNowConstants.INSTANCE_URL, "https://new.service-now.com");
        newAttributes.put(ServiceNowConstants.AUTH_TYPE, "oauth");
        newAttributes.put(ServiceNowConstants.USERNAME, "newUser");
        newAttributes.put(ServiceNowConstants.PASSWORD, "newPass");

        // Act
        attributes.update(newAttributes);

        // Assert
        assertEquals("newClient", attributes.getClientID());
        assertEquals("newSecret", attributes.getClientSecret());
        assertEquals("new@test.com", attributes.getEmail());
        assertEquals("https://new.service-now.com", attributes.getInstanceURL());
        assertEquals("oauth", attributes.getAuthType());
        assertEquals("newUser", attributes.getUsername());
        assertEquals("newPass", attributes.getPassword());
    }

    @Test
    void testUpdate_WithPartialAttributes_UpdatesOnlyProvidedFields() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        Map<String, Object> newAttributes = new HashMap<>();
        newAttributes.put(ServiceNowConstants.CLIENT_ID, "newClient");
        newAttributes.put(ServiceNowConstants.EMAIL, "newemail@test.com");

        // Act
        attributes.update(newAttributes);

        // Assert
        assertEquals("newClient", attributes.getClientID());
        assertEquals("newemail@test.com", attributes.getEmail());
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client123",
            "secret456", "user@example.com", "https://instance.service-now.com", "oauth", "admin", "pass123");

        // Act & Assert
        assertEquals("client123", attributes.getClientID());
        assertEquals("secret456", attributes.getClientSecret());
        assertEquals("user@example.com", attributes.getEmail());
        assertEquals("https://instance.service-now.com", attributes.getInstanceURL());
        assertEquals("oauth", attributes.getAuthType());
        assertEquals("admin", attributes.getUsername());
        assertEquals("pass123", attributes.getPassword());
        assertEquals("https://example.com", attributes.getRoutingUrl());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", null,
            null, null, null, null, null, null);

        // Assert
        assertNotNull(attributes);
        assertNull(attributes.getClientID());
        assertNull(attributes.getClientSecret());
        assertNull(attributes.getEmail());
        assertNull(attributes.getInstanceURL());
        assertNull(attributes.getAuthType());
        assertNull(attributes.getUsername());
        assertNull(attributes.getPassword());
    }

    @Test
    void testConstructor_WithEmptyMap_HandlesGracefully() {
        // Arrange
        Map<String, Object> emptyAttributes = new HashMap<>();

        // Act
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", emptyAttributes);

        // Assert
        assertNotNull(attributes);
        assertNull(attributes.getClientID());
        assertNull(attributes.getClientSecret());
        assertNull(attributes.getEmail());
        assertNull(attributes.getInstanceURL());
    }

    @Test
    void testGetOAuth20Service_ReturnsNonNull() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client123",
            "secret456", "user@example.com", "https://instance.service-now.com", "oauth", "admin", "pass123");

        // Act
        var oAuth20Service = attributes.getOAuth20Service();

        // Assert
        assertNotNull(oAuth20Service);
    }

    @Test
    void testGetOAuth20Service_ReturnsSameInstance() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client123",
            "secret456", "user@example.com", "https://instance.service-now.com", "oauth", "admin", "pass123");

        // Act
        var service1 = attributes.getOAuth20Service();
        var service2 = attributes.getOAuth20Service();

        // Assert
        assertSame(service1, service2);
    }

    @Test
    void testUserAccountConstant_HasCorrectValue() {
        // Assert
        assertEquals("useraccopunt", ServiceNowAttributes.USERACCOPUNT);
    }

    @Test
    void testCallbackConstant_HasCorrectValue() {
        // Assert
        assertEquals("/callback", ServiceNowAttributes.CALLBACK);
    }

    @Test
    void testGetCallBackUri_WithDifferentRoutingUrls_GeneratesCorrectUris() {
        // Arrange
        ServiceNowAttributes attributes1 = new ServiceNowAttributes("https://app1.example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");
        ServiceNowAttributes attributes2 = new ServiceNowAttributes("https://app2.example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        // Act
        String callback1 = attributes1.getCallBackUri();
        String callback2 = attributes2.getCallBackUri();

        // Assert
        assertNotEquals(callback1, callback2);
        assertTrue(callback1.startsWith("https://app1.example.com"));
        assertTrue(callback2.startsWith("https://app2.example.com"));
    }

    @Test
    void testUpdate_WithNullValues_SetsFieldsToNull() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        Map<String, Object> newAttributes = new HashMap<>();
        // Empty map will result in null values

        // Act
        attributes.update(newAttributes);

        // Assert
        assertNull(attributes.getClientID());
        assertNull(attributes.getClientSecret());
        assertNull(attributes.getEmail());
    }

    @Test
    void testConstructor_WithBasicAuth_CreatesAttributes() {
        // Arrange & Act
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", null,
            null, null, "https://instance.service-now.com", "basic", "admin", "password123");

        // Assert
        assertEquals("basic", attributes.getAuthType());
        assertEquals("admin", attributes.getUsername());
        assertEquals("password123", attributes.getPassword());
        assertNull(attributes.getClientID());
        assertNull(attributes.getClientSecret());
    }

    @Test
    void testConstructor_WithOAuthAuth_CreatesAttributes() {
        // Arrange & Act
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client123",
            "secret456", "user@example.com", "https://instance.service-now.com", "oauth", null, null);

        // Assert
        assertEquals("oauth", attributes.getAuthType());
        assertEquals("client123", attributes.getClientID());
        assertEquals("secret456", attributes.getClientSecret());
        assertEquals("user@example.com", attributes.getEmail());
        assertNull(attributes.getUsername());
        assertNull(attributes.getPassword());
    }

    @Test
    void testGetCallBackUri_Format_IsCorrect() {
        // Arrange
        ServiceNowAttributes attributes = new ServiceNowAttributes("https://example.com", "client",
            "secret", "email@test.com", "https://instance.service-now.com", "oauth", "user", "pass");

        // Act
        String callbackUri = attributes.getCallBackUri();

        // Assert
        String expected = "https://example.com/" + ServiceNowConstants.JAXRS_ID + "/callback";
        assertEquals(expected, callbackUri);
    }
}

