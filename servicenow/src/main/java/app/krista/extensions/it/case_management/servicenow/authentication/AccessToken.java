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

public final class AccessToken {

  public final String access_token;
  public final String scope;
  public final Long expires_in;
  public final String token_type;
  public final String refresh_token;

  public AccessToken(String access_token, String scope, Long expires_in, String token_type,
      String refreshToken) {
    this.access_token = access_token;
    this.scope = scope;
    this.expires_in = expires_in;
    this.token_type = token_type;
    this.refresh_token = refreshToken;
  }

}
