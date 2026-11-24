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

package app.krista.extensions.it.case_management.servicenow.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ServiceNowUtil}.
 * <p>
 * Tests utility methods for ServiceNow operations including priority mappings,
 * URL construction, and value lookups.
 * </p>
 */
class ServiceNowUtilTest {

    @Test
    void testGetPriorityFromKey_WithValidKeys_ReturnsCorrectPriority() {
        // Act & Assert
        assertEquals("Critical", ServiceNowUtil.getPriorityFromKey("1"));
        assertEquals("High", ServiceNowUtil.getPriorityFromKey("2"));
        assertEquals("Moderate", ServiceNowUtil.getPriorityFromKey("3"));
        assertEquals("Low", ServiceNowUtil.getPriorityFromKey("4"));
    }

    @Test
    void testGetPriorityFromKey_WithInvalidKey_ReturnsNull() {
        // Act
        String result = ServiceNowUtil.getPriorityFromKey("5");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetPriorityFromKey_WithNullKey_ReturnsNull() {
        // Act
        String result = ServiceNowUtil.getPriorityFromKey(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetPriorityFromKey_WithEmptyKey_ReturnsNull() {
        // Act
        String result = ServiceNowUtil.getPriorityFromKey("");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetKeyFromValue_WithValidValue_ReturnsKey() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "Critical");
        testMap.put("2", "High");
        testMap.put("3", "Moderate");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "Critical");

        // Assert
        assertEquals("1", result);
    }

    @Test
    void testGetKeyFromValue_WithInvalidValue_ReturnsNull() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "Critical");
        testMap.put("2", "High");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "Unknown");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetKeyFromValue_WithNullValue_ReturnsNull() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "Critical");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, null);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetKeyFromValue_WithEmptyMap_ReturnsNull() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "Critical");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetKeyFromValue_WithMultipleEntries_ReturnsFirstMatch() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "Critical");
        testMap.put("2", "High");
        testMap.put("3", "Moderate");
        testMap.put("4", "Low");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "High");

        // Assert
        assertEquals("2", result);
    }

    @Test
    void testPriorityKeyValues_ContainsAllExpectedMappings() {
        // Assert
        assertNotNull(ServiceNowUtil.priorityKeyValues);
        assertEquals(4, ServiceNowUtil.priorityKeyValues.size());
        assertEquals("Critical", ServiceNowUtil.priorityKeyValues.get("1"));
        assertEquals("High", ServiceNowUtil.priorityKeyValues.get("2"));
        assertEquals("Moderate", ServiceNowUtil.priorityKeyValues.get("3"));
        assertEquals("Low", ServiceNowUtil.priorityKeyValues.get("4"));
    }

    @Test
    void testConstants_HaveCorrectValues() {
        // Assert
        assertEquals(1000, ServiceNowUtil.MAX_RESULT);
        assertEquals("", ServiceNowUtil.EMPTYSTRING);
        assertEquals("/api/sn_customerservice/", ServiceNowUtil.API_SN_CUSTOMERSERVICE);
        assertEquals("/api/now/v1/table/", ServiceNowUtil.API_NOW_V_1_TABLE);
        assertEquals("Critical", ServiceNowUtil.CRITICAL);
        assertEquals("High", ServiceNowUtil.HIGH);
        assertEquals("Moderate", ServiceNowUtil.MODERATE);
        assertEquals("Low", ServiceNowUtil.LOW);
    }

    @Test
    void testMaxResult_IsPositive() {
        // Assert
        assertTrue(ServiceNowUtil.MAX_RESULT > 0);
    }

    @Test
    void testApiPaths_StartWithSlash() {
        // Assert
        assertTrue(ServiceNowUtil.API_SN_CUSTOMERSERVICE.startsWith("/"));
        assertTrue(ServiceNowUtil.API_NOW_V_1_TABLE.startsWith("/"));
    }

    @Test
    void testApiPaths_EndWithSlash() {
        // Assert
        assertTrue(ServiceNowUtil.API_SN_CUSTOMERSERVICE.endsWith("/"));
        assertTrue(ServiceNowUtil.API_NOW_V_1_TABLE.endsWith("/"));
    }

    @Test
    void testApiPaths_ContainApi() {
        // Assert
        assertTrue(ServiceNowUtil.API_SN_CUSTOMERSERVICE.contains("/api/"));
        assertTrue(ServiceNowUtil.API_NOW_V_1_TABLE.contains("/api/"));
    }

    @Test
    void testEmptyString_IsEmpty() {
        // Assert
        assertTrue(ServiceNowUtil.EMPTYSTRING.isEmpty());
        assertEquals(0, ServiceNowUtil.EMPTYSTRING.length());
    }

    @Test
    void testPriorityConstants_AreNotNull() {
        // Assert
        assertNotNull(ServiceNowUtil.CRITICAL);
        assertNotNull(ServiceNowUtil.HIGH);
        assertNotNull(ServiceNowUtil.MODERATE);
        assertNotNull(ServiceNowUtil.LOW);
    }

    @Test
    void testPriorityConstants_AreNotEmpty() {
        // Assert
        assertFalse(ServiceNowUtil.CRITICAL.isEmpty());
        assertFalse(ServiceNowUtil.HIGH.isEmpty());
        assertFalse(ServiceNowUtil.MODERATE.isEmpty());
        assertFalse(ServiceNowUtil.LOW.isEmpty());
    }

    @Test
    void testPriorityConstants_AreDistinct() {
        // Assert
        assertNotEquals(ServiceNowUtil.CRITICAL, ServiceNowUtil.HIGH);
        assertNotEquals(ServiceNowUtil.HIGH, ServiceNowUtil.MODERATE);
        assertNotEquals(ServiceNowUtil.MODERATE, ServiceNowUtil.LOW);
        assertNotEquals(ServiceNowUtil.CRITICAL, ServiceNowUtil.LOW);
    }

    @Test
    void testGetKeyFromValue_WithPriorityMap_ReturnsCorrectKeys() {
        // Act & Assert
        assertEquals("1", ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues, "Critical"));
        assertEquals("2", ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues, "High"));
        assertEquals("3", ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues, "Moderate"));
        assertEquals("4", ServiceNowUtil.getKeyFromValue(ServiceNowUtil.priorityKeyValues, "Low"));
    }

    @Test
    void testGetKeyFromValue_WithCaseSensitiveValue_ReturnsNull() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "Critical");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "critical");

        // Assert
        assertNull(result);
    }

    @Test
    void testGetKeyFromValue_WithExactMatch_ReturnsKey() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("key1", "Value with spaces");
        testMap.put("key2", "Another Value");

        // Act
        String result = ServiceNowUtil.getKeyFromValue(testMap, "Value with spaces");

        // Assert
        assertEquals("key1", result);
    }

    @Test
    void testPriorityKeyValues_IsImmutableReference() {
        // Arrange
        Map<String, String> originalMap = ServiceNowUtil.priorityKeyValues;

        // Act
        Map<String, String> sameMap = ServiceNowUtil.priorityKeyValues;

        // Assert
        assertSame(originalMap, sameMap);
    }

    @Test
    void testGetPriorityFromKey_WithAllValidKeys_ReturnsNonNull() {
        // Arrange
        String[] validKeys = {"1", "2", "3", "4"};

        // Act & Assert
        for (String key : validKeys) {
            assertNotNull(ServiceNowUtil.getPriorityFromKey(key),
                "Priority for key " + key + " should not be null");
        }
    }

    @Test
    void testApiPaths_HaveCorrectFormat() {
        // Assert
        assertTrue(ServiceNowUtil.API_SN_CUSTOMERSERVICE.matches("^/api/[a-z_]+/$"));
        assertTrue(ServiceNowUtil.API_NOW_V_1_TABLE.matches("^/api/[a-z0-9_/]+/$"));
    }

    @Test
    void testMaxResult_IsReasonableLimit() {
        // Assert
        assertTrue(ServiceNowUtil.MAX_RESULT >= 100, "MAX_RESULT should be at least 100");
        assertTrue(ServiceNowUtil.MAX_RESULT <= 10000, "MAX_RESULT should not exceed 10000");
    }

    @Test
    void testGetKeyFromValue_WithSpecialCharacters_HandlesCorrectly() {
        // Arrange
        Map<String, String> testMap = new HashMap<>();
        testMap.put("key1", "Value-with-dashes");
        testMap.put("key2", "Value_with_underscores");
        testMap.put("key3", "Value.with.dots");

        // Act & Assert
        assertEquals("key1", ServiceNowUtil.getKeyFromValue(testMap, "Value-with-dashes"));
        assertEquals("key2", ServiceNowUtil.getKeyFromValue(testMap, "Value_with_underscores"));
        assertEquals("key3", ServiceNowUtil.getKeyFromValue(testMap, "Value.with.dots"));
    }
}

