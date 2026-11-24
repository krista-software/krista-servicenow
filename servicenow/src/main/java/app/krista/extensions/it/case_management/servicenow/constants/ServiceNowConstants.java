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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.Encoded;

public final class ServiceNowConstants {


  public static final String API_SN_CHG_REST_CHANGE_NORMAL = "/api/sn_chg_rest/change/normal/";

  private ServiceNowConstants() {
  }

  public static final String CLIENT_ID = "clientId";
  public static final String CLIENT_SECRET = "clientSecret";
  public static final String USERNAME = "Username";
  public static final String INSTANCE_URL = "Instance URL";
  public static final String REFRESH_TOKEN = "refresh_token";

  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  public static final String CODE = "code";
  public static final String JAXRS_ID = "servicenow";
  public static final String CALLBACK_URI = "redirect_uri";
  public static final String CHANGE_URL = "/api/sn_chg_rest/change/normal";
  public static final String INVALID_INPUT = "Invalid input";
  public static final String RESULT = "result";
  public static final String PASSWORD = "Password";

  public static final String AUTH_TYPE = "AuthType";
  public static final String OAUTH = "Oauth";
  public static final String EMAIL = "Email";
  public static final String BASIC = "BasicAuth";
  public static final String DESC = "DESC";

  public static final String SYSPARM_LIMIT = "&sysparm_limit=";
  public static final String SYSPARM_LIMIT_2 = "?sysparm_limit=";
  public static final String SYSPARM_OFFSET = "&sysparm_offset=";

  public static final String ORDERBY = "&ORDERBY";

  public static final String AND = "&AND";

  @Encoded
  public static final String SYSPARM_QUERY = "?sysparm_query=";

  public static final String FAILED_TO_SEARCH_FOR_CAMPAIGN = "Search failed. ";

  public static final String ENTITY_INPUT_IS_NULL_OR_EMPTY = "Search criteria cannot be empty. Please provide at least one search condition and try again.";


}
