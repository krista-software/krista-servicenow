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
import app.krista.extensions.it.case_management.servicenow.catalog.store.CaseStore;
import app.krista.extensions.it.case_management.servicenow.impl.CaseImpl;
import java.util.Map;

/**
 * Entity representing a ServiceNow Customer Service Case.
 * <p>
 * This entity encapsulates all fields and properties of a ServiceNow customer service case,
 * including case details, customer information, channel, priority, state, and assignments.
 * Cases are used to track and manage customer service requests and issues.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Create - Create new customer service cases</li>
 *   <li>Get - Retrieve case by System ID</li>
 *   <li>Search - Search cases by various criteria</li>
 *   <li>Update - Update case fields</li>
 *   <li>Delete - Delete cases</li>
 * </ul>
 * </p>
 * <p>
 * <b>Searchable Fields:</b> Case ID, Channel, Priority
 * </p>
 *
 * @see CaseStore
 * @see CaseImpl
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
    name = "Case Management",
    ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
    ecosystemName = "IT",
    ecosystemVersion = "ff2953aa-8cef-4c61-b738-94d467908cfc")
@Entity(name = "Case", id = "localDomainEntity_d7a23dd1-3e73-4446-be7f-d5c66db8318f", primaryKey = "Sys Id", supportStore = false)
@Searchable
public class Case {

  public static final String CASE_ID = "Case Id";
  public static final String CHANNEL = "Channel";
  public static final String PRIORITY = "Priority";
  public static final String CUSTOMER = "Customer";
  public static final String ASSIGNED_TO = "Assigned To Sys Id";
  public static final String SHORT_DESCRIPTION = "Short Description";
  public static final String SYS_ID = "Sys Id";
  public static final String STATE = "State";
  @Searchable
  @ToString
  @Field(name = CASE_ID, type = "Identifier", required = false)
  public String caseId;

  @Searchable
  @ToString
  @Field.Desc(name = CHANNEL, type = "PickOne(Web|Phone|Email|Chat|Social|Community|Alert|Virtual Agent)", required = false)
  public String channel;

  @Searchable
  @ToString
  @Field.Desc(name = PRIORITY, type = "PickOne(Critical|High|Moderate|Low)", required = false)
  public String priority;

  @ToString
  @Field(name = CUSTOMER, type = "Identifier", required = false)
  public String customer;

  @ToString
  @Field(name = ASSIGNED_TO, type = "Identifier", required = false)
  public String assignedToSysId;

  @Searchable
  @ToString
  @Field(name = SHORT_DESCRIPTION, type = "Paragraph", required = false)
  public String shortDescription;

  @ToString
  @Field(name = SYS_ID, type = "Text", required = false)
  public String sysId;

  @Searchable
  @ToString
  @Field.Desc(name = STATE, type = "PickOne(New|Open|Awaiting Info|Resolved|Closed)", required = false)
  public String state;

  public Case(Map<String, Object> fields) {
    this.caseId = (String) fields.get(Case.CASE_ID);
    this.channel = (String) fields.get(Case.CHANNEL);
    this.priority = (String) fields.get(Case.PRIORITY);
    this.customer = (String) fields.get(Case.CUSTOMER);
    this.assignedToSysId = (String) fields.get(Case.ASSIGNED_TO);
    this.shortDescription = (String) fields.get(Case.SHORT_DESCRIPTION);
    this.sysId = (String) fields.get(Case.SYS_ID);
    this.state = (String) fields.get(Case.STATE);
  }

  public Case() {
  }

  /**
   * Converts exposed "Case" (entity) to logical "Case" object used in service classes * @param
   * snCase
   *
   * @return
   */
  public static app.krista.extensions.it.case_management.servicenow.service.Case createServiceNowCase(
      Case snCase) {
    return new CaseImpl(snCase.caseId, snCase.channel, snCase.priority, snCase.customer,
        snCase.assignedToSysId, snCase.shortDescription, snCase.sysId, snCase.state);
  }

  /**
   * Converts logical data layer's "Case" object into "Case" entity.
   *
   * @param snCase
   * @return
   */
  public static Case create(
      app.krista.extensions.it.case_management.servicenow.service.Case snCase) {
    Case newCase = new Case();
    newCase.caseId = snCase.getCaseId();
    newCase.channel = snCase.getChannel();
    newCase.priority = snCase.getPriority();
    newCase.customer = snCase.getAccountId();
    newCase.assignedToSysId = snCase.getAssignedTo();
    newCase.shortDescription = snCase.getShortDescription();
    newCase.sysId = snCase.getSysId();
    newCase.state = snCase.getState();
    return newCase;
  }

}
