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
import app.krista.extensions.it.case_management.servicenow.catalog.entities.Case;
import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.impl.CaseDaoImpl;
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
public final class CaseStore implements EntityStore<Case> {

    private final CaseDaoImpl caseDaoImpl;
    private static final Logger LOG = LogManager.getLogger(CaseStore.class.getName());

    @Inject
    public CaseStore(CaseDaoImpl caseDaoImpl) {
        this.caseDaoImpl = caseDaoImpl;
    }

    @Override
    public Case create(Map<String, Object> fields) {
        Case newCase = Case.create(caseDaoImpl.createCase(fields));
        LOG.info("Case created successfully for id = " + newCase.caseId);
        return newCase;
    }

    @Override
    public Case get(String sysId) throws IOException {
        Case newCase = Case.create(caseDaoImpl.getCaseById(sysId));
        LOG.info("Case data successfully fetched for id = " + sysId);
        return newCase;
    }

    @Override
    public Case update(Case entity) throws IOException {
        Case newCase = Case.create(caseDaoImpl.updateCase(Case.createServiceNowCase(entity)));
        LOG.info("Case update successfully executed for case id = " + entity.caseId);
        return newCase;
    }

    @Override
    public void delete(String primaryKey) {
        // return void
    }

    @Override
    public boolean contains(String primaryKey) throws IOException {
        boolean bool = caseDaoImpl.getCaseList(null, 0, 1000, String.valueOf(QueryBuilder.serviceNowField.get(
                        Constants.PRIORITY)), "DESC").stream()
                .noneMatch(c -> c.getSysId().toLowerCase().contains(primaryKey));
        LOG.info((bool) ? "the case data is present in system for id = " + primaryKey : "the case data is not present in " +
                "system for id = " + primaryKey);
        return bool;
    }

    @Override
    public List<Case> search(List<SearchCondition> conditions, long pageIndex, int pageSize)
            throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<Case> search(SearchQuery searchQuery, long pageIndex, int pageSize)
            throws IOException {
        pageIndex = pageIndex - 1;
        return caseDaoImpl.getCaseList(pageIndex, pageSize, searchQuery).stream()
                .map(Case::create).collect(Collectors.toList());
    }

    @Override
    public long count(List<SearchCondition> conditions) throws IOException {
        long newLong = caseDaoImpl.getCaseList(null, 0, 1000, String.valueOf(QueryBuilder.serviceNowField.get(
                Constants.PRIORITY)), "DESC").size();
        LOG.info("Success,In Case,count Record fetched = " + newLong);
        return newLong;
    }

    /**
     * Performs a lookup operation on a set of fields using the provided field names.
     *
     * This method takes a map of fields and a list of field names as input and returns a list of strings as the result.
     * This function is used to dynamically fetch pick or pick list for the pick option in the extension.
     * it is sending empty list as if it returned nothing or null then extension will fail
     *
     * @param fields     A map of fields where the keys are field names, and the values are objects.
     * @param fieldNames A list of field names to look up in the fields map.
     * @return A list of strings containing the values of the fields found in the fields map. If no matching fields are found,
     *         an empty list is returned.
     */
    @Override
    public List<String> lookup(Map<String, Object> fields, List<String> fieldName) {
        return Collections.emptyList();
    }

}
