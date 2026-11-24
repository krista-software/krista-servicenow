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

package app.krista.extensions.it.case_management.servicenow.service;

import app.krista.extension.executor.SearchQuery;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import java.util.List;
import java.util.Map;

public interface IncidentDao {


    /**
     *
     * https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/task/t_GetStartedRetrieveExisting.html
     * @return
     */
    List<Incident> getIncidents(long pageIndex, int pageSize, SearchQuery searchQuery );

    List<Incident> getIncidents(String url, long pageIndex, int pageSize,String operand, String operator);

    Incident getIncidentBySysId(String sysId);

    Incident getIncidentByIncidentId(String number);

    Incident createIncident(Map<String, Object> map);

    Incident updateIncident(Incident incident);

    boolean assignUserToIncident(String sysIdUser, String sysIdIncident);

}
