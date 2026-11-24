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

package app.krista.extensions.it.case_management.servicenow.catalog.store;

import app.krista.extension.executor.Invoker;
import app.krista.extension.request.RoutingInfo;
import app.krista.extension.request.protos.http.HttpProtocol;
import app.krista.extensions.it.case_management.servicenow.ServiceNowAttributes;
import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import app.krista.extensions.util.KeyValueStore;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;


@Service
public class ServiceNowAttributeStore {

  private final Invoker invoker;
  private final KeyValueStore store;

  @Inject
  public ServiceNowAttributeStore(Invoker invoker, KeyValueStore store) {
    this.invoker = invoker;
    this.store = store;
  }

  public ServiceNowAttributes load(String authContextId) {
    Map<String,Object> attributes = ServiceNowConstants.GSON.fromJson(String.valueOf(store.get(authContextId)),
        Map.class);
    return new ServiceNowAttributes(invoker.getRoutingInfo()
        .getRoutingURL(HttpProtocol.PROTOCOL_NAME, RoutingInfo.Type.APPLIANCE),
        (String) attributes.get(ServiceNowConstants.CLIENT_ID),
        (String) attributes.get(ServiceNowConstants.CLIENT_SECRET),
        (String) attributes.get(ServiceNowConstants.EMAIL),
        (String) attributes.get(ServiceNowConstants.INSTANCE_URL), (String) attributes.get(ServiceNowConstants.OAUTH), null, null);
  }

  public String save(ServiceNowAttributes serviceNowAttributes) {
    String authContextId = UUID.randomUUID().toString();
    store.put(authContextId, ServiceNowConstants.GSON.toJson(
        Map.of(ServiceNowConstants.CLIENT_ID, serviceNowAttributes.getClientID(),
            ServiceNowConstants.CLIENT_SECRET, serviceNowAttributes.getClientSecret(),
            ServiceNowConstants.INSTANCE_URL, serviceNowAttributes.getInstanceURL(),
            ServiceNowConstants.EMAIL, serviceNowAttributes.getEmail(),
            ServiceNowConstants.OAUTH, serviceNowAttributes.getAuthType())));
    return authContextId;
  }


  public void remove(String authContextId) {
    store.remove(authContextId);
  }

}
