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

package app.krista.extensions.it.case_management.servicenow.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ServiceNowApi20}.
 * <p>
 * Tests ServiceNow OAuth 2.0 API endpoints and URL generation.
 * </p>
 */
class ServiceNowApi20Test {

    @Test
    void testInstance_WithValidUrl_ReturnsInstance() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";

        // Act
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertNotNull(api);
    }

    @Test
    void testInstance_ReturnsSameInstance() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";

        // Act
        ServiceNowApi20 api1 = ServiceNowApi20.instance(instanceUrl);
        ServiceNowApi20 api2 = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertSame(api1, api2);
    }

    @Test
    void testGetAccessTokenEndpoint_ReturnsCorrectUrl() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertNotNull(endpoint);
        assertTrue(endpoint.contains("oauth_token.do"));
        assertTrue(endpoint.startsWith(instanceUrl));
    }

    @Test
    void testGetAccessTokenEndpoint_ContainsInstanceUrl() {
        // Arrange
        String instanceUrl = "https://mycompany.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertTrue(endpoint.contains("mycompany.service-now.com"));
    }

    @Test
    void testGetAccessTokenEndpoint_EndsWithOAuthToken() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertTrue(endpoint.endsWith("oauth_token.do"));
    }

    @Test
    void testGetAccessTokenExtractor_ReturnsNonNull() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        var extractor = api.getAccessTokenExtractor();

        // Assert
        assertNotNull(extractor);
    }

    @Test
    void testInstance_WithDifferentUrls_UpdatesEndpoint() {
        // Arrange
        String url1 = "https://instance1.service-now.com/";
        String url2 = "https://instance2.service-now.com/";

        // Act
        ServiceNowApi20 api1 = ServiceNowApi20.instance(url1);
        String endpoint1 = api1.getAccessTokenEndpoint();

        ServiceNowApi20 api2 = ServiceNowApi20.instance(url2);
        String endpoint2 = api2.getAccessTokenEndpoint();

        // Assert
        assertTrue(endpoint2.contains("instance2"));
    }

    @Test
    void testGetAccessTokenEndpoint_WithTrailingSlash_FormatsCorrectly() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertEquals("https://instance.service-now.com/oauth_token.do", endpoint);
    }

    @Test
    void testGetAccessTokenEndpoint_WithoutTrailingSlash_FormatsCorrectly() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertTrue(endpoint.contains("oauth_token.do"));
    }

    @Test
    void testInstance_WithHttpsUrl_AcceptsUrl() {
        // Arrange
        String instanceUrl = "https://secure.service-now.com/";

        // Act
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertNotNull(api);
        assertTrue(api.getAccessTokenEndpoint().startsWith("https://"));
    }

    @Test
    void testGetAccessTokenEndpoint_IsValidUrl() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertDoesNotThrow(() -> new java.net.URL(endpoint));
    }

    @Test
    void testInstance_WithCustomDomain_ReturnsInstance() {
        // Arrange
        String instanceUrl = "https://custom-domain.example.com/";

        // Act
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertNotNull(api);
        assertTrue(api.getAccessTokenEndpoint().contains("custom-domain.example.com"));
    }

    @Test
    void testGetAccessTokenEndpoint_WithDifferentInstances_ReturnsDifferentEndpoints() {
        // Arrange
        String url1 = "https://dev.service-now.com/";
        String url2 = "https://prod.service-now.com/";

        // Act
        ServiceNowApi20 api1 = ServiceNowApi20.instance(url1);
        String endpoint1 = api1.getAccessTokenEndpoint();

        ServiceNowApi20 api2 = ServiceNowApi20.instance(url2);
        String endpoint2 = api2.getAccessTokenEndpoint();

        // Assert
        assertNotEquals(endpoint1, endpoint2);
    }

    @Test
    void testGetAccessTokenExtractor_IsSingleton() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        var extractor1 = api.getAccessTokenExtractor();
        var extractor2 = api.getAccessTokenExtractor();

        // Assert
        assertSame(extractor1, extractor2);
    }

    @Test
    void testInstance_WithSubdomain_HandlesCorrectly() {
        // Arrange
        String instanceUrl = "https://sub.domain.service-now.com/";

        // Act
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertNotNull(api);
        assertTrue(api.getAccessTokenEndpoint().contains("sub.domain.service-now.com"));
    }

    @Test
    void testGetAccessTokenEndpoint_Format_IsCorrect() {
        // Arrange
        String instanceUrl = "https://test.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertTrue(endpoint.matches("https://.*\\.service-now\\.com/oauth_token\\.do"));
    }

    @Test
    void testInstance_WithPort_HandlesCorrectly() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com:8443/";

        // Act
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Assert
        assertNotNull(api);
        assertTrue(api.getAccessTokenEndpoint().contains(":8443"));
    }

    @Test
    void testGetAccessTokenEndpoint_WithComplexUrl_FormatsCorrectly() {
        // Arrange
        String instanceUrl = "https://my-company-dev.service-now.com/";
        ServiceNowApi20 api = ServiceNowApi20.instance(instanceUrl);

        // Act
        String endpoint = api.getAccessTokenEndpoint();

        // Assert
        assertEquals("https://my-company-dev.service-now.com/oauth_token.do", endpoint);
    }
}

