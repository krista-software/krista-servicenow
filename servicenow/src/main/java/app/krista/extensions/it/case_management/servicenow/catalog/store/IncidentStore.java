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

package app.krista.extensions.it.case_management.servicenow.catalog.store;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.it.case_management.servicenow.catalog.entities.Incident;
import app.krista.extensions.it.case_management.servicenow.impl.IncidentDaoImpl;
import app.krista.extensions.it.case_management.servicenow.impl.UserDaoImpl;
import app.krista.extensions.it.case_management.servicenow.util.QueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public final class IncidentStore implements EntityStore<Incident> {
    private static final Logger LOG = LogManager.getLogger(IncidentStore.class.getName());
    public static final String SYSPARM_QUERY = "?sysparm_query";
    public static final String ORDERBY = "^ORDERBY";
    private final IncidentDaoImpl incidentDaoImpl;

    public static final String FAILED_TO_SEARCH_FOR_CAMPAIGN = "Failed to search for Campaign. ";

    public static final String ENTITY_INPUT_IS_NULL_OR_EMPTY = "Entity input is null or empty.";


    private final UserDaoImpl userDaoImpl;

    @Inject
    public IncidentStore(IncidentDaoImpl incidentDaoImpl, UserDaoImpl userDaoImpl) {
        this.incidentDaoImpl = incidentDaoImpl;
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public Incident create(Map<String, Object> fields) {
        Incident incident = new Incident(fields);
        if (incident.caller != null && !incident.caller.isBlank()) {
            incident.caller = userDaoImpl.getUserID(incident.caller);
        }
        Incident newIncident = Incident.create(incidentDaoImpl.createIncident(fields));
        LOG.info("Incident created successfully for incident id = " + newIncident.incidentId);
        return newIncident;
    }

    @Override
    public Incident get(String primaryKey) throws IOException {
        Incident newIncident = Incident.create(incidentDaoImpl.getIncidentBySysId(primaryKey));
        LOG.info("Incident data fetched successfully for id = " + primaryKey);
        return newIncident;
    }

    @Override
    public Incident update(Incident incident) {
        Incident newIncident = Incident.create(
                incidentDaoImpl.updateIncident(Incident.createServiceNowIncident(incident)));
        LOG.info("Incident updated successfully ");
        return newIncident;
    }

    @Override
    public void delete(String primaryKey) {
        //return void
    }

    @Override
    public boolean contains(String primaryKey) {
        boolean bool = incidentDaoImpl.getIncidentBySysId(primaryKey) != null;
        LOG.info((bool) ? "the incident data is present in system for id = " + primaryKey : "the incident data is not present in " +
                "system for id = " + primaryKey);
        return bool;
    }

    @Override
    public List<Incident> search(List<SearchCondition> conditions, long pageIndex, int pageSize)
            throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<Incident> search(SearchQuery searchQuery, long pageIndex, int pageSize)
            throws IOException {

        pageIndex = pageIndex - 1;
        return incidentDaoImpl.getIncidents(pageIndex, pageSize, searchQuery).stream()
                .map(Incident::create).collect(Collectors.toList());
    }

    @Override
    public long count(List<SearchCondition> conditions) {
        long newLong = incidentDaoImpl.getIncidents(null, 0, 1000, String.valueOf
                (QueryBuilder.serviceNowField.get("Impact")), "DESC").size();
        LOG.info("Success,In Incident,count Record fetched = " + newLong);
        return newLong;
    }

    @Override
    public List<String> lookup(Map<String, Object> fields, List<String> fieldName) {
        return Collections.emptyList();
    }

}
