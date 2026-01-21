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

package app.krista.extensions.it.case_management.servicenow.authentication;


import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenJsonExtractor;

public class ServiceNowApi20 extends DefaultApi20 {

  private static String instanceURl;

  private static class InstanceHolder {

    private static final ServiceNowApi20 INSTANCE = new ServiceNowApi20();
  }

  public static ServiceNowApi20 instance(String instanceURL) {
    instanceURl = instanceURL;
    return InstanceHolder.INSTANCE;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return instanceURl + "oauth_token.do";
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return instanceURl + "oauth_auth.do";
  }

  @Override
  public OAuth2AccessTokenJsonExtractor getAccessTokenExtractor() {
    return OAuth2AccessTokenJsonExtractor.instance();
  }


}
