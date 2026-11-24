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

package app.krista.extensions.it.case_management.servicenow.api;

import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.authentication.AccessToken;
import app.krista.extensions.it.case_management.servicenow.authentication.OAuthClient;
import app.krista.extensions.it.case_management.servicenow.catalog.store.RefreshTokenStore;
import app.krista.extensions.it.case_management.servicenow.catalog.store.ServiceNowAttributeStore;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthService;
import app.krista.extensions.it.case_management.servicenow.impl.connectors.AuthServiceFactory;
import app.krista.ksdk.authentication.AuthorizationListener;
import app.krista.ksdk.context.AuthorizationContext;
import com.github.scribejava.core.exceptions.OAuthException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
public final class AuthenticationResource {

  public static final String USER_AUTHENTICATED_SUCCESSFULLY = "User authenticated successfully.";
  private final AuthorizationListener authorizationListener;

  private final RefreshTokenStore refreshTokenStore;

  private final AuthServiceFactory authServiceFactory;
  private final ServiceNowAttributeStore serviceNowAttributeStore;

  private final AuthorizationContext context;


  @Inject
  public AuthenticationResource(
      AuthorizationContext context, AuthorizationListener authorizationListener,
      RefreshTokenStore refreshTokenStore, AuthServiceFactory authServiceFactory,
      ServiceNowAttributeStore serviceNowAttributeStore) {
    this.context = context;
    this.authorizationListener = authorizationListener;
    this.refreshTokenStore = refreshTokenStore;
    this.authServiceFactory = authServiceFactory;
    this.serviceNowAttributeStore = serviceNowAttributeStore;
  }

  @GET
  @Path("/callback")
  public String getCallBack(@QueryParam(ServiceNowConstants.CODE) String code,
      @QueryParam("state") String state) {
    Objects.requireNonNull(code);
    String[] parts = state.split("#");
    if (parts[0].isBlank()) {
      throw new BadRequestException("Invalid state parameters!");
    }

    String key = parts[0];
    String authContextId = parts.length == 2 ? parts[1] : null;
    try {
      AuthService clientProvider;
      ServiceNowAttributes effectiveOneDriveAttributes;
      if (authContextId == null) {
        clientProvider = authServiceFactory.create();

      } else {
        effectiveOneDriveAttributes = serviceNowAttributeStore.load(authContextId);
        clientProvider = authServiceFactory.create(effectiveOneDriveAttributes);

      }

      OAuthClient oAuthClient = new OAuthClient(clientProvider.getServiceNowAttributes());

      Map<String, Object> authTokenResponse = getAuthTokenResponse(oAuthClient, code);
      String refreshToken = (String) authTokenResponse.get(ServiceNowConstants.REFRESH_TOKEN);

      key = key + "#" + clientProvider.getServiceNowAttributes().getClientID()
          + clientProvider.getServiceNowAttributes().getClientSecret();
      refreshTokenStore.put(key, refreshToken);

      if (!key.startsWith("wsContact") && authContextId == null) {
        refreshTokenStore.remove(key);
        return "Unauthorised user. User email '" + key.split("#")[0]
            + "' configured in the setup does not match with authenticated user.";
      }
      if (context.isAuthenticated()) {
        authorizationListener.authorized();
        return USER_AUTHENTICATED_SUCCESSFULLY;
      }
      return "User authenticated successfully. Save the changes.";
    } catch (OAuthException cause) {
      throw new IllegalStateException(getErrorDescription(cause), cause.getCause());
    } catch (Exception cause) {
      throw new IllegalStateException("Error occurred during authorization", cause.getCause());
    } finally {
      if (authContextId != null) {
        serviceNowAttributeStore.remove(authContextId);
      }
    }
  }


  private String getErrorDescription(OAuthException cause) {
    String errorDescription = "Authorization failed. ";
    if (cause.getMessage() != null && !cause.getMessage().isBlank()) {
      Map<String , Object> exceptionMap = ServiceNowConstants.GSON.fromJson(cause.getMessage(), Map.class);
      if (exceptionMap.containsKey("error_description")) {
        errorDescription = errorDescription + String.valueOf(exceptionMap.get("error_description")) +
                " Please verify your Client ID, Client Secret, and Instance URL are correct in the extension configuration.";
      } else {
        errorDescription = errorDescription + "Please verify your OAuth credentials (Client ID, Client Secret) and ensure the redirect URL is correctly configured in ServiceNow.";
      }
    } else {
      errorDescription = errorDescription + "Please verify your OAuth credentials and ServiceNow configuration, then try authorizing again.";
    }
    return errorDescription;
  }


  public Map<String, Object> getAuthTokenResponse(OAuthClient oAuthClient, String code) {
    Map<String, Object> response = new HashMap<>();
    try {
      Objects.requireNonNull(code);
      AccessToken accessToken = oAuthClient.getAccessToken(code);
      response.put(ServiceNowConstants.REFRESH_TOKEN, accessToken.access_token);
    } catch (Exception cause) {
      throw new IllegalStateException("Error occurred due to :" + cause.getMessage(),
          cause.getCause());
    }
    return response;
  }

}
