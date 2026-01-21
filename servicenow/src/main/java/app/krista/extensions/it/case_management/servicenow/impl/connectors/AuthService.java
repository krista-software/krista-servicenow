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

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.authentication.RestClient;
import app.krista.extensions.it.case_management.servicenow.catalog.store.RefreshTokenStore;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.ksdk.context.AuthorizationContext;
import app.krista.ksdk.context.RequestContext;
import app.krista.model.field.NamedValuedField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public final class AuthService {

    private static final Logger LOG = LogManager.getLogger(AuthService.class.getName());
    private final RefreshTokenStore refreshTokenStore;
    private final ServiceNowAttributes serviceNowAttributes;
    private final RequestContext requestContext;
    private final AuthorizationContext authorizationContext;
    private final String authContextId;

    @Inject
    public AuthService(RefreshTokenStore refreshTokenStore, ServiceNowAttributes serviceNowAttributes,
                       RequestContext requestContext, AuthorizationContext authorizationContext) {
        this(refreshTokenStore, serviceNowAttributes, requestContext, authorizationContext, null);
    }

    public AuthService(RefreshTokenStore refreshTokenStore, ServiceNowAttributes serviceNowAttributes,
                       RequestContext requestContext, AuthorizationContext authorizationContext,
                       String authContextId) {
        this.refreshTokenStore = refreshTokenStore;
        this.serviceNowAttributes = serviceNowAttributes;
        this.requestContext = requestContext;
        this.authorizationContext = authorizationContext;
        this.authContextId = authContextId;
    }

    public ServiceNowAttributes getServiceNowAttributes() {
        return serviceNowAttributes;
    }

    public RestClient getRestClientAdmin() {
        return getRestClient(true);
    }

    public RestClient getRestClient() {
        return getRestClient(!requestContext.invokeAsUser());
    }

    private RestClient getRestClient(boolean isAdmin) {

        if (serviceNowAttributes.getAuthType().equals(ServiceNowConstants.OAUTH)) {
            LOG.info("Authentication Used " + ServiceNowConstants.OAUTH);
            String userID = getUserId(isAdmin);
            String refreshToken = refreshTokenStore.get(
                    userID + "#" + getServiceNowAttributes().getClientID()
                            + getServiceNowAttributes().getClientSecret());

            if (refreshToken == null) {
                throw createMustAuthorizationException(userID, false);
            }
            boolean authorize = new RestClient(serviceNowAttributes.getInstanceURL(), refreshToken,
                    serviceNowAttributes.getAuthType()).get("/api/global/user_role_inheritance",
                    new HashMap<>()).has("result");
            if (authorize) {
                return new RestClient(serviceNowAttributes.getInstanceURL(), refreshToken,
                        serviceNowAttributes.getAuthType());
            }
            throw createMustAuthorizationException(userID, false);
        } else {
            LOG.info("Authentication Used " + ServiceNowConstants.BASIC);
            String username = serviceNowAttributes.getUsername();
            String password = serviceNowAttributes.getPassword();
            return new RestClient(serviceNowAttributes.getInstanceURL(), username, password,
                    serviceNowAttributes.getAuthType());
        }
    }

    private String getUserId(boolean calledFromValidateAttributes) {
        String userId = calledFromValidateAttributes || !requestContext.invokeAsUser()
                ? serviceNowAttributes.getEmail()
                : authorizationContext.getAuthorizedAccount().getAccountId();
        if (userId == null) {
            throw new IllegalStateException("Failed to get account");
        }
        return userId;
    }

    private MustAuthorizeException createMustAuthorizationException(String userId,
                                                                    boolean reAuthentication) {
        LOG.error(
                reAuthentication ? "Got error for Authentication. So sending for re authentication"
                        : "Got error for Authentication. So sending for authentication");
        List<NamedValuedField> details = new ArrayList<>();
        NamedValuedField userIdField = new NamedValuedField("userId", "Text", userId, new HashMap<>(),
                new HashMap<>());
        details.add(userIdField);
        if (authContextId != null) {
            NamedValuedField contextIdField = new NamedValuedField("authContextId", "Text", authContextId,
                    new HashMap<>(), new HashMap<>());
            details.add(contextIdField);
        }
        return new MustAuthorizeException(
                reAuthentication ? "Refresh Token Expired. Please reauthorize yourself"
                        : "You are not authorized. Please authorize yourself", details);
    }


}
