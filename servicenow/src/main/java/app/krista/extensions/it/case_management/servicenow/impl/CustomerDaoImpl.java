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

package app.krista.extensions.it.case_management.servicenow.impl;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extensions.it.case_management.servicenow.constants.Errors;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthService;
import app.krista.extensions.it.case_management.servicenow.service.Customer;
import app.krista.extensions.it.case_management.servicenow.service.CustomerDao;
import app.krista.extensions.it.case_management.servicenow.util.QueryBuilder;
import app.krista.extensions.it.case_management.servicenow.util.ServiceNowUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of {@link CustomerDao} for ServiceNow customer account operations.
 * <p>
 * This class provides methods to interact with ServiceNow customer account records,
 * including retrieving, searching, and deleting customer accounts through the
 * ServiceNow REST API.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Get customer account by System ID</li>
 *   <li>Search customer accounts with pagination</li>
 *   <li>Delete customer account</li>
 * </ul>
 * </p>
 *
 * @see CustomerDao
 * @see Customer
 */
@Service
public class CustomerDaoImpl implements CustomerDao {


    public static final String CUSTOMER_ACCOUNT = "customer_account/";
    private final AuthService authService;
    private static final Logger LOG = LogManager.getLogger(CustomerDaoImpl.class.getName());

    @Inject
    public CustomerDaoImpl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Retrieves a list of customer accounts from ServiceNow with pagination and sorting.
     * <p>
     * This method fetches customer accounts from the ServiceNow customer_account table.
     * Results can be paginated and sorted by a specified field.
     * </p>
     *
     * @param url the custom URL to use for the request, or null to use the default endpoint
     * @param pageIndex the zero-based page index for pagination
     * @param pageSize the number of records to return per page (max 1000)
     * @param operand the field name to sort by, or null for no sorting
     * @param operator the sort order ("ASC" or "DESC"), or null for default
     * @return a list of {@link Customer} objects matching the criteria
     * @throws IllegalArgumentException if an error occurs while retrieving customer accounts
     */
    @Override
    public List<Customer> getCustomerAccounts(String url, long pageIndex, int pageSize, String operand, String operator) {
        try {
            String endpointRaw = "/api/now/v1/table/" + "customer_account" + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;
            endpointRaw = ServiceNowUtil.addOrderBy(operand) ? endpointRaw + ServiceNowConstants.ORDERBY + operand + operator : endpointRaw;

            String endPoint = (url == null) ? endpointRaw : url;
            JSONObject jsonResponse =
                    authService.getRestClient().get(endPoint, new HashMap<>());
            return getCustomerList(jsonResponse.getJSONArray(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            throw new IllegalArgumentException(Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNTS, cause);
        }
    }


    /**
     * Searches for customer accounts based on specified search criteria.
     * <p>
     * This method allows searching customer accounts using various search conditions
     * such as name, customer ID, phone, city, or zip code. Results are paginated
     * and can be sorted based on query clauses.
     * </p>
     *
     * @param pageIndex the zero-based page index for pagination
     * @param pageSize the number of records to return per page (max 1000)
     * @param searchQuery the search query containing search conditions and sorting clauses
     * @return a list of {@link Customer} objects matching the search criteria
     * @throws IllegalArgumentException if search conditions are null or empty
     */
    @Override
    public List<Customer> getCustomerAccounts(long pageIndex, int pageSize,
                                              SearchQuery searchQuery) {
        List<Customer> customers = new ArrayList<>();
        StringBuilder finalQuery = new StringBuilder();
        String rootUrl = ServiceNowUtil.getTableURL("customer_account");
        List<SearchCondition> searchConditions = searchQuery.getSearchConditions();
        List<QueryClause> queryClauses = searchQuery.getQueryClauses();

        if (searchConditions == null || searchConditions.isEmpty()) {
            throw new IllegalArgumentException(ServiceNowConstants.FAILED_TO_SEARCH_FOR_CAMPAIGN + ServiceNowConstants.ENTITY_INPUT_IS_NULL_OR_EMPTY);
        }

        String operand = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
                QueryBuilder.serviceNowField.get(queryClauses.get(0).getFieldName())) : null;
        String operator = (queryClauses != null && !queryClauses.isEmpty()) ? String.valueOf(
                queryClauses.get(0).getOperand()) : ServiceNowConstants.DESC;

        finalQuery.append(ServiceNowConstants.SYSPARM_QUERY);

        if (searchConditions.size() == 1 && customers.isEmpty()) {
            return getCustomerAccounts(null, pageIndex, pageSize, operand, operator);
        }
        int count = 0;
        for (SearchCondition condition : searchConditions) {
            if (count == 0) {
                count++;
                continue;
            }
            String eachQuery = QueryBuilder.buildQuery(condition.getFieldName(), String.valueOf(condition.getOperator()), String.valueOf(condition.getOperand()));
            finalQuery.append(eachQuery);
        }

        if (String.valueOf(finalQuery).endsWith("AND")) {
            finalQuery = new StringBuilder(finalQuery.substring(0, finalQuery.length() - 4));
        }
        if (ServiceNowUtil.addOrderBy(operand)) {
            finalQuery.append(ServiceNowConstants.ORDERBY + operand + operator);
        }

        String url = String.valueOf(finalQuery).equals(ServiceNowConstants.SYSPARM_QUERY) ? rootUrl + ServiceNowConstants.SYSPARM_LIMIT_2 + pageSize
                + ServiceNowConstants.SYSPARM_LIMIT + pageIndex
                : rootUrl + String.valueOf(finalQuery) + ServiceNowConstants.SYSPARM_LIMIT + pageSize + ServiceNowConstants.SYSPARM_OFFSET + pageIndex;


        customers = getCustomerAccounts(url, 0, 0, null, null);
        return customers;
    }

    /**
     * Retrieves a customer account by its System ID.
     * <p>
     * This method fetches a single customer account record from ServiceNow
     * using the unique System ID (sys_id).
     * </p>
     *
     * @param id the System ID (sys_id) of the customer account to retrieve
     * @return the {@link Customer} object with the specified System ID
     * @throws IllegalArgumentException if the customer account cannot be retrieved or doesn't exist
     */
    @Override
    public Customer getAccountById(String id) {
        JSONObject jsonAccount = authService.getRestClient()
                .get(ServiceNowUtil.getTableURL(CUSTOMER_ACCOUNT) + id, new HashMap<>());
        try {
            return CustomerImpl.create(jsonAccount.getJSONObject(ServiceNowConstants.RESULT));
        } catch (JSONException cause) {
            throw new IllegalArgumentException(
                    Errors.ERROR_OCCURRED_WHILE_GETTING_CUSTOMER_ACCOUNT_FOR_THE_SYS_ID, cause);
        }
    }

    /**
     * Deletes a customer account from ServiceNow.
     * <p>
     * This method permanently deletes a customer account record from ServiceNow
     * using the System ID. This operation cannot be undone.
     * </p>
     *
     * @param id the System ID (sys_id) of the customer account to delete
     */
    @Override
    public void deleteCustomer(String id) {
        authService.getRestClient().delete(ServiceNowUtil.getTableURL(CUSTOMER_ACCOUNT) + id, new HashMap<>());
        LOG.info("Customer deleted successful");
    }

    /**
     * Converts a JSON array of customer records to a list of Customer objects.
     *
     * @param jsonArray the JSON array containing customer records
     * @return a list of {@link Customer} objects
     * @throws JSONException if JSON parsing fails
     */
    private List<Customer> getCustomerList(JSONArray jsonArray) throws JSONException {
        List<Customer> allUsers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonRequest = jsonArray.getJSONObject(i);
            allUsers.add(CustomerImpl.create(jsonRequest));
        }
        return allUsers;
    }

}
