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
 * Unit tests for {@link Constants}.
 * <p>
 * Tests constant values, enums, and field definitions.
 * </p>
 */
class ConstantsTest {

    @Test
    void testMessageConstant_HasCorrectValue() {
        // Assert
        assertEquals("message", Constants.MESSAGE);
    }

    @Test
    void testPriorityConstant_HasCorrectValue() {
        // Assert
        assertEquals("Priority", Constants.PRIORITY);
    }

    @Test
    void testSysparmQueryConstant_HasCorrectValue() {
        // Assert
        assertEquals("sysparm_query", Constants.SYSPARM_QUERY);
    }

    @Test
    void testShortDescriptionConstant_HasCorrectValue() {
        // Assert
        assertEquals("short_description", Constants.SHORT_DESCRIPTION);
    }

    @Test
    void testAccountConstant_HasCorrectValue() {
        // Assert
        assertEquals("account", Constants.ACCOUNT);
    }

    @Test
    void testStateConstant_HasCorrectValue() {
        // Assert
        assertEquals("state", Constants.STATE);
    }

    @Test
    void testAssignmentGroupConstant_HasCorrectValue() {
        // Assert
        assertEquals("Assignment group", Constants.ASSIGNMENT_GROUP);
    }

    @Test
    void testCreateChangeRequestMessage_HasCorrectValue() {
        // Assert
        assertEquals("Create change request is created successfully with state New",
            Constants.CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW);
    }

    @Test
    void testTextField_IsNotNull() {
        // Assert
        assertNotNull(Constants.textField);
    }

    @Test
    void testTextField_IsValidFieldInstance() {
        // Assert
        assertNotNull(Constants.textField);
        assertTrue(Constants.textField instanceof app.krista.model.field.Field);
    }

    @Test
    void testOrderByEnum_HasAscValue() {
        // Act
        Constants.OrderBy orderBy = Constants.OrderBy.ASC;

        // Assert
        assertNotNull(orderBy);
        assertEquals("ASC", orderBy.name());
    }

    @Test
    void testOrderByEnum_HasDscValue() {
        // Act
        Constants.OrderBy orderBy = Constants.OrderBy.DSC;

        // Assert
        assertNotNull(orderBy);
        assertEquals("DSC", orderBy.name());
    }

    @Test
    void testOrderByEnum_HasTwoValues() {
        // Act
        Constants.OrderBy[] values = Constants.OrderBy.values();

        // Assert
        assertEquals(2, values.length);
    }

    @Test
    void testOrderByEnum_ValueOfAsc_ReturnsCorrectEnum() {
        // Act
        Constants.OrderBy orderBy = Constants.OrderBy.valueOf("ASC");

        // Assert
        assertEquals(Constants.OrderBy.ASC, orderBy);
    }

    @Test
    void testOrderByEnum_ValueOfDsc_ReturnsCorrectEnum() {
        // Act
        Constants.OrderBy orderBy = Constants.OrderBy.valueOf("DSC");

        // Assert
        assertEquals(Constants.OrderBy.DSC, orderBy);
    }

    @Test
    void testAuthTypeEnum_HasOAuthValue() {
        // Act
        Constants.AuthType authType = Constants.AuthType.OAUTH;

        // Assert
        assertNotNull(authType);
        assertEquals("OAUTH", authType.name());
    }

    @Test
    void testAuthTypeEnum_HasBasicValue() {
        // Act
        Constants.AuthType authType = Constants.AuthType.BASIC;

        // Assert
        assertNotNull(authType);
        assertEquals("BASIC", authType.name());
    }

    @Test
    void testAuthTypeEnum_HasApiTokenValue() {
        // Act
        Constants.AuthType authType = Constants.AuthType.API_TOKEN;

        // Assert
        assertNotNull(authType);
        assertEquals("API_TOKEN", authType.name());
    }

    @Test
    void testAuthTypeEnum_HasThreeValues() {
        // Act
        Constants.AuthType[] values = Constants.AuthType.values();

        // Assert
        assertEquals(3, values.length);
    }

    @Test
    void testAuthTypeEnum_ValueOfOAuth_ReturnsCorrectEnum() {
        // Act
        Constants.AuthType authType = Constants.AuthType.valueOf("OAUTH");

        // Assert
        assertEquals(Constants.AuthType.OAUTH, authType);
    }

    @Test
    void testAuthTypeEnum_ValueOfBasic_ReturnsCorrectEnum() {
        // Act
        Constants.AuthType authType = Constants.AuthType.valueOf("BASIC");

        // Assert
        assertEquals(Constants.AuthType.BASIC, authType);
    }

    @Test
    void testAuthTypeEnum_ValueOfApiToken_ReturnsCorrectEnum() {
        // Act
        Constants.AuthType authType = Constants.AuthType.valueOf("API_TOKEN");

        // Assert
        assertEquals(Constants.AuthType.API_TOKEN, authType);
    }

    @Test
    void testAllConstants_AreNotNull() {
        // Assert
        assertNotNull(Constants.MESSAGE);
        assertNotNull(Constants.PRIORITY);
        assertNotNull(Constants.SYSPARM_QUERY);
        assertNotNull(Constants.SHORT_DESCRIPTION);
        assertNotNull(Constants.ACCOUNT);
        assertNotNull(Constants.STATE);
        assertNotNull(Constants.ASSIGNMENT_GROUP);
        assertNotNull(Constants.CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW);
    }

    @Test
    void testAllConstants_AreNotEmpty() {
        // Assert
        assertFalse(Constants.MESSAGE.isEmpty());
        assertFalse(Constants.PRIORITY.isEmpty());
        assertFalse(Constants.SYSPARM_QUERY.isEmpty());
        assertFalse(Constants.SHORT_DESCRIPTION.isEmpty());
        assertFalse(Constants.ACCOUNT.isEmpty());
        assertFalse(Constants.STATE.isEmpty());
        assertFalse(Constants.ASSIGNMENT_GROUP.isEmpty());
        assertFalse(Constants.CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW.isEmpty());
    }

    @Test
    void testOrderByEnum_AllValuesAreDistinct() {
        // Arrange
        Constants.OrderBy asc = Constants.OrderBy.ASC;
        Constants.OrderBy dsc = Constants.OrderBy.DSC;

        // Assert
        assertNotEquals(asc, dsc);
    }

    @Test
    void testAuthTypeEnum_AllValuesAreDistinct() {
        // Arrange
        Constants.AuthType oauth = Constants.AuthType.OAUTH;
        Constants.AuthType basic = Constants.AuthType.BASIC;
        Constants.AuthType apiToken = Constants.AuthType.API_TOKEN;

        // Assert
        assertNotEquals(oauth, basic);
        assertNotEquals(basic, apiToken);
        assertNotEquals(oauth, apiToken);
    }

    @Test
    void testTextField_CanBeUsedInFieldDefinitions() {
        // Assert - Verify the field can be used without throwing exceptions
        assertDoesNotThrow(() -> {
            app.krista.model.field.Field field = Constants.textField;
            assertNotNull(field);
        });
    }
}

