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
 * Unit tests for {@link CustomerImpl}.
 * <p>
 * Tests the creation and manipulation of Customer objects from JSON responses.
 * </p>
 */
class CustomerImplTest {

    @Test
    void testConstructor_WithAllParameters_CreatesCustomer() {
        // Arrange
        String name = "ACME Corporation";
        String customerId = "ACC001";
        String phone = "+1-555-0100";
        String city = "San Francisco";
        String zipCode = "94102";
        String sysId = "cust123";

        // Act
        CustomerImpl customer = new CustomerImpl(name, customerId, phone, city, zipCode, sysId);

        // Assert
        assertNotNull(customer);
        assertEquals(name, customer.getName());
        assertEquals(customerId, customer.getCustomerId());
        assertEquals(phone, customer.getPhone());
        assertEquals(city, customer.getCity());
        assertEquals(zipCode, customer.getZipCode());
        assertEquals(sysId, customer.getSysId());
    }

    @Test
    void testConstructor_WithNullValues_HandlesGracefully() {
        // Arrange & Act
        CustomerImpl customer = new CustomerImpl(null, null, null, null, null, null);

        // Assert
        assertNotNull(customer);
        assertNull(customer.getName());
        assertNull(customer.getCustomerId());
        assertNull(customer.getPhone());
    }

    @Test
    void testCreate_WithValidJsonObject_ReturnsCustomer() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "ACME Corporation");
        jsonObject.put("number", "ACC001");
        jsonObject.put("phone", "+1-555-0100");
        jsonObject.put("city", "San Francisco");
        jsonObject.put("zip", "94102");
        jsonObject.put("sys_id", "cust123");

        // Act
        CustomerImpl customer = CustomerImpl.create(jsonObject);

        // Assert
        assertNotNull(customer);
        assertEquals("ACME Corporation", customer.getName());
        assertEquals("ACC001", customer.getCustomerId());
        assertEquals("+1-555-0100", customer.getPhone());
        assertEquals("San Francisco", customer.getCity());
        assertEquals("94102", customer.getZipCode());
        assertEquals("cust123", customer.getSysId());
    }

    @Test
    void testGetters_ReturnCorrectValues() {
        // Arrange
        CustomerImpl customer = new CustomerImpl(
            "ACME Corp", "ACC001", "+1-555-0100", "SF", "94102", "cust123"
        );

        // Act & Assert
        assertEquals("ACME Corp", customer.getName());
        assertEquals("ACC001", customer.getCustomerId());
        assertEquals("+1-555-0100", customer.getPhone());
        assertEquals("SF", customer.getCity());
        assertEquals("94102", customer.getZipCode());
        assertEquals("cust123", customer.getSysId());
    }

    @Test
    void testCreate_WithDifferentCities_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Tech Corp");
        jsonObject.put("number", "ACC002");
        jsonObject.put("phone", "+1-555-0200");
        jsonObject.put("city", "New York");
        jsonObject.put("zip", "10001");
        jsonObject.put("sys_id", "cust456");

        // Act
        CustomerImpl customer = CustomerImpl.create(jsonObject);

        // Assert
        assertEquals("New York", customer.getCity());
        assertEquals("10001", customer.getZipCode());
    }

    @Test
    void testCreate_WithPhoneFormats_HandlesCorrectly() throws JSONException {
        // Arrange - US format
        JSONObject usPhone = new JSONObject();
        usPhone.put("name", "US Corp");
        usPhone.put("number", "ACC001");
        usPhone.put("phone", "+1-555-0100");
        usPhone.put("city", "Boston");
        usPhone.put("zip", "02101");
        usPhone.put("sys_id", "cust001");

        // Act
        CustomerImpl usCust = CustomerImpl.create(usPhone);

        // Assert
        assertEquals("+1-555-0100", usCust.getPhone());

        // Arrange - International format
        JSONObject intlPhone = new JSONObject();
        intlPhone.put("name", "UK Corp");
        intlPhone.put("number", "ACC002");
        intlPhone.put("phone", "+44-20-1234-5678");
        intlPhone.put("city", "London");
        intlPhone.put("zip", "SW1A");
        intlPhone.put("sys_id", "cust002");

        // Act
        CustomerImpl intlCust = CustomerImpl.create(intlPhone);

        // Assert
        assertEquals("+44-20-1234-5678", intlCust.getPhone());
    }

    @Test
    void testCreate_WithSpecialCharactersInName_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "ACME Corp. & Associates, Inc.");
        jsonObject.put("number", "ACC123");
        jsonObject.put("phone", "+1-555-0100");
        jsonObject.put("city", "San Francisco");
        jsonObject.put("zip", "94102");
        jsonObject.put("sys_id", "cust123");

        // Act
        CustomerImpl customer = CustomerImpl.create(jsonObject);

        // Assert
        assertEquals("ACME Corp. & Associates, Inc.", customer.getName());
    }

    @Test
    void testCreate_WithEmptyStrings_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "");
        jsonObject.put("number", "");
        jsonObject.put("phone", "");
        jsonObject.put("city", "");
        jsonObject.put("zip", "");
        jsonObject.put("sys_id", "");

        // Act
        CustomerImpl customer = CustomerImpl.create(jsonObject);

        // Assert
        assertNotNull(customer);
        assertEquals("", customer.getName());
        assertEquals("", customer.getCustomerId());
        assertEquals("", customer.getPhone());
    }

    @Test
    void testCreate_WithVariousZipCodeFormats_HandlesCorrectly() throws JSONException {
        // Arrange - 5 digit
        JSONObject zip5 = new JSONObject();
        zip5.put("name", "Corp1");
        zip5.put("number", "ACC001");
        zip5.put("phone", "+1-555-0100");
        zip5.put("city", "Boston");
        zip5.put("zip", "02101");
        zip5.put("sys_id", "cust001");

        // Act
        CustomerImpl cust1 = CustomerImpl.create(zip5);

        // Assert
        assertEquals("02101", cust1.getZipCode());

        // Arrange - ZIP+4
        JSONObject zip9 = new JSONObject();
        zip9.put("name", "Corp2");
        zip9.put("number", "ACC002");
        zip9.put("phone", "+1-555-0200");
        zip9.put("city", "New York");
        zip9.put("zip", "10001-1234");
        zip9.put("sys_id", "cust002");

        // Act
        CustomerImpl cust2 = CustomerImpl.create(zip9);

        // Assert
        assertEquals("10001-1234", cust2.getZipCode());
    }

    @Test
    void testCreate_WithLongCityNames_HandlesCorrectly() throws JSONException {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Test Corp");
        jsonObject.put("number", "ACC001");
        jsonObject.put("phone", "+1-555-0100");
        jsonObject.put("city", "San Juan Capistrano");
        jsonObject.put("zip", "92675");
        jsonObject.put("sys_id", "cust123");

        // Act
        CustomerImpl customer = CustomerImpl.create(jsonObject);

        // Assert
        assertEquals("San Juan Capistrano", customer.getCity());
    }
}

