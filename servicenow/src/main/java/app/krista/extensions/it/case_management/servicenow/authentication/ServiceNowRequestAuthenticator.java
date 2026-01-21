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

import app.krista.extension.authorization.MustAuthenticateException;
import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extension.authorization.RequestAuthenticator;
import app.krista.extension.request.ProtoRequest;
import app.krista.extension.request.ProtoResponse;
import app.krista.extension.request.protos.http.HttpRequest;
import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.catalog.store.ServiceNowAttributeStore;
import app.krista.model.field.NamedField;
import app.krista.model.field.NamedValuedField;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import org.jvnet.hk2.annotations.Service;

@Service
public final class ServiceNowRequestAuthenticator implements RequestAuthenticator {


  private ServiceNowAttributeStore serviceNowAttributeStore;

  private ServiceNowAttributes serviceNowAttributes;

  @Inject
  public ServiceNowRequestAuthenticator(ServiceNowAttributeStore serviceNowAttributeStore,
      ServiceNowAttributes serviceNowAttributes) {
    this.serviceNowAttributes = serviceNowAttributes;
    this.serviceNowAttributeStore = serviceNowAttributeStore;

  }

  @Override
  public String getScheme() {
    return null;
  }

  @Override
  public Set<String> getSupportedProtocols() {
    return Collections.emptySet();
  }

  @Override
  public String getAuthenticatedAccountId(ProtoRequest protoRequest) {
    return handleAuthRequest(protoRequest);
  }

  @Override
  public boolean setServiceAuthorization(String s) {
    return false;
  }

  @Override
  public Map<String, NamedField> getAttributeFields() {
    return Map.of();
  }

  @Override
  public ProtoResponse getMustAuthenticateResponse(MustAuthenticateException cause,
      ProtoRequest protoRequest) {
    return null;
  }

  @Override
  public AuthorizationResponse getMustAuthenticateResponse(MustAuthenticateException cause) {
    return null;
  }

  @Override
  public ProtoResponse getMustAuthorizeResponse(MustAuthorizeException cause,
      ProtoRequest protoRequest) {
    return null;
  }

  @Override
  public AuthorizationResponse getMustAuthorizeResponse(MustAuthorizeException cause) {

    String userId = (String) cause.getDetails().get(0).getValue();
    Optional<NamedValuedField> authContextIdField = cause.getDetails().stream()
        .filter(f -> Objects.equals(f.getName(), "authContextId")).findFirst();

    ServiceNowAttributes effectiveServiceNowAttributes;
    String state;
    if (authContextIdField.isPresent()) {
      String authContextId = (String) authContextIdField.get().getValue();
      effectiveServiceNowAttributes = serviceNowAttributeStore.load(authContextId);
      state = userId + "#" + authContextId;
    } else {
      effectiveServiceNowAttributes = serviceNowAttributes;
      state = userId;
    }


    String oAuthUrl = new OAuthClient(effectiveServiceNowAttributes).getOAuthUrl(state);


    String urlQueryParam = "&access_type=offline&approval_prompt=force";
    return new AuthorizationResponse(oAuthUrl + urlQueryParam, List.of());


  }

  private String handleAuthRequest(ProtoRequest protoRequest) {
    if (!(protoRequest instanceof HttpRequest)) {
      return null;
    }
    try {
      HttpRequest httpRequest = (HttpRequest) protoRequest;
      if (httpRequest.getUri().getPath().equals("/callback")) {
        httpRequest.bufferBody();
        MultivaluedMap<String, String> queryParameters = httpRequest.getQueryParameters();
        String state = String.valueOf(queryParameters.get("state").get(0));
        String[] parts = state.split("#");
        return parts[0];
      } else {
        return null;
      }
    } catch (RuntimeException | IOException cause) {
      return null;
    }
  }

}
