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
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.impl.IncidentDaoImpl;
import app.krista.model.field.util.FieldValues;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Ticket Management catalog area for ServiceNow extension.
 * <p>
 * This class provides catalog requests for managing ServiceNow incidents,
 * including assignment operations. All operations are performed through
 * the ServiceNow Incident Management API.
 * </p>
 * <p>
 * <b>Available Catalog Requests:</b>
 * <ul>
 *   <li>Assign User to Incident - Assigns an incident to a specific user</li>
 * </ul>
 * </p>
 *
 * @see app.krista.extensions.it.case_management.servicenow.catalog.entities.Incident
 * @see IncidentDaoImpl
 */
@Domain(id = "catEntryDomain_d237315e-3339-48c7-b28a-179ef38a3a7d",
        name = "Case Management",
        ecosystemId = "catEntryEcosystem_f7baeeb8-5cbe-4c4c-b0f3-5f2891b36d9d",
        ecosystemName = "IT",
        ecosystemVersion = "4992d475-aaf4-4e44-8972-d9ae6a038315")
public class TicketManagementArea {

    private final IncidentDaoImpl incidentDaoImpl;
    private static final Logger LOG = LogManager.getLogger(TicketManagementArea.class.getName());

    @Inject
    public TicketManagementArea(IncidentDaoImpl incidentDaoImpl) {
        this.incidentDaoImpl = incidentDaoImpl;
    }

    /**
     * Assigns an incident to a specific user.
     * <p>
     * This catalog request assigns the specified incident to the specified user
     * by updating the "Assigned To" field in ServiceNow. The user must exist in
     * ServiceNow and have appropriate permissions to be assigned incidents.
     * </p>
     *
     * @param sysIDUser the System ID (sys_id) of the user to assign the incident to
     * @param sysIDIncident the System ID (sys_id) of the incident to assign
     * @return true if the assignment was successful, false otherwise
     * @throws IllegalArgumentException if either System ID is invalid or doesn't exist
     */
    @CatalogRequest(
            id = "localDomainRequest_813da8c1-80e7-44d4-87c5-b9294dc3b814",
            name = "Assign user to incident",
            description = "Accepts system user ID, incident ID and assigns the incident to the user",
            area = "Ticket Management",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Is Successful", type = "Switch", required = false)
    public Boolean assignUserToIncident(
            @Field(name = "Sys ID user", type = "Text") String sysIDUser,
            @Field(name = "Sys ID Incident", type = "Text") String sysIDIncident) {
        sysIDUser = FieldValues.toString(Constants.textField, sysIDUser);
        sysIDIncident = FieldValues.toString(Constants.textField, sysIDIncident);

        Boolean bool = incidentDaoImpl.assignUserToIncident(sysIDUser, sysIDIncident);
        LOG.info("Assign to user request executed successfully for system ID user " + sysIDUser + "and system ID Incident"
                + sysIDIncident);
        return bool;
    }

}
