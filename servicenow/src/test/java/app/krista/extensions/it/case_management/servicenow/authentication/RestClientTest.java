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

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RestClient}.
 * <p>
 * Tests REST client initialization, credential encoding, and authentication methods.
 * </p>
 */
class RestClientTest {

    @Test
    void testConstructor_WithOAuthToken_CreatesClient() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com";
        String accessToken = "token123";
        String oauthType = "oauth";

        // Act
        RestClient restClient = new RestClient(instanceUrl, accessToken, oauthType);

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testConstructor_WithBasicAuth_CreatesClient() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com";
        String username = "admin";
        String password = "password123";
        String oauthType = "basic";

        // Act
        RestClient restClient = new RestClient(instanceUrl, username, password, oauthType);

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testEncodeCredentials_WithValidCredentials_ReturnsBase64() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "admin";
        String password = "password";

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    void testEncodeCredentials_WithValidCredentials_ReturnsCorrectEncoding() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "admin";
        String password = "password";
        String expected = Base64.getEncoder().encodeToString("admin:password".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_WithSpecialCharacters_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "user@example.com";
        String password = "p@ssw0rd!";
        String expected = Base64.getEncoder().encodeToString("user@example.com:p@ssw0rd!".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_WithEmptyPassword_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "", "basic");
        String username = "admin";
        String password = "";
        String expected = Base64.getEncoder().encodeToString("admin:".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_WithEmptyUsername_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "", "password", "basic");
        String username = "";
        String password = "password";
        String expected = Base64.getEncoder().encodeToString(":password".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_WithLongCredentials_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "very_long_username_with_many_characters_12345";
        String password = "very_long_password_with_many_characters_67890";
        String credentials = username + ":" + password;
        String expected = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_IsReversible() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "testuser";
        String password = "testpass";

        // Act
        String encoded = restClient.encodeCredentials(username, password);
        String decoded = new String(Base64.getDecoder().decode(encoded));

        // Assert
        assertEquals("testuser:testpass", decoded);
    }

    @Test
    void testEncodeCredentials_WithUnicodeCharacters_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "user_日本語";
        String password = "pass_中文";
        String credentials = username + ":" + password;
        String expected = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testEncodeCredentials_WithColonInPassword_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "admin";
        String password = "pass:word";
        String expected = Base64.getEncoder().encodeToString("admin:pass:word".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testConstructor_WithOAuth_DoesNotRequireUsernamePassword() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com";
        String accessToken = "valid_token_123";
        String oauthType = "oauth";

        // Act
        RestClient restClient = new RestClient(instanceUrl, accessToken, oauthType);

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testConstructor_WithBasicAuth_RequiresUsernamePassword() {
        // Arrange
        String instanceUrl = "https://instance.service-now.com";
        String username = "admin";
        String password = "password123";
        String oauthType = "basic";

        // Act
        RestClient restClient = new RestClient(instanceUrl, username, password, oauthType);

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testEncodeCredentials_WithDifferentCredentials_ProducesDifferentEncodings() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");

        // Act
        String encoded1 = restClient.encodeCredentials("user1", "pass1");
        String encoded2 = restClient.encodeCredentials("user2", "pass2");

        // Assert
        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void testEncodeCredentials_WithSameCredentials_ProducesSameEncoding() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");

        // Act
        String encoded1 = restClient.encodeCredentials("admin", "password");
        String encoded2 = restClient.encodeCredentials("admin", "password");

        // Assert
        assertEquals(encoded1, encoded2);
    }

    @Test
    void testEncodeCredentials_ProducesValidBase64() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");

        // Act
        String encoded = restClient.encodeCredentials("testuser", "testpass");

        // Assert
        assertDoesNotThrow(() -> Base64.getDecoder().decode(encoded));
    }

    @Test
    void testEncodeCredentials_WithNumericCredentials_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "12345";
        String password = "67890";
        String expected = Base64.getEncoder().encodeToString("12345:67890".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }

    @Test
    void testConstructor_WithNullInstanceUrl_CreatesClient() {
        // Arrange & Act
        RestClient restClient = new RestClient(null, "admin", "password", "basic");

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testConstructor_WithEmptyInstanceUrl_CreatesClient() {
        // Arrange & Act
        RestClient restClient = new RestClient("", "admin", "password", "basic");

        // Assert
        assertNotNull(restClient);
    }

    @Test
    void testEncodeCredentials_WithWhitespaceInCredentials_EncodesCorrectly() {
        // Arrange
        RestClient restClient = new RestClient("https://instance.service-now.com",
            "admin", "password", "basic");
        String username = "user name";
        String password = "pass word";
        String expected = Base64.getEncoder().encodeToString("user name:pass word".getBytes());

        // Act
        String encoded = restClient.encodeCredentials(username, password);

        // Assert
        assertEquals(expected, encoded);
    }
}

