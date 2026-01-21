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

package app.krista.extensions.it.case_management.servicenow;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extension.util.InvokerAttributes;
import app.krista.extensions.it.case_management.servicenow.authentication.ServiceNowApi20;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public final class ServiceNowAttributes {


    public static final String USERACCOPUNT = "useraccopunt";
    public static final String CALLBACK = "/callback";
    private String clientId;

    private String clientSecret;

    private String instanceUrl;

    private String email;

    private String routingUrl;

    private String authType;

    private String username;

    private String password;

    private OAuth20Service oAuth20Service;


    public String getClientID() {
        return clientId;
    }

    public String getInstanceURL() {
        return instanceUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getRoutingUrl() {
        return routingUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthType() {
        return authType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCallBackUri() {
        return routingUrl+"/"+ServiceNowConstants.JAXRS_ID + CALLBACK;
    }

    public static ServiceNowAttributes create(Invoker invoker, Map<String, Object> attributes) {
        return new ServiceNowAttributes(
                invoker.getRoutingInfo()
                        .getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
                attributes);
    }

    @Inject
    public ServiceNowAttributes(Invoker invoker) {
        this(invoker.getRoutingInfo()
                        .getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
                invoker.getAttributes());
    }

    public ServiceNowAttributes(String routingUrl, Map<String, Object> attributes) {
        this(routingUrl, InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.CLIENT_ID),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.CLIENT_SECRET),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.EMAIL),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.INSTANCE_URL),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.AUTH_TYPE),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.USERNAME),
                InvokerAttributes.getStringOrNull(attributes, ServiceNowConstants.PASSWORD)

        );
    }

    public ServiceNowAttributes(String routingUrl, String clientId, String clientSecret,
                                String email, String instanceUrl, String authType, String username, String password) {

        this.routingUrl = routingUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.instanceUrl = instanceUrl;
        this.email = email;
        this.authType = authType;
        this.username = username;
        this.password = password;


    }

    public void update(Map<String, Object> newAttributes) {
        clientId = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.CLIENT_ID);
        email = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.EMAIL);
        clientSecret = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.CLIENT_SECRET);
        instanceUrl = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.INSTANCE_URL);
        username = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.USERNAME);
        password = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.PASSWORD);
        authType = InvokerAttributes.getStringOrNull(newAttributes, ServiceNowConstants.AUTH_TYPE);
    }

    public synchronized OAuth20Service getOAuth20Service() {
        if (oAuth20Service == null) {
            oAuth20Service = new ServiceBuilder(clientId)
                    .apiSecret(clientSecret)
                    .callback(getCallBackUri())
                    .defaultScope(USERACCOPUNT)
                    .build(ServiceNowApi20.instance(instanceUrl));
        }
        return oAuth20Service;
    }

}
