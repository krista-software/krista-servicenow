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

import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Entity;
import app.krista.extension.impl.anno.Field;
import app.krista.extension.impl.anno.Searchable;
import app.krista.extension.impl.anno.ToString;
import app.krista.extensions.it.case_management.servicenow.catalog.store.UserStore;

/**
 * Entity representing a ServiceNow User.
 * <p>
 * This entity encapsulates user information from ServiceNow, including basic
 * profile details such as name and email. Users can be assigned to incidents,
 * change requests, and cases.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Get - Retrieve user by System ID</li>
 *   <li>Search - Search users by name or email</li>
 * </ul>
 * </p>
 * <p>
 * <b>Searchable Fields:</b> First Name, Last Name, Email
 * </p>
 *
 * @see UserStore
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
    name = "Case Management",
    ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
    ecosystemName = "IT",
    ecosystemVersion = "cd304bcb-2b9b-408f-9b7b-d5ff98674ae4")
@Entity(name = "User", id = "localDomainEntity_b0ec2999-2dc6-4fa0-b2ba-d9655f65e8b0", primaryKey = "Sys Id", supportStore = false)
@Searchable
public class User {

  public static final String LAST_NAME = "Last Name";
  public static final String EMAIL = "Email";
  public static final String FIRST_NAME = "First Name";

  @Field(name = "Sys Id", type = "Text")
  public String sysId;

  @Searchable
  @ToString
  @Field(name = FIRST_NAME, type = "Text")
  public String firstName;

  @Searchable
  @ToString
  @Field(name = LAST_NAME, type = "Text")
  public String lastName;

  @Searchable
  @ToString
  @Field(name = EMAIL, type = EMAIL)
  public String email;

  /**
   * Creates a User entity from a service User object.
   * <p>
   * This factory method converts a service layer User object to a catalog
   * entity User object, mapping all relevant fields.
   * </p>
   *
   * @param user the service layer User object to convert
   * @return a new User entity with fields populated from the service object
   */
  public static User create(app.krista.extensions.it.case_management.servicenow.service.User user) {
    User newUser = new User();
    newUser.sysId = user.getSysId();
    newUser.firstName = user.getFirstName();
    newUser.lastName = user.getLastName();
    newUser.email = user.getEmailId();
    return newUser;
  }
}
