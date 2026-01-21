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
import java.util.List;
import java.util.Map;

/**
 * The interface defines communication flow for the ServiceNow case API
 * https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/case-api.html
 */
public interface CaseDao {

    /**
     * https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/case-api.html#title_case-GET-case-id
     *
     * @param id
     * @return
     */
    Case getCaseById(String id);

    /**
     *
     *
     * @return
     */
    List<Case> getCaseList(String url, long pageIndex , int pageSize,String operand , String operator);
    /**
     * https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/case-api.html#title_case-POST-case
     *
     * @param map
     * @return
     */
    Case createCase(Map<String, Object> map);


    /**
     * https://docs.servicenow.com/bundle/tokyo-application-development/page/integrate/inbound-rest/concept/case-api.html#title_case-PUT-case
     *
     * @param caseToUpdate
     * @return
     */
    Case updateCase(Case caseToUpdate);



    List<Case> getCaseList(long pageIndex, int pageSize, SearchQuery searchQuery );

}
