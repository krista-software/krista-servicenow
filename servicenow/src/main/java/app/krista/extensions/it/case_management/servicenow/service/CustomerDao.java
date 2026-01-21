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
import java.util.List;

/**
 * Data Access Object (DAO) interface for ServiceNow customer account operations.
 * <p>
 * This interface defines methods for interacting with ServiceNow customer account records,
 * providing capabilities to retrieve, search, and delete customer accounts through the
 * ServiceNow REST API.
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Retrieve customer accounts with pagination and sorting</li>
 *   <li>Search customer accounts using various criteria</li>
 *   <li>Get a specific customer account by System ID</li>
 *   <li>Delete a customer account</li>
 * </ul>
 * </p>
 *
 * @see Customer
 */
public interface CustomerDao {

    /**
     * Retrieves a list of customer accounts with pagination and optional sorting.
     *
     * @param url the custom URL to use, or null to use the default endpoint
     * @param pageIndex the zero-based page index for pagination
     * @param pageSize the number of records to return per page (max 1000)
     * @param operand the field name to sort by, or null for no sorting
     * @param operator the sort order ("ASC" or "DESC"), or null for default
     * @return a list of customer accounts matching the criteria
     */
    List<Customer> getCustomerAccounts(String url, long pageIndex , int pageSize,String operand, String operator);

    /**
     * Searches for customer accounts based on specified search criteria.
     *
     * @param pageIndex the zero-based page index for pagination
     * @param pageSize the number of records to return per page (max 1000)
     * @param searchQuery the search query containing search conditions and sorting clauses
     * @return a list of customer accounts matching the search criteria
     */
    List<Customer> getCustomerAccounts(long pageIndex, int pageSize, SearchQuery searchQuery );

    /**
     * Retrieves a customer account by its System ID.
     *
     * @param id the System ID (sys_id) of the customer account
     * @return the customer account with the specified System ID
     */
    Customer getAccountById(String id);

    /**
     * Deletes a customer account from ServiceNow.
     *
     * @param id the System ID (sys_id) of the customer account to delete
     */
    void deleteCustomer(String id);

}
