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

package app.krista.extensions.it.case_management.servicenow.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ServiceNowConstants}.
 * <p>
 * Tests the ServiceNow API constants and URL parameters.
 * </p>
 */
class ServiceNowConstantsTest {

    @Test
    void testAuthenticationConstants_AreCorrect() {
        // Assert
        assertEquals("clientId", ServiceNowConstants.CLIENT_ID);
        assertEquals("clientSecret", ServiceNowConstants.CLIENT_SECRET);
        assertEquals("Username", ServiceNowConstants.USERNAME);
        assertEquals("Password", ServiceNowConstants.PASSWORD);
        assertEquals("Instance URL", ServiceNowConstants.INSTANCE_URL);
        assertEquals("refresh_token", ServiceNowConstants.REFRESH_TOKEN);
    }

    @Test
    void testAuthTypeConstants_AreCorrect() {
        // Assert
        assertEquals("AuthType", ServiceNowConstants.AUTH_TYPE);
        assertEquals("Oauth", ServiceNowConstants.OAUTH);
        assertEquals("BasicAuth", ServiceNowConstants.BASIC);
    }

    @Test
    void testApiEndpointConstants_AreCorrect() {
        // Assert
        assertEquals("/api/sn_chg_rest/change/normal/", ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL);
        assertEquals("/api/sn_chg_rest/change/normal", ServiceNowConstants.CHANGE_URL);
    }

    @Test
    void testQueryParameterConstants_AreCorrect() {
        // Assert
        assertEquals("&sysparm_limit=", ServiceNowConstants.SYSPARM_LIMIT);
        assertEquals("?sysparm_limit=", ServiceNowConstants.SYSPARM_LIMIT_2);
        assertEquals("&sysparm_offset=", ServiceNowConstants.SYSPARM_OFFSET);
        assertEquals("?sysparm_query=", ServiceNowConstants.SYSPARM_QUERY);
    }

    @Test
    void testOperatorConstants_AreCorrect() {
        // Assert
        assertEquals("&ORDERBY", ServiceNowConstants.ORDERBY);
        assertEquals("&AND", ServiceNowConstants.AND);
    }

    @Test
    void testMiscConstants_AreCorrect() {
        // Assert
        assertEquals("code", ServiceNowConstants.CODE);
        assertEquals("servicenow", ServiceNowConstants.JAXRS_ID);
        assertEquals("redirect_uri", ServiceNowConstants.CALLBACK_URI);
        assertEquals("result", ServiceNowConstants.RESULT);
        assertEquals("Email", ServiceNowConstants.EMAIL);
        assertEquals("DESC", ServiceNowConstants.DESC);
        assertEquals("Invalid input", ServiceNowConstants.INVALID_INPUT);
    }

    @Test
    void testErrorMessageConstants_AreCorrect() {
        // Assert
        assertEquals("Search failed. ", ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN);
        assertEquals(
            "Search criteria cannot be empty. Please provide at least one search condition and try again.",
            ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY
        );
    }

