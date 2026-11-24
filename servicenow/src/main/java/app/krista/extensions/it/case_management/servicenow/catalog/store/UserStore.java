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
import app.krista.extensions.it.case_management.servicenow.catalog.entities.User;
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
public final class UserStore implements EntityStore<User> {
    private static final Logger LOG = LogManager.getLogger(UserStore.class.getName());
    private final UserDaoImpl userDaoImpl;

    @Inject
    public UserStore(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    @Override
    public User create(Map<String, Object> fields) {
        return null;
    }

    @Override
    public User get(String primaryKey) {
        return User.create(userDaoImpl.getUserById(primaryKey));
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void delete(String sysID) {
        userDaoImpl.deleteUser(sysID);
    }

    @Override
    public boolean contains(String primaryKey) {
        return userDaoImpl.getUserById(primaryKey) != null;
    }

    @Override
    public List<User> search(List<SearchCondition> conditions, long pageIndex, int pageSize)
            throws IOException {
        return Collections.emptyList();
    }

    @Override
    public List<User> search(SearchQuery searchQuery, long pageIndex, int pageSize)
            throws IOException {
        pageIndex = pageIndex - 1;
        return userDaoImpl.getUsers(pageIndex, pageSize, searchQuery).stream().map(User::create)
                .collect(Collectors.toList());
    }

    @Override
    public long count(List<SearchCondition> conditions) {
        long newLong = userDaoImpl.getUsers(null, 0, 1000, String.valueOf
                (QueryBuilder.serviceNowField.get("Email")), "DESC").size();
        LOG.info("Success,In USER,count Record fetched = " + newLong);
        return newLong;
    }

    @Override
    public List<String> lookup(Map<String, Object> fields, List<String> fieldName) {
        List<String> newString = List.of();
        LOG.info("User lookup function successfully executed");
        return newString;
    }

}
