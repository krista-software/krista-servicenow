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

package app.krista.extensions.it.case_management.servicenow.util;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public final class JsonMapper {

    private JsonMapper(){

    }

    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    public static <T> T fromJson(JsonObject json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static <T> T fromJson(String json, Type userListType) {
        return gson.fromJson(json, userListType);
    }

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

}
