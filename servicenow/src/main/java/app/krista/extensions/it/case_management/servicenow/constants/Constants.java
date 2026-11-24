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

package app.krista.extensions.it.case_management.servicenow.constants;

import java.util.Map;

public class Constants {


    public static final String MESSAGE = "message";
    public static final String PRIORITY = "Priority";
    public static final String SYSPARM_QUERY = "sysparm_query";
    public static final String SHORT_DESCRIPTION = "short_description";
    public static final String ACCOUNT = "account";
    public static final String STATE = "state";
    public static final String ASSIGNMENT_GROUP = "Assignment group";
    public static final String CREATE_CHANGE_REQUEST_IS_CREATED_SUCCESSFULLY_WITH_STATE_NEW = "Create change request is created successfully with state New";



    public static final app.krista.model.field.Field textField = new app.krista.model.field.Field("com.krista.fields.Text", Map.of(), Map.of());


    public enum OrderBy {
        ASC,
        DSC
    }


    public enum AuthType {
        OAUTH,
        BASIC,
        API_TOKEN
    }
}
