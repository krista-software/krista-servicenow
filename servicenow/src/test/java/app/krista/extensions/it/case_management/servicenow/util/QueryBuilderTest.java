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
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QueryBuilder}.
 * <p>
 * Tests query building functionality including field mapping, operator conversion,
 * and pick-one value transformation for ServiceNow API queries.
 * </p>
 */
class QueryBuilderTest {

    @Test
    void testBuildQuery_WithNumericValue_AndEqualsOperator() {
        // Arrange
        String field = "Incident Id";
        String operator = "equals";
        String value = "12345";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("number=12345&AND", result);
    }

    @Test
    void testBuildQuery_WithNumericValue_AndGteOperator() {
        // Arrange
        String field = "Incident Id";
        String operator = "gte";
        String value = "100";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("number>=100&AND", result);
    }

    @Test
    void testBuildQuery_WithNumericValue_AndLteOperator() {
        // Arrange
        String field = "Incident Id";
        String operator = "lte";
        String value = "500";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("number<=500&AND", result);
    }

    @Test
    void testBuildQuery_WithStringValue_AndContainsOperator() {
        // Arrange
        String field = "Short Description";
        String operator = "contains";
        String value = "network";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("short_descriptionCONTAINSnetwork&AND", result);
    }

    @Test
    void testBuildQuery_WithStringValue_AndStartsWithOperator() {
        // Arrange
        String field = "Short Description";
        String operator = "startswith";
        String value = "Server";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("short_descriptionSTARTSWITHServer&AND", result);
    }

    @Test
    void testBuildQuery_WithStringValue_AndEndsWithOperator() {
        // Arrange
        String field = "Short Description";
        String operator = "endswith";
        String value = "down";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("short_descriptionENDSWITHdown&AND", result);
    }

    @Test
    void testBuildQuery_WithNullValue_ReturnsEmptyString() {
        // Arrange
        String field = "Incident Id";
        String operator = "equals";
        String value = null;

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testBuildQuery_WithEmptyValue_ReturnsEmptyString() {
        // Arrange
        String field = "Incident Id";
        String operator = "equals";
        String value = "";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testBuildQuery_WithInvalidOperator_ReturnsEmptyString() {
        // Arrange
        String field = "Incident Id";
        String operator = "invalid";
        String value = "12345";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, value);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithPriorityHigh() {
        // Arrange
        String fieldName = "Priority";
        String operand = "High";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("2", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithPriorityLow() {
        // Arrange
        String fieldName = "Priority";
        String operand = "Low";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("4", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithPriorityModerate() {
        // Arrange
        String fieldName = "Priority";
        String operand = "Moderate";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("3", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithPriorityCritical() {
        // Arrange
        String fieldName = "Priority";
        String operand = "Critical";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("1", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithStateNew() {
        // Arrange
        String fieldName = "State";
        String operand = "New";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("1", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithStateInProgress() {
        // Arrange
        String fieldName = "State";
        String operand = "In Progress";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("2", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithStateResolved() {
        // Arrange
        String fieldName = "State";
        String operand = "Resolved";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("6", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithChannelEmail() {
        // Arrange
        String fieldName = "Channel";
        String operand = "Email";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("email", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithChannelPhone() {
        // Arrange
        String fieldName = "Channel";
        String operand = "Phone";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("phone", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithUrgencyHigh() {
        // Arrange
        String fieldName = "Urgency";
        String operand = "High";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("1", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithImpactMedium() {
        // Arrange
        String fieldName = "Impact";
        String operand = "Medium";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("2", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithNonPickOneField_ReturnsOriginalValue() {
        // Arrange
        String fieldName = "Name";
        String operand = "John Doe";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("John Doe", result);
    }

    @Test
    void testReturnCorrectOperandValue_WithUnknownPickOneValue_ReturnsOriginalValue() {
        // Arrange
        String fieldName = "Priority";
        String operand = "Unknown";

        // Act
        String result = QueryBuilder.returnCorrectOperandValue(fieldName, operand);

        // Assert
        assertEquals("Unknown", result);
    }

    // ==================== Sanitization Tests ====================

    @Test
    void testSanitizeQueryValue_WithNull_ReturnsEmptyString() {
        // Act
        String result = QueryBuilder.sanitizeQueryValue(null);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testSanitizeQueryValue_WithEmptyString_ReturnsEmptyString() {
        // Act
        String result = QueryBuilder.sanitizeQueryValue("");

        // Assert
        assertEquals("", result);
    }

    @Test
    void testSanitizeQueryValue_WithNormalText_ReturnsUnchanged() {
        // Arrange
        String value = "Normal text without special characters";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("Normal text without special characters", result);
    }

    @Test
    void testSanitizeQueryValue_WithCaretCharacter_EscapesIt() {
        // Arrange
        String value = "value^with^carets";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("value\\^with\\^carets", result);
    }

    @Test
    void testSanitizeQueryValue_WithAmpersand_EscapesIt() {
        // Arrange
        String value = "value&with&ampersands";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("value\\&with\\&ampersands", result);
    }

    @Test
    void testSanitizeQueryValue_WithPipe_EscapesIt() {
        // Arrange
        String value = "value|with|pipes";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("value\\|with\\|pipes", result);
    }

    @Test
    void testSanitizeQueryValue_WithBackslash_EscapesIt() {
        // Arrange
        String value = "value\\with\\backslashes";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("value\\\\with\\\\backslashes", result);
    }

    @Test
    void testSanitizeQueryValue_WithAllSpecialCharacters_EscapesAll() {
        // Arrange
        String value = "test^value&with|special\\chars";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(value);

        // Assert
        assertEquals("test\\^value\\&with\\|special\\\\chars", result);
    }

    @Test
    void testSanitizeQueryValue_WithMaliciousInput_PreventsInjection() {
        // Arrange - Attempt to inject additional query conditions
        String maliciousValue = "normal^ORnumber=999";

        // Act
        String result = QueryBuilder.sanitizeQueryValue(maliciousValue);

        // Assert - The ^ should be escaped, preventing query injection
        assertEquals("normal\\^ORnumber=999", result);
    }

    @Test
    void testBuildQuery_WithMaliciousInput_UsesSanitization() {
        // Arrange - Attempt to inject additional query conditions
        String field = "Incident Id";
        String operator = "equals";
        String maliciousValue = "INC001^ORstate=closed";

        // Act
        String result = QueryBuilder.buildQuery(field, operator, maliciousValue);

        // Assert - The ^ should be escaped in the final query
        assertTrue(result.contains("\\^"), "Query should contain escaped caret");
        assertEquals("number=INC001\\^ORstate=closed&AND", result);
    }
}

