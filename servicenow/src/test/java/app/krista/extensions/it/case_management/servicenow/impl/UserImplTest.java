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

package app.krista.extensions.it.case_management.servicenow.impl;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserImpl}.
 * <p>
 * Tests the creation and manipulation of User objects from JSON responses.
 * </p>
 */
class UserImplTest {

    @Test
    void testConstructor_WithAllParameters_CreatesUser() {
        // Arrange
        String sysId = "user123";
        String firstName = "John";
        String lastName = "Doe";
        String emailId = "john.doe@example.com";

        // Act
        UserImpl user = new UserImpl(sysId, firstName, lastName, emailId);

        // Assert
        assertNotNull(user);
        assertEquals(sysId, user.getSysId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(emailId, user.getEmailId());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        UserImpl user = new UserImpl(null, null, null, null);

        // Assert
        assertNotNull(user);
        assertNull(user.getSysId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmailId());
    }

    @Test
    void testCreate_WithValidJsonObject_ReturnsUser() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");
        jsonObject.put("first_name", "John");
        jsonObject.put("last_name", "Doe");
        jsonObject.put("email", "john.doe@example.com");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertNotNull(user);
        assertEquals("user123", user.getSysId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmailId());
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        UserImpl user = new UserImpl("user123", "John", "Doe", "john@example.com");

        // Act & Assert
        assertEquals("user123", user.getSysId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmailId());
    }

    @Test
    void testToString_ReturnsFullName() {
        // Arrange
        UserImpl user = new UserImpl("user123", "John", "Doe", "john@example.com");

        // Act
        String result = user.toString();

        // Assert
        assertEquals("John Doe", result);
    }

    @Test
    void testToString_WithNullNames_HandlesGracefully() {
        // Arrange
        UserImpl user = new UserImpl("user123", null, null, "john@example.com");

        // Act
        String result = user.toString();

        // Assert
        assertEquals("null null", result);
    }

    @Test
    void testCreate_WithEmailValidation_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");
        jsonObject.put("first_name", "John");
        jsonObject.put("last_name", "Doe");
        jsonObject.put("email", "john.doe@example.com");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertEquals("john.doe@example.com", user.getEmailId());
    }

    @Test
    void testCreate_WithSpecialCharactersInName_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");
        jsonObject.put("first_name", "Jean-Pierre");
        jsonObject.put("last_name", "O'Connor");
        jsonObject.put("email", "jean.pierre@example.com");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertEquals("Jean-Pierre", user.getFirstName());
        assertEquals("O'Connor", user.getLastName());
    }

    @Test
    void testCreate_WithEmptyStrings_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "");
        jsonObject.put("first_name", "");
        jsonObject.put("last_name", "");
        jsonObject.put("email", "");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertNotNull(user);
        assertEquals("", user.getSysId());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getEmailId());
    }

    @Test
    void testFindUserSysID_WithValidJsonObject_ReturnsSysId() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");

        // Act
        String sysId = UserImpl.findUserSysID(jsonObject);

        // Assert
        assertEquals("user123", sysId);
    }

    @Test
    void testCreate_WithLongNames_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");
        jsonObject.put("first_name", "Christopher");
        jsonObject.put("last_name", "Montgomery-Wellington");
        jsonObject.put("email", "christopher.montgomery@example.com");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertEquals("Christopher", user.getFirstName());
        assertEquals("Montgomery-Wellington", user.getLastName());
    }

    @Test
    void testToString_WithLongNames_FormatsCorrectly() {
        // Arrange
        UserImpl user = new UserImpl(
            "user123",
            "Christopher",
            "Montgomery-Wellington",
            "christopher@example.com"
        );

        // Act
        String result = user.toString();

        // Assert
        assertEquals("Christopher Montgomery-Wellington", result);
        assertTrue(result.contains(" "));
    }

    @Test
    void testCreate_WithVariousEmailFormats_HandlesCorrectly() throws JSONException {
        // Arrange - Standard email
        JSONObject standard = new JSONObject();
        standard.put("sys_id", "user001");
        standard.put("first_name", "John");
        standard.put("last_name", "Doe");
        standard.put("email", "john.doe@example.com");

        // Act
        UserImpl user1 = UserImpl.create(standard);

        // Assert
        assertEquals("john.doe@example.com", user1.getEmailId());

        // Arrange - Email with subdomain
        JSONObject subdomain = new JSONObject();
        subdomain.put("sys_id", "user002");
        subdomain.put("first_name", "Jane");
        subdomain.put("last_name", "Smith");
        subdomain.put("email", "jane.smith@mail.example.com");

        // Act
        UserImpl user2 = UserImpl.create(subdomain);

        // Assert
        assertEquals("jane.smith@mail.example.com", user2.getEmailId());
    }

    @Test
    void testCreate_WithSingleCharacterNames_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sys_id", "user123");
        jsonObject.put("first_name", "J");
        jsonObject.put("last_name", "D");
        jsonObject.put("email", "j.d@example.com");

        // Act
        UserImpl user = UserImpl.create(jsonObject);

        // Assert
        assertEquals("J", user.getFirstName());
        assertEquals("D", user.getLastName());
        assertEquals("J D", user.toString());
    }
}

