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
import app.krista.extensions.it.case_management.servicenow.catalog.entities.Customer;
import app.krista.extensions.it.case_management.servicenow.impl.CustomerDaoImpl;
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
public final class CustomerStore implements EntityStore<Customer> {
    private static final Logger LOG = LogManager.getLogger(CustomerStore.class.getName());
    private final CustomerDaoImpl customerDaoImpl;

    @Inject
    public CustomerStore(CustomerDaoImpl customerDaoImpl) {
        this.customerDaoImpl = customerDaoImpl;
    }

    @Override
    public Customer create(Map<String, Object> fields) {
        return null;
    }

    @Override
    public Customer get(String primaryKey) {
        Customer newCustomer = Customer.create(customerDaoImpl.getAccountById(primaryKey));
        LOG.info("Customer data fetched successfully for id = " + primaryKey);
        return newCustomer;
    }

    @Override
    public Customer update(Customer entity) {
        return null;
    }

    @Override
    public void delete(String sysID) {
        customerDaoImpl.deleteCustomer(sysID);
    }

    @Override
    public boolean contains(String primaryKey) {
        boolean bool = customerDaoImpl.getAccountById(primaryKey) != null;
        LOG.info((bool) ? "the Customer data is present in system for id = " + primaryKey : "the Customer data is not present in " +
                "system for id = " + primaryKey);
        return bool;
    }

    @Override
    public List<Customer> search(List<SearchCondition> conditions, long pageIndex, int pageSize)
            throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<Customer> search(SearchQuery searchQuery, long pageIndex, int pageSize)
            throws IOException {

        pageIndex = pageIndex - 1;
        return customerDaoImpl.getCustomerAccounts(pageIndex, pageSize, searchQuery).stream()
                .map(Customer::create).collect(Collectors.toList());
    }

    @Override
    public long count(List<SearchCondition> conditions) {
        long newLong = customerDaoImpl.getCustomerAccounts(null, 0, 1000, String.valueOf
                (QueryBuilder.serviceNowField.get("Priority")), "DESC").size();
        LOG.info("Success,In Customer,count Record fetched = " + newLong);
        return newLong;
    }

    @Override
    public List<String> lookup(Map<String, Object> fields, List<String> fieldName) {
        List<String> newString = List.of();
        LOG.info("Customer lookup function successfully executed");
        return newString;
    }

}
