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
import app.krista.extensions.it.case_management.servicenow.catalog.store.ChangeRequestStore;
import app.krista.extensions.it.case_management.servicenow.impl.ChangeRequestImpl;

/**
 * Entity representing a ServiceNow Change Request.
 * <p>
 * This entity encapsulates all fields and properties of a ServiceNow change request,
 * including request details, priority, risk assessment, state, assignments, and approvals.
 * Change requests are used to manage and track changes to IT infrastructure and services.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Create - Create new change requests</li>
 *   <li>Get - Retrieve change request by System ID</li>
 *   <li>Search - Search change requests by various criteria</li>
 *   <li>Delete - Delete change requests</li>
 * </ul>
 * </p>
 * <p>
 * <b>Searchable Fields:</b> Request ID, Priority, Short Description, State, Sys ID,
 * Assigned To, Assignment Group, Requested By
 * </p>
 *
 * @see ChangeRequestStore
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
    name = "Case Management",
    ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
    ecosystemName = "IT",
    ecosystemVersion = "4d17d380-bdce-46d4-b49b-64a1660cd74e")
@Entity(name = "Change Request", id = "localDomainEntity_9a77dd4a-9047-4c30-b4c9-0dd8ba98736e", primaryKey = "Sys ID", supportStore = false)
@Searchable
public class ChangeRequest {

  public static final String REQUEST_ID = "Request ID";
  public static final String PRIORITY = "Priority";
  public static final String RISK = "Risk";
  public static final String SHORT_DESCRIPTION = "Short Description";
  public static final String MODEL = "Model";
  public static final String IMPACT = "Impact";
  public static final String STATE = "State";
  public static final String SYS_ID = "Sys ID";
  public static final String DESCRIPTION = "Description";
  public static final String APPROVAL = "Approval";
  public static final String ASSIGNED_TO = "Assigned to";
  public static final String ASSIGNMENT_GROUP = "Assignment group";
  public static final String REQUESTED_BY = "Requested By";

  @Searchable
  @ToString
  @Field(name = REQUEST_ID, type = "Text", required = false)
  public String requestID;

  @Searchable
  @Field.Desc(name = PRIORITY, type = "PickOne(Low|Moderate|High|Critical)", required = false)
  public String priority;

  @Field.Desc(name = RISK, type = "PickOne(Low|Moderate|High)", required = false)
  public String risk;

  @Searchable
  @ToString
  @Field(name = SHORT_DESCRIPTION, type = "Text", required = false)
  public String shortDescription;

  @Field(name = MODEL, type = "Text", required = false)
  public String model;

  @Field.Desc(name = IMPACT, type = "PickOne(Low|Medium|High)", required = false)
  public String impact;

  @Searchable
  @Field(name = STATE, type = "Text", required = false)
  public String state;

  @Searchable
  @Field(name = SYS_ID, type = "Text", required = false)
  public String sysID;

  @Field(name = DESCRIPTION, type = "Text", required = false)
  public String description;

  @Searchable
  @Field(name = ASSIGNED_TO, type = "Text", required = false)
  public String assignedTo;

  @Searchable
  @Field(name = ASSIGNMENT_GROUP, type = "Text", required = false)
  public String assignmentGroup;

  @Searchable
  @ToString
  @Field(name = REQUESTED_BY, type = "Text", required = false)
  public String requestedBy;

  @Field.Text(name = "Approval", required = false, attributes = {}, options = {})
  public String approval;

  public static app.krista.extensions.it.case_management.servicenow.service.ChangeRequest createServiceNowChangeRequest(
      ChangeRequest snChangeRequest) {
    return new ChangeRequestImpl(snChangeRequest.sysID, snChangeRequest.requestID,
        snChangeRequest.state, snChangeRequest.shortDescription, snChangeRequest.description,
        snChangeRequest.risk, snChangeRequest.priority, snChangeRequest.impact,
        snChangeRequest.assignmentGroup, snChangeRequest.assignedTo,
        snChangeRequest.model, snChangeRequest.requestedBy, snChangeRequest.approval);
  }

  public static ChangeRequest create(
      app.krista.extensions.it.case_management.servicenow.service.ChangeRequest snChangeRequest) {
    ChangeRequest newChangeRequest = new ChangeRequest();
    newChangeRequest.requestID = snChangeRequest.getRequestId();
    newChangeRequest.risk = snChangeRequest.getRisk();
    newChangeRequest.priority = snChangeRequest.getPriority();
    newChangeRequest.model = snChangeRequest.getModel();
    newChangeRequest.impact = snChangeRequest.getImpact();
    newChangeRequest.state = snChangeRequest.getState();
    newChangeRequest.assignedTo = snChangeRequest.getAssignedTo();
    newChangeRequest.shortDescription = snChangeRequest.getShortDescription();
    newChangeRequest.sysID = snChangeRequest.getSysId();
    newChangeRequest.state = snChangeRequest.getState();
    newChangeRequest.description = snChangeRequest.getDescription();
    newChangeRequest.assignmentGroup = snChangeRequest.getAssignmentGroup();
    newChangeRequest.approval = snChangeRequest.getApproval();
    newChangeRequest.requestedBy= snChangeRequest.getRequestedBy();
    return newChangeRequest;
  }

}
