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

package app.krista.extensions.it.case_management.servicenow.catalog.entities;

import app.krista.extension.impl.anno.Custom;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Entity;
import app.krista.extension.impl.anno.Field;
import app.krista.extension.impl.anno.Searchable;
import app.krista.extension.impl.anno.ToString;
import app.krista.extensions.it.case_management.servicenow.catalog.store.CustomerStore;
import app.krista.extensions.it.case_management.servicenow.impl.CustomerImpl;

/**
 * Entity representing a ServiceNow Customer Account.
 * <p>
 * This entity encapsulates customer account information from ServiceNow, including
 * customer details such as name, contact information, and location. Customer accounts
 * are used to track and manage customer relationships and service requests.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Get - Retrieve customer account by System ID</li>
 *   <li>Search - Search customer accounts by name, ID, phone, city, or zip code</li>
 *   <li>Delete - Delete customer accounts</li>
 * </ul>
 * </p>
 * <p>
 * <b>Searchable Fields:</b> Name, Customer ID, Phone, City, Zip Code
 * </p>
 *
 * @see CustomerStore
 * @see CustomerImpl
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
    name = "Case Management",
    ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
    ecosystemName = "IT",
    ecosystemVersion ="ec8e0ba3-0495-4861-8a01-8b31e8a7594b")
@Custom
@Entity(name = "Customer", id = "localDomainEntity_e34fb07c-e1bf-401a-9fc7-d66b6dbbafcd", primaryKey = "Sys Id", supportStore = false, options = {})
@Searchable
public class Customer {

  public static final String NAME = "Name";
  public static final String CUSTOMER_ID = "Customer Id";
  public static final String PHONE = "Phone";
  public static final String CITY = "City";
  public static final String ZIP_CODE = "Zip Code";
  public static final String SYS_ID = "Sys Id";

  @Searchable
  @ToString
  @Field(name = NAME, type = "Text")
  public String name;

  @Searchable
  @Field(name = CUSTOMER_ID, type = "Identifier", required = false)
  public String customerId;

  @Searchable
  @Field(name = PHONE, type = "Text", required = false)
  public String phone;

  @Searchable
  @ToString
  @Field(name = CITY, type = CITY, required = false)
  public String city;

  @Searchable
  @Field(name = ZIP_CODE, type = "PostalCode", required = false)
  public String zipCode;

  @Searchable
  @Field(name = SYS_ID, type = "Text", required = false)
  public String sysId;

  public static app.krista.extensions.it.case_management.servicenow.service.Customer createServiceNowCustomer(
      Customer snCustomer) {
    return new CustomerImpl(snCustomer.name, snCustomer.customerId, snCustomer.phone,
        snCustomer.city, snCustomer.zipCode,
        snCustomer.sysId);
  }

  public static Customer create(
      app.krista.extensions.it.case_management.servicenow.service.Customer snCustomer) {
    Customer newCustomer = new Customer();
    newCustomer.name = snCustomer.getName();
    newCustomer.customerId = snCustomer.getCustomerId();
    newCustomer.phone = snCustomer.getPhone();
    newCustomer.sysId = snCustomer.getSysId();
    newCustomer.zipCode = snCustomer.getZipCode();
    newCustomer.city = snCustomer.getCity();
    return newCustomer;
  }

}
