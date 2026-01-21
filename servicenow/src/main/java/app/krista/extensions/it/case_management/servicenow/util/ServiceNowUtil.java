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

import app.krista.extension.executor.SearchCondition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for ServiceNow extension operations.
 * <p>
 * This class provides helper methods for working with ServiceNow API endpoints,
 * priority mappings, URL construction, and search condition processing.
 * </p>
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *   <li>Priority value mapping (numeric to text)</li>
 *   <li>ServiceNow API URL construction</li>
 *   <li>Search condition value extraction</li>
 *   <li>Sorting parameter validation</li>
 * </ul>
 * </p>
 * <p>
 * This is a utility class with only static methods and cannot be instantiated.
 * </p>
 */
public final class ServiceNowUtil {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ServiceNowUtil(){

    }

    public static final int MAX_RESULT = 1000;
    public static final Map<String, String> priorityKeyValues;
    public static final String EMPTYSTRING = "";
    public static final String API_SN_CUSTOMERSERVICE = "/api/sn_customerservice/";
    public static final String API_NOW_V_1_TABLE = "/api/now/v1/table/";
    public static final String CRITICAL = "Critical";
    public static final String HIGH = "High";
    public static final String MODERATE = "Moderate";
    public static final String LOW = "Low";

    static {
        priorityKeyValues = new HashMap<>();

        priorityKeyValues.put("1", CRITICAL);
        priorityKeyValues.put("2", HIGH);
        priorityKeyValues.put("3", MODERATE);
        priorityKeyValues.put("4", LOW);
    }

    /**
     * Converts a priority key (numeric) to its text representation.
     * <p>
     * Maps ServiceNow priority numbers to user-friendly text values:
     * <ul>
     *   <li>1 → Critical</li>
     *   <li>2 → High</li>
     *   <li>3 → Moderate</li>
     *   <li>4 → Low</li>
     * </ul>
     * </p>
     *
     * @param key the numeric priority key (e.g., "1", "2", "3", "4")
     * @return the text representation of the priority, or null if key not found
     */
    public static String getPriorityFromKey(String key) {
        return priorityKeyValues.get(key);
    }

    /**
     * Retrieves the key for a given value in a map.
     * <p>
     * This method performs a reverse lookup in a map, finding the first key
     * that matches the specified value.
     * </p>
     *
     * @param map the map to search
     * @param value the value to find
     * @return the key associated with the value, or null if not found
     */
    public static String getKeyFromValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Constructs a ServiceNow Table API URL for the specified endpoint.
     * <p>
     * Prepends the ServiceNow Table API base path (/api/now/v1/table/) to the endpoint.
     * </p>
     *
     * @param endPoint the table endpoint (e.g., "incident", "change_request")
     * @return the complete Table API URL
     */
    public static String getTableURL(String endPoint) {
        return API_NOW_V_1_TABLE + endPoint;
    }

    /**
     * Constructs a ServiceNow Customer Service API URL for the specified endpoint.
     * <p>
     * Prepends the ServiceNow Customer Service API base path (/api/sn_customerservice/) to the endpoint.
     * </p>
     *
     * @param endPoint the customer service endpoint (e.g., "case")
     * @return the complete Customer Service API URL
     */
    public static String getCustomerServiceURL(String endPoint) {
        return API_SN_CUSTOMERSERVICE + endPoint;
    }

    /**
     * Extracts the operand value for a specific field from search conditions.
     * <p>
     * This method searches through a list of search conditions to find the value
     * (operand) associated with a specific field name.
     * </p>
     *
     * @param conditions the list of search conditions to search
     * @param fieldName the field name to find (case-insensitive)
     * @return the operand value as a trimmed string, or empty string if not found
     */
    public static String getConditionValue(List<SearchCondition> conditions, String fieldName) {
        for (SearchCondition con : conditions) {
            if (fieldName.equalsIgnoreCase(con.getFieldName())) {
                return con.getOperand() != null ? String.valueOf(con.getOperand()).trim() : EMPTYSTRING;
            }
        }
        return EMPTYSTRING;
    }

    /**
     * Determines whether an ORDER BY clause should be added to the query.
     * <p>
     * Returns true if the operand (sort field) is not null, indicating that
     * sorting should be applied to the query results.
     * </p>
     *
     * @param operand the field name to sort by
     * @return true if operand is not null, false otherwise
     */
    public static boolean addOrderBy(String operand) {
        if (operand == null) {
            return false;
        }
        return true;
    }

}
