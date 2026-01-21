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

import app.krista.extensions.it.case_management.servicenow.service.User;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class UserImpl implements User {

    public static final String SYS_ID = "sys_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    private final String sysId;
    private final String firstName;
    private final String lastName;
    private final String emailId;

    public UserImpl(String sysId, String firstName, String lastName, String emailId) {
        this.sysId = sysId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public static UserImpl create(JSONObject jsonUser) throws JSONException {
        String sysId = jsonUser.getString(SYS_ID);
        String firstName = jsonUser.getString(FIRST_NAME);
        String lastName = jsonUser.getString(LAST_NAME);
        String emailId = jsonUser.getString(EMAIL);
        return new UserImpl(sysId, firstName, lastName, emailId);
    }

    public static String findUserSysID(JSONObject jsonRequest) throws JSONException {
        return String.valueOf(jsonRequest.get(SYS_ID));
    }

    @Override
    public String getSysId() {
        return sysId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmailId() {
        return emailId;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }

}
