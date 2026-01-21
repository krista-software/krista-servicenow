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

import app.krista.extensions.it.case_management.servicenow.service.Customer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class CustomerImpl implements Customer {

    private final String name;
    private final String customerId;
    private final String phone;
    private final String city;
    private final String zipCode;
    private final String sysId;

    public CustomerImpl(String name, String customerId, String phone, String city, String zipCode, String sysId) {
        this.name = name;
        this.customerId = customerId;
        this.phone = phone;
        this.city = city;
        this.zipCode = zipCode;
        this.sysId = sysId;
    }

    public static CustomerImpl create(JSONObject jsonCustomer) throws JSONException {
        String name = jsonCustomer.getString("name");
        String zipCode = jsonCustomer.getString("zip");
        String city = jsonCustomer.getString("city");
        String sysId = jsonCustomer.getString("sys_id");
        String customerId = jsonCustomer.getString("number");
        String phone = jsonCustomer.getString("phone");
        return new CustomerImpl(name, customerId, phone, city, zipCode, sysId);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String getSysId() {
        return sysId;
    }

}
