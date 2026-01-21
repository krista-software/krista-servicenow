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

import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.it.case_management.servicenow.util.JsonMapper;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth 2.0 client for ServiceNow authentication.
 * <p>
 * This class handles OAuth 2.0 authentication flow with ServiceNow, including:
 * <ul>
 *   <li>Generating OAuth authorization URLs</li>
 *   <li>Exchanging authorization codes for access tokens</li>
 *   <li>Managing OAuth client credentials</li>
 * </ul>
 * </p>
 * <p>
 * The OAuth flow follows the Authorization Code Grant type as specified in
 * the OAuth 2.0 specification and ServiceNow's OAuth implementation.
 * </p>
 *
 * @see AccessToken
 * @see ServiceNowAttributes
 */
@Service
public final class OAuthClient {

    private static final String OAUTH_PATH = "oauth_auth.do";
    private static final String RESPONSE_TYPE = "response_type";
    private static final String CODE = "code";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret,";
    private static final String OAUTH_TOKEN = "oauth_token.do";
    private static final String STATE = "state";
    private final ServiceNowAttributes serviceNowAttributes;
    private final WebTarget webTarget;

    @Inject
    public OAuthClient(ServiceNowAttributes serviceNowAttributes) {
        this.serviceNowAttributes = serviceNowAttributes;
        this.webTarget = ClientBuilder.newClient()
                .register(HttpAuthenticationFeature.basic(serviceNowAttributes.getClientID(),
                        serviceNowAttributes.getClientSecret()))
                .target(serviceNowAttributes.getInstanceURL());
    }

    /**
     * Generates the OAuth authorization URL for ServiceNow.
     * <p>
     * This method constructs the complete OAuth authorization URL that users
     * should be redirected to for granting access. The URL includes all required
     * OAuth parameters including client ID, redirect URI, and state.
     * </p>
     *
     * @param state a unique state parameter for CSRF protection
     * @return the complete OAuth authorization URL
     */
    public String getOAuthUrl(String state) {
        WebTarget authorize = webTarget.path(OAUTH_PATH);
        authorize = authorize.queryParam(RESPONSE_TYPE, CODE);
        authorize = authorize.queryParam(CLIENT_ID, serviceNowAttributes.getClientID());
        authorize = authorize.queryParam(CLIENT_SECRET, URLEncoder.encode(serviceNowAttributes.getClientSecret(),
                StandardCharsets.UTF_8));
        authorize = authorize.queryParam(STATE, state);
        authorize = authorize.queryParam(ServiceNowConstants.CALLBACK_URI, URLEncoder.encode(serviceNowAttributes.getCallBackUri(),
                StandardCharsets.UTF_8));
        return authorize.getUri().toString();
    }

    /**
     * Exchanges an authorization code for an access token.
     * <p>
     * This method completes the OAuth authorization flow by exchanging the
     * authorization code received from ServiceNow for an access token that
     * can be used to make API calls.
     * </p>
     *
     * @param code the authorization code received from ServiceNow
     * @return the {@link AccessToken} containing access token and refresh token
     */
    public AccessToken getAccessToken(String code) {
        return getAccessToken(new Form()
                .param("grant_type", "authorization_code")
                .param("code", code));
    }

    /**
     * Requests an access token from ServiceNow using the provided form parameters.
     * <p>
     * This private method handles the actual HTTP request to ServiceNow's token
     * endpoint, including client credentials and callback URI.
     * </p>
     *
     * @param body the form containing grant type and authorization code
     * @return the {@link AccessToken} containing access token and refresh token
     */
    private AccessToken getAccessToken(Form body) {
        body.param(ServiceNowConstants.CALLBACK_URI, serviceNowAttributes.getCallBackUri())
                .param(ServiceNowConstants.CLIENT_ID, serviceNowAttributes.getClientID())
                .param(ServiceNowConstants.CLIENT_SECRET, serviceNowAttributes.getClientSecret());

        Response response = webTarget.path(OAUTH_TOKEN)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.form(body));
        return JsonMapper.fromJson(response.readEntity(String.class), AccessToken.class);
    }

}
