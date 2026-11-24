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

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ChangeRequestDao {

    ChangeRequest createChangeRequest(Map<String, Object> map);

    ChangeRequest moveToAssess(String id);

    ChangeRequest escalateChangeRequest(String id);

    ChangeRequest getChangeRequestByRequestID(String id);

    ChangeRequest getChangeRequestBySysID(String id);

    ChangeRequest approveChangeRequest(String id);

    List<ChangeRequest> getRequests(HashMap<String, Object> filters);

    List<ChangeRequest> getChangeRequests(long pageIndex, int pageSize, SearchQuery searchQuery );


    void deleteChangeRequest(String id);


//    /**
//     * https://developer.servicenow.com/dev.do#!/reference/api/sandiego/rest/change-management-api#change-DELETE-normal
//     * @param id
//     * @return
//     */
//    boolean deleteChangeRequest(String id);


}
