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

package app.krista.extensions.it.case_management.servicenow.catalog;

import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.it.case_management.servicenow.catalog.entities.ChangeRequest;
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.impl.ChangeRequestDaoImpl;
import app.krista.model.field.util.FieldValues;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Change Management catalog area for ServiceNow extension.
 * <p>
 * This class provides catalog requests for managing ServiceNow change requests,
 * including escalation, status checking, and approval operations. All operations
 * are performed through the ServiceNow Change Management API.
 * </p>
 * <p>
 * <b>Available Catalog Requests:</b>
 * <ul>
 *   <li>Escalate Issue - Assigns change request to assignment group manager</li>
 *   <li>Check Status - Retrieves current status of a change request</li>
 *   <li>Approve Change Request - Approves change request as current user</li>
 * </ul>
 * </p>
 *
 * @see ChangeRequest
 * @see ChangeRequestDaoImpl
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
        name = "Case Management",
        ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
        ecosystemName = "IT",
        ecosystemVersion = "ca80013f-3de0-4e0d-bbdc-4634f8fe080d")
public class ChangeManagementArea {

    private final ChangeRequestDaoImpl changeRequestDaoImpl;
    private static final Logger LOG = LogManager.getLogger(ChangeManagementArea.class.getName());

    @Inject
    public ChangeManagementArea(ChangeRequestDaoImpl changeRequestDaoImpl) {
        this.changeRequestDaoImpl = changeRequestDaoImpl;
    }

    /**
     * Escalates a change request to the manager of its assignment group.
     * <p>
     * This catalog request assigns the specified change request to the manager
     * of the current assignment group. This is useful when a change request
     * requires management approval or oversight.
     * </p>
     *
     * @param requestID the Change Request number (e.g., CHG0030001), not the System ID
     * @return the updated {@link ChangeRequest} entity with the new assignment
     * @throws IllegalArgumentException if the request ID is invalid or change request doesn't exist
     */
    @CatalogRequest(
            id = "localDomainRequest_19b682cb-ab62-4e44-9cd2-d39d5185c40e",
            name = "Escalate issue",
            description = "Assigns the 'Change Request' to the manager of the assignment group.",
            area = "Change Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Change Request", type = "Entity(Change Request)", required = false)
    public ChangeRequest escalateIssue(
            @Field(name = "Request ID", type = "Text") String requestID) {
        requestID = FieldValues.toString(Constants.textField, requestID);
        ChangeRequest newChangeRequest = ChangeRequest.create(changeRequestDaoImpl.escalateChangeRequest(changeRequestDaoImpl.
                getChangeRequestByRequestID(requestID).getSysId()));
        LOG.info("Escalate issue executed successfully for " + requestID);
        return newChangeRequest;
    }

    /**
     * Retrieves the current status and details of a change request.
     * <p>
     * This catalog request is a read-only operation that fetches the complete
     * information about a change request, including its current state, priority,
     * assignments, and approval status.
     * </p>
     *
     * @param requestID the Change Request number (e.g., CHG0030001), not the System ID
     * @return the {@link ChangeRequest} entity with current status and details
     * @throws IllegalArgumentException if the request ID is invalid or change request doesn't exist
     */
    @CatalogRequest(
            id = "localDomainRequest_07d51e76-7757-4a02-80c5-5f29e0278d14",
            name = "Check Status",
            description = "Accepts the Request ID of the 'Change Request' and returns the current status.",
            area = "Change Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Change Request", type = "Entity(Change Request)", required = false)
    public ChangeRequest checkStatus(
            @Field(name = "Request ID", type = "Text") String requestID) {
        requestID = FieldValues.toString(Constants.textField, requestID);
        ChangeRequest newChangeRequest = ChangeRequest.create(changeRequestDaoImpl.getChangeRequestByRequestID(requestID));
        LOG.info("Check Status executed successfully for " + requestID);
        return newChangeRequest;
    }

    /**
     * Approves a change request as the currently authenticated user.
     * <p>
     * This catalog request allows the currently logged-in user to approve
     * a change request. The user must have appropriate permissions in ServiceNow
     * to approve change requests.
     * </p>
     *
     * @param requestID the Change Request number (e.g., CHG0030001), not the System ID
     * @return the updated {@link ChangeRequest} entity with approval status
     * @throws IllegalArgumentException if the request ID is invalid, change request doesn't exist,
     *         or user doesn't have permission to approve
     */
    @CatalogRequest(
            id = "localDomainRequest_d27e1744-5a22-44b4-9f09-189c83816054",
            name = "Approve Change Request",
            description = "Currently logged in user approves the change request matching to the input Request ID.",
            area = "Change Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Change Request", type = "Entity(Change Request)", required = false)
    public ChangeRequest approveChangeRequest(
            @Field(name = "Request ID", type = "Text") String requestID) {
        requestID = FieldValues.toString(Constants.textField, requestID);
        ChangeRequest newChangeRequest = ChangeRequest.create(
                changeRequestDaoImpl.approveChangeRequest(changeRequestDaoImpl.
                        getChangeRequestByRequestID(requestID).getSysId()));
        LOG.info("Approve Change Request executed successfully for " + requestID);
        return newChangeRequest;
    }

}