    @Test
    void testSysparmLimitConstant_StartsWithAmpersand() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_LIMIT.startsWith("&"));
    }

    @Test
    void testSysparmLimit2Constant_StartsWithQuestionMark() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_LIMIT_2.startsWith("?"));
    }

    @Test
    void testSysparmOffsetConstant_StartsWithAmpersand() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_OFFSET.startsWith("&"));
    }

    @Test
    void testSysparmQueryConstant_StartsWithQuestionMark() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_QUERY.startsWith("?"));
    }

    @Test
    void testOrderByConstant_StartsWithAmpersand() {
        // Assert
        assertTrue(ServiceNowConstants.ORDERBY.startsWith("&"));
    }

    @Test
    void testAndConstant_StartsWithAmpersand() {
        // Assert
        assertTrue(ServiceNowConstants.AND.startsWith("&"));
    }

    @Test
    void testSysparmLimitConstant_ContainsCorrectParameter() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_LIMIT.contains("sysparm_limit"));
    }

    @Test
    void testSysparmOffsetConstant_ContainsCorrectParameter() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_OFFSET.contains("sysparm_offset"));
    }

    @Test
    void testSysparmQueryConstant_ContainsCorrectParameter() {
        // Assert
        assertTrue(ServiceNowConstants.SYSPARM_QUERY.contains("sysparm_query"));
    }

    @Test
    void testErrorMessages_AreNotEmpty() {
        // Assert
        assertFalse(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN.isEmpty());
        assertFalse(ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY.isEmpty());
    }

    @Test
    void testErrorMessages_AreDescriptive() {
        // Assert
        assertTrue(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN.contains("Search"));
        assertTrue(ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY.contains("Search criteria"));
    }

    @Test
    void testChangeUrlConstants_ContainApiPath() {
        // Assert
        assertTrue(ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL.contains("/api/"));
        assertTrue(ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL.contains("/sn_chg_rest/"));
        assertTrue(ServiceNowConstants.CHANGE_URL.contains("/api/"));
    }

    @Test
    void testChangeUrlConstant_EndsWithSlash() {
        // Assert
        assertTrue(ServiceNowConstants.API_SN_CHG_REST_CHANGE_NORMAL.endsWith("/"));
    }

    @Test
    void testChangeUrlConstant_DoesNotEndWithSlash() {
        // Assert
        assertFalse(ServiceNowConstants.CHANGE_URL.endsWith("/"));
    }

    @Test
    void testConstants_AreNotNull() {
        // Assert
        assertNotNull(ServiceNowConstants.CLIENT_ID);
        assertNotNull(ServiceNowConstants.CLIENT_SECRET);
        assertNotNull(ServiceNowConstants.USERNAME);
        assertNotNull(ServiceNowConstants.PASSWORD);
        assertNotNull(ServiceNowConstants.SYSPARM_LIMIT);
        assertNotNull(ServiceNowConstants.SYSPARM_OFFSET);
        assertNotNull(ServiceNowConstants.SYSPARM_QUERY);
        assertNotNull(ServiceNowConstants.ORDERBY);
        assertNotNull(ServiceNowConstants.AND);
        assertNotNull(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN);
        assertNotNull(ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY);
        assertNotNull(ServiceNowConstants.GSON);
    }

    @Test
    void testGsonInstance_IsNotNull() {
        // Assert
        assertNotNull(ServiceNowConstants.GSON);
    }

    @Test
    void testQueryParameterFormat_IsCorrect() {
        // Arrange
        String baseUrl = "https://example.service-now.com/api/now/table/incident";
        
        // Act
        String urlWithLimit = baseUrl + ServiceNowConstants.SYSPARM_LIMIT_2 + "10";
        String urlWithOffset = urlWithLimit + ServiceNowConstants.SYSPARM_OFFSET + "0";
        
        // Assert
        assertTrue(urlWithLimit.contains("?sysparm_limit=10"));
        assertTrue(urlWithOffset.contains("&sysparm_offset=0"));
    }

    @Test
    void testQueryBuilding_WithAndOperator() {
        // Arrange
        String query = "priority=1";
        
        // Act
        String queryWithAnd = query + ServiceNowConstants.AND;
        
        // Assert
        assertEquals("priority=1&AND", queryWithAnd);
    }

    @Test
    void testQueryBuilding_WithOrderBy() {
        // Arrange
        String query = "priority=1";
        
        // Act
        String queryWithOrderBy = query + ServiceNowConstants.ORDERBY + "number";
        
        // Assert
        assertEquals("priority=1&ORDERBYnumber", queryWithOrderBy);
    }

    @Test
    void testJaxrsId_IsCorrect() {
        // Assert
        assertEquals("servicenow", ServiceNowConstants.JAXRS_ID);
    }

    @Test
    void testCallbackUri_IsCorrect() {
        // Assert
        assertEquals("redirect_uri", ServiceNowConstants.CALLBACK_URI);
    }

    @Test
    void testAuthTypeValues_AreDistinct() {
        // Assert
        assertNotEquals(ServiceNowConstants.OAUTH, ServiceNowConstants.BASIC);
    }
}

