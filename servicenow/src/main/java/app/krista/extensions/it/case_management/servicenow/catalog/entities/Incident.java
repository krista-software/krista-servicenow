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
import app.krista.extensions.it.case_management.servicenow.catalog.store.IncidentStore;
import app.krista.extensions.it.case_management.servicenow.impl.IncidentImpl;
import java.util.Map;

/**
 * Entity representing a ServiceNow Incident.
 * <p>
 * This entity encapsulates all fields and properties of a ServiceNow incident,
 * including incident details, caller information, assignments, state, priority,
 * and resolution information. Incidents are used to track and manage IT service
 * disruptions and issues.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Create - Create new incidents</li>
 *   <li>Get - Retrieve incident by System ID</li>
 *   <li>Search - Search incidents by various criteria</li>
 *   <li>Update - Update incident fields</li>
 *   <li>Delete - Delete incidents</li>
 * </ul>
 * </p>
 * <p>
 * <b>Searchable Fields:</b> Incident ID, Incident State
 * </p>
 *
 * @see IncidentStore
 * @see IncidentImpl
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
    name = "Case Management",
    ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
    ecosystemName = "IT",
    ecosystemVersion ="d8919fea-e553-41a9-9b19-a7b19ce28f87")
@Custom
@Entity(name = "Incident", id = "localDomainEntity_03be097d-8c37-429d-99bf-e344993e7a82", primaryKey = "Sys Id", supportStore = false, options = {})
@Searchable
public class Incident {

  public static final String SYS_ID = "Sys Id";
  public static final String INCIDENT_ID = "Incident Id";
  public static final String CALLER = "Caller";
  public static final String ASSIGNED_TO = "Assigned To";
  public static final String STATE = "Incident State";
  public static final String SHORT_DESCRIPTION = "Short Description";
  public static final String DESCRIPTION = "Description";
  public static final String IMPACT = "Impact";
  public static final String URGENCY = "Urgency";

  @Field(name = SYS_ID, type = "Text", required = false)
  public String sysId;

  @Searchable
  @Field(name = INCIDENT_ID, type = "Text", required = false)
  public String incidentId;

  @Field(name = CALLER, type = "Text", required = false)
  public String caller;

  @Field(name = ASSIGNED_TO, type = "Text", required = false)
  public String assignedTo;

  @Searchable
  @Field.Desc(name = STATE, type = "PickOne(New|In Progress|On Hold|Resolved|Closed|Canceled)", required = false)
  public String incidentState;


  @Field(name = SHORT_DESCRIPTION, type = "Text", required = false)
  public String shortDescription;

  @Field(name = DESCRIPTION, type = "Paragraph", required = false)
  public String description;

  @Searchable
  @Field.Desc(name = IMPACT, type = "PickOne(High|Medium|Low)", required = false)
  public String impact;

  @Searchable
  @Field.Desc(name = URGENCY, type = "PickOne(High|Medium|Low)", required = false)
  public String urgency;

  public Incident(Map<String, Object> fields) {
    this.sysId = (String) fields.get(Incident.SYS_ID);
    this.incidentId = (String) fields.get(Incident.INCIDENT_ID);
    this.caller = (String) fields.get(Incident.CALLER);
    this.assignedTo = (String) fields.get(Incident.ASSIGNED_TO);
    this.incidentState = (String) fields.get(Incident.STATE);
    this.shortDescription = (String) fields.get(Incident.SHORT_DESCRIPTION);
    this.description = (String) fields.get(Incident.DESCRIPTION);
    this.impact = (String) fields.get(Incident.IMPACT);
    this.urgency = (String) fields.get(Incident.URGENCY);
  }

  public Incident() {
  }

  public static app.krista.extensions.it.case_management.servicenow.service.Incident createServiceNowIncident(
      Incident snIncident) {
    return new IncidentImpl(snIncident.sysId, snIncident.incidentId, snIncident.caller,
        snIncident.assignedTo,
        snIncident.incidentState, snIncident.shortDescription, snIncident.description,
        snIncident.impact, snIncident.urgency);
  }

  public static Incident create(
      app.krista.extensions.it.case_management.servicenow.service.Incident snIncident) {
    Incident newIncident = new Incident();
    newIncident.sysId = snIncident.getSysId();
    newIncident.incidentId = snIncident.getIncidentId();
    newIncident.caller = snIncident.getCaller();
    newIncident.assignedTo = snIncident.getAssignedTo();
    newIncident.incidentState = snIncident.getState();
    newIncident.description = snIncident.getDescription();
    newIncident.shortDescription = snIncident.getShortDescription();
    newIncident.impact = snIncident.getImpact();
    newIncident.urgency = snIncident.getUrgency();
    return newIncident;
  }

}
