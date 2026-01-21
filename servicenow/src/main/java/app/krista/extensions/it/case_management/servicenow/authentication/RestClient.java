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

import app.krista.extensions.it.case_management.servicenow.constants.Constants;
import app.krista.extensions.it.case_management.servicenow.constants.Errors;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.Encoded;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Base64;
import java.util.Map;


public final class RestClient {
    private static final Logger LOG = LogManager.getLogger(RestClient.class.getName());
    private WebTarget resourceTarget;
    private String accessToken;

    private String password;

    private String username;

    private String oauthType;

    private String instanceUrl;

    private Client client;

    public RestClient(String instanceURL, String accessToken, String oauthType) {
        this.accessToken = accessToken;
        this.client = ClientBuilder.newClient();
        this.oauthType = oauthType;
        this.instanceUrl = instanceURL;
    }

    public RestClient(String instanceURL, String username, String password, String oauthType) {
        client = ClientBuilder.newClient()
                .register(HttpAuthenticationFeature.basic(username, password));
        this.oauthType = oauthType;
        this.username = username;
        this.password = password;
        this.instanceUrl = instanceURL;
    }

    public JSONObject get(String urlWithEndPoint, Map<String, Object> queryParams) {
        Builder request = getRequest(urlWithEndPoint, queryParams);
        addHeader(request);
        return handleResponse(request.get());
    }

    public Builder getRequest(@Encoded String urlWithEndPoint,
                              Map<String, Object> queryParams) {
        LOG.info("url built " + instanceUrl + urlWithEndPoint);
        resourceTarget = client.target(instanceUrl + urlWithEndPoint);
        for (Map.Entry<String, Object> entrySet : queryParams.entrySet()) {
            resourceTarget = resourceTarget.queryParam(entrySet.getKey(), entrySet.getValue());
        }
        return resourceTarget.request(MediaType.APPLICATION_JSON);

    }

    public JSONObject post(String url, JSONObject body) {
        Builder request = client.target(instanceUrl + url).request(MediaType.APPLICATION_JSON);
        addHeader(request);
        Response response = request.post(Entity.entity(body.toString(), MediaType.APPLICATION_JSON));
        return handleResponse(response);

    }

    public JSONObject put(String url, JSONObject body) {
        Builder request = client.target(instanceUrl + url).request(MediaType.APPLICATION_JSON);
        addHeader(request);
        Response response = request.put(Entity.entity(body.toString(), MediaType.APPLICATION_JSON));
        return handleResponse(response);

    }

    public JSONObject patch(String url, JSONObject body) {
        Builder request = client.target(instanceUrl + url)
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .request(MediaType.APPLICATION_JSON);
        addHeader(request);
        Response response = request.method("PATCH",
                Entity.entity(body.toString(), MediaType.APPLICATION_JSON));
        return handleResponse(response);

    }

    public void delete(String url, Map<String, Object> queryParams) {
        Builder request = getRequest(url, queryParams);
        handleResponse(request.delete());
    }

    private void addHeader(Builder request) {
        if (accessToken != null && oauthType.equals(ServiceNowConstants.OAUTH)) {
            request.header("Authorization", "Bearer " + accessToken);
        } else if (oauthType.equals(ServiceNowConstants.BASIC)) {
            request.header("Authorization", "Basic " + encodeCredentials(username, password));
        }

    }

    public String encodeCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }


    public JSONObject handleResponse(Response response) {
        String responseString = response.readEntity(String.class);
        try {
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                return new JSONObject(responseString);
            } else if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
                throw new IllegalArgumentException(String.valueOf(new JSONObject(responseString).getJSONObject(
                        Errors.ERROR).get(Constants.MESSAGE)));
            } else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
                throw new IllegalArgumentException(String.valueOf(new JSONObject(responseString).getJSONObject(
                        Errors.ERROR).get(Constants.MESSAGE)));
            } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                if (oauthType.equals(ServiceNowConstants.BASIC)) {
                    throw new IllegalArgumentException(String.valueOf(new JSONObject(responseString).getJSONObject(
                            Errors.ERROR).get(Constants.MESSAGE)));
                }
                return new JSONObject(responseString);
            } else {
                throw new IllegalArgumentException("Invalid request data. Please verify all input fields are correct and properly formatted. Check that required fields are provided and field values match expected formats.");
            }
        } catch (JSONException cause) {
            throw new IllegalArgumentException("Unable to process ServiceNow API response. The response format may be invalid or unexpected. Please verify your ServiceNow instance is accessible and try again.", cause);
        }
    }

}
