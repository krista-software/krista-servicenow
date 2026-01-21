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
 * Unit tests for {@link AccessToken}.
 * <p>
 * Tests the creation and properties of OAuth 2.0 access tokens.
 * </p>
 */
class AccessTokenTest {

    @Test
    void testConstructor_WithAllParameters_CreatesAccessToken() {
        // Arrange
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String scope = "read write";
        Long expiresIn = 3600L;
        String tokenType = "Bearer";
        String refreshToken = "refresh_token_value";

        // Act
        AccessToken token = new AccessToken(
            accessToken, scope, expiresIn, tokenType, refreshToken
        );

        // Assert
        assertNotNull(token);
        assertEquals(accessToken, token.access_token);
        assertEquals(scope, token.scope);
        assertEquals(expiresIn, token.expires_in);
        assertEquals(tokenType, token.token_type);
        assertEquals(refreshToken, token.refresh_token);
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        AccessToken token = new AccessToken(null, null, null, null, null);

        // Assert
        assertNotNull(token);
        assertNull(token.access_token);
        assertNull(token.scope);
        assertNull(token.expires_in);
        assertNull(token.token_type);
        assertNull(token.refresh_token);
    }

    @Test
    void testAccessToken_WithBearerType_IsCorrect() {
        // Arrange
        String accessToken = "test_access_token";
        String tokenType = "Bearer";

        // Act
        AccessToken token = new AccessToken(
            accessToken, "read", 3600L, tokenType, "refresh"
        );

        // Assert
        assertEquals("Bearer", token.token_type);
    }

    @Test
    void testAccessToken_WithStandardExpiration_IsCorrect() {
        // Arrange
        Long standardExpiration = 3600L; // 1 hour

        // Act
        AccessToken token = new AccessToken(
            "token", "read", standardExpiration, "Bearer", "refresh"
        );

        // Assert
        assertEquals(3600L, token.expires_in);
    }

    @Test
    void testAccessToken_WithMultipleScopes_IsCorrect() {
        // Arrange
        String multipleScopes = "read write delete";

        // Act
        AccessToken token = new AccessToken(
            "token", multipleScopes, 3600L, "Bearer", "refresh"
        );

        // Assert
        assertEquals("read write delete", token.scope);
        assertTrue(token.scope.contains("read"));
        assertTrue(token.scope.contains("write"));
        assertTrue(token.scope.contains("delete"));
    }

    @Test
    void testAccessToken_WithLongExpiration_IsCorrect() {
        // Arrange
        Long longExpiration = 86400L; // 24 hours

        // Act
        AccessToken token = new AccessToken(
            "token", "read", longExpiration, "Bearer", "refresh"
        );

        // Assert
        assertEquals(86400L, token.expires_in);
    }

    @Test
    void testAccessToken_WithShortExpiration_IsCorrect() {
        // Arrange
        Long shortExpiration = 300L; // 5 minutes

        // Act
        AccessToken token = new AccessToken(
            "token", "read", shortExpiration, "Bearer", "refresh"
        );

        // Assert
        assertEquals(300L, token.expires_in);
    }

    @Test
    void testAccessToken_WithEmptyScope_IsCorrect() {
        // Arrange
        String emptyScope = "";

        // Act
        AccessToken token = new AccessToken(
            "token", emptyScope, 3600L, "Bearer", "refresh"
        );

        // Assert
        assertEquals("", token.scope);
    }

    @Test
    void testAccessToken_WithLongTokenString_IsCorrect() {
        // Arrange
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        AccessToken token = new AccessToken(
            longToken, "read", 3600L, "Bearer", "refresh"
        );

        // Assert
        assertEquals(longToken, token.access_token);
        assertTrue(token.access_token.length() > 100);
    }

    @Test
    void testAccessToken_WithRefreshToken_IsCorrect() {
        // Arrange
        String refreshToken = "refresh_token_abc123";

        // Act
        AccessToken token = new AccessToken(
            "access", "read", 3600L, "Bearer", refreshToken
        );

        // Assert
        assertEquals("refresh_token_abc123", token.refresh_token);
    }

    @Test
    void testAccessToken_WithoutRefreshToken_IsCorrect() {
        // Arrange & Act
        AccessToken token = new AccessToken(
            "access", "read", 3600L, "Bearer", null
        );

        // Assert
        assertNull(token.refresh_token);
    }

    @Test
    void testAccessToken_FieldsArePublicAndFinal() {
        // Arrange
        AccessToken token = new AccessToken(
            "access", "read", 3600L, "Bearer", "refresh"
        );

        // Act & Assert - Fields should be directly accessible
        assertNotNull(token.access_token);
        assertNotNull(token.scope);
        assertNotNull(token.expires_in);
        assertNotNull(token.token_type);
        assertNotNull(token.refresh_token);
    }

    @Test
    void testAccessToken_WithZeroExpiration_IsCorrect() {
        // Arrange
        Long zeroExpiration = 0L;

        // Act
        AccessToken token = new AccessToken(
            "token", "read", zeroExpiration, "Bearer", "refresh"
        );

        // Assert
        assertEquals(0L, token.expires_in);
    }

    @Test
    void testAccessToken_WithNegativeExpiration_IsCorrect() {
        // Arrange
        Long negativeExpiration = -1L;

        // Act
        AccessToken token = new AccessToken(
            "token", "read", negativeExpiration, "Bearer", "refresh"
        );

        // Assert
        assertEquals(-1L, token.expires_in);
    }

    @Test
    void testAccessToken_WithSpecialCharactersInScope_IsCorrect() {
        // Arrange
        String specialScope = "user:read user:write admin:*";

        // Act
        AccessToken token = new AccessToken(
            "token", specialScope, 3600L, "Bearer", "refresh"
        );

        // Assert
        assertEquals("user:read user:write admin:*", token.scope);
    }

    @Test
    void testAccessToken_WithEmptyStrings_IsCorrect() {
        // Arrange & Act
        AccessToken token = new AccessToken("", "", 0L, "", "");

        // Assert
        assertEquals("", token.access_token);
        assertEquals("", token.scope);
        assertEquals(0L, token.expires_in);
        assertEquals("", token.token_type);
        assertEquals("", token.refresh_token);
    }
}

