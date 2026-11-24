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

package app.krista.extensions.it.case_management.servicenow.impl.connectors;

import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.catalog.store.RefreshTokenStore;
import app.krista.extensions.it.case_management.servicenow.catalog.store.ServiceNowAttributeStore;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;

@Service
public class AuthServiceFactory {


  private final RefreshTokenStore refreshTokenStore;
  private final ServiceNowAttributeStore serviceNowAttributeStore;
  private final RequestContext requestContext;
  private final AuthorizationContext authorizationContext;

  private final AuthService defaultClientProvider;

  @Inject
  public AuthServiceFactory(RefreshTokenStore refreshTokenStore,
      ServiceNowAttributeStore serviceNowAttributeStore, ServiceNowAttributes serviceNowAttributes,
      RequestContext requestContext, AuthorizationContext authorizationContext) {
    this(refreshTokenStore, serviceNowAttributeStore, requestContext, authorizationContext,
        new AuthService(refreshTokenStore, serviceNowAttributes, requestContext,
            authorizationContext, null));
  }

  public AuthServiceFactory(RefreshTokenStore refreshTokenStore,
      ServiceNowAttributeStore serviceNowAttributeStore, RequestContext requestContext,
      AuthorizationContext authorizationContext, AuthService defaultClientProvider) {
    this.refreshTokenStore = refreshTokenStore;
    this.serviceNowAttributeStore = serviceNowAttributeStore;
    this.requestContext = requestContext;
    this.authorizationContext = authorizationContext;
    this.defaultClientProvider = defaultClientProvider;
  }


  public AuthService create(ServiceNowAttributes serviceNowAttributes) {
    if (serviceNowAttributes.getAuthType().equals(ServiceNowConstants.OAUTH)) {
      String authContextId = serviceNowAttributeStore.save(serviceNowAttributes);
      return new AuthService(refreshTokenStore, serviceNowAttributes, requestContext,
          authorizationContext, authContextId);
    }
    return new AuthService(refreshTokenStore, serviceNowAttributes, requestContext,
        authorizationContext, null);
  }

  public AuthService create() {
    return defaultClientProvider;
  }


}
