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

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JsonMapper}.
 * <p>
 * Tests JSON serialization and deserialization functionality using Gson.
 * </p>
 */
class JsonMapperTest {

    @Test
    void testFromJson_WithJsonObject_ReturnsObject() {
        // Arrange
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John Doe");
        jsonObject.addProperty("age", 30);

        // Act
        TestPerson result = JsonMapper.fromJson(jsonObject, TestPerson.class);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.name);
        assertEquals(30, result.age);
    }

    @Test
    void testFromJson_WithJsonString_ReturnsObject() {
        // Arrange
        String json = "{\"name\":\"Jane Smith\",\"age\":25}";

        // Act
        TestPerson result = JsonMapper.fromJson(json, TestPerson.class);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Smith", result.name);
        assertEquals(25, result.age);
    }

    @Test
    void testFromJson_WithEmptyJsonString_ReturnsEmptyObject() {
        // Arrange
        String json = "{}";

        // Act
        TestPerson result = JsonMapper.fromJson(json, TestPerson.class);

        // Assert
        assertNotNull(result);
        assertNull(result.name);
        assertEquals(0, result.age);
    }

    @Test
    void testFromJson_WithNullJsonString_ReturnsNull() {
        // Arrange
        String json = null;

        // Act
        TestPerson result = JsonMapper.fromJson(json, TestPerson.class);

        // Assert
        assertNull(result);
    }

    @Test
    void testToJson_WithObject_ReturnsJsonString() {
        // Arrange
        TestPerson person = new TestPerson();
        person.name = "Bob Johnson";
        person.age = 35;

        // Act
        String result = JsonMapper.toJson(person);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("\"name\": \"Bob Johnson\""));
        assertTrue(result.contains("\"age\": 35"));
    }

    @Test
    void testToJson_WithNullObject_ReturnsNullString() {
        // Arrange
        TestPerson person = null;

        // Act
        String result = JsonMapper.toJson(person);

        // Assert
        assertEquals("null", result);
    }

    @Test
    void testToJson_WithEmptyObject_ReturnsEmptyJsonString() {
        // Arrange
        TestPerson person = new TestPerson();

        // Act
        String result = JsonMapper.toJson(person);

        // Assert
        assertNotNull(result);
        // Gson with serializeNulls() will include null values
        assertTrue(result.contains("\"age\"") || result.contains("age"));
    }

    @Test
    void testFromJson_WithComplexObject_ReturnsObject() {
        // Arrange
        String json = "{\"name\":\"Alice\",\"age\":28,\"address\":{\"street\":\"123 Main St\",\"city\":\"Boston\"}}";

        // Act
        TestPersonWithAddress result = JsonMapper.fromJson(json, TestPersonWithAddress.class);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.name);
        assertEquals(28, result.age);
        assertNotNull(result.address);
        assertEquals("123 Main St", result.address.street);
        assertEquals("Boston", result.address.city);
    }

    @Test
    void testToJson_WithComplexObject_ReturnsJsonString() {
        // Arrange
        TestPersonWithAddress person = new TestPersonWithAddress();
        person.name = "Charlie";
        person.age = 40;
        person.address = new TestAddress();
        person.address.street = "456 Oak Ave";
        person.address.city = "New York";

        // Act
        String result = JsonMapper.toJson(person);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("\"name\": \"Charlie\""));
        assertTrue(result.contains("\"age\": 40"));
        assertTrue(result.contains("\"street\": \"456 Oak Ave\""));
        assertTrue(result.contains("\"city\": \"New York\""));
    }

    @Test
    void testFromJson_WithArray_ReturnsList() {
        // Arrange
        String json = "[{\"name\":\"Person1\",\"age\":20},{\"name\":\"Person2\",\"age\":30}]";

        // Act
        List<TestPerson> result = JsonMapper.fromJson(json, 
            new com.google.gson.reflect.TypeToken<List<TestPerson>>(){}.getType());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Person1", result.get(0).name);
        assertEquals(20, result.get(0).age);
        assertEquals("Person2", result.get(1).name);
        assertEquals(30, result.get(1).age);
    }

    @Test
    void testToJson_WithList_ReturnsJsonArray() {
        // Arrange
        List<TestPerson> people = List.of(
            createPerson("Person1", 20),
            createPerson("Person2", 30)
        );

        // Act
        String result = JsonMapper.toJson(people);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("\"name\": \"Person1\""));
        assertTrue(result.contains("\"age\": 20"));
        assertTrue(result.contains("\"name\": \"Person2\""));
        assertTrue(result.contains("\"age\": 30"));
    }

    @Test
    void testGsonInstance_IsPrettyPrinting() {
        // Arrange
        TestPerson person = new TestPerson();
        person.name = "Test";
        person.age = 25;

        // Act
        String result = JsonMapper.toJson(person);

        // Assert
        assertTrue(result.contains("\n"), "JSON should be pretty-printed with newlines");
        assertTrue(result.contains("  "), "JSON should be pretty-printed with indentation");
    }

    // Helper classes for testing
    private static class TestPerson {
        String name;
        int age;
    }

    private static class TestAddress {
        String street;
        String city;
    }

    private static class TestPersonWithAddress {
        String name;
        int age;
        TestAddress address;
    }

    private TestPerson createPerson(String name, int age) {
        TestPerson person = new TestPerson();
        person.name = name;
        person.age = age;
        return person;
    }
}

