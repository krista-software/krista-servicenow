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

import app.krista.extensions.it.case_management.servicenow.constants.ServiceNowConstants;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Query builder utility for constructing ServiceNow API query strings.
 * <p>
 * This class provides methods to build ServiceNow query strings from search criteria,
 * handling field name mapping, operator conversion, and value transformation for
 * different field types (text, numeric, pick-one).
 * </p>
 * <p>
 * <b>Supported Operations:</b>
 * <ul>
 *   <li>Mathematical operators: >=, <=, <, >, =, !=</li>
 *   <li>String operators: STARTSWITH, ENDSWITH, CONTAINS, =</li>
 *   <li>Field name mapping from Krista to ServiceNow field names</li>
 *   <li>Pick-one value transformation (e.g., "High" → "1")</li>
 * </ul>
 * </p>
 * <p>
 * This is a utility class with only static methods and cannot be instantiated.
 * </p>
 */
public final class QueryBuilder {


  public static final Map<String , Object> mathOperators;
  public static final Map<String , Object> stringOperator;
  public static final Map<String , Object> serviceNowField;
  public static final Map<String , Object> pickOneOperandValue;

  public static final Set<String> fieldValueWithPickOne;



  static{
    mathOperators = new HashMap<>();
    serviceNowField =new HashMap<>();
    stringOperator = new HashMap<>();
    fieldValueWithPickOne = new HashSet<>();
    pickOneOperandValue = new HashMap<>();

    mathOperators.put("gte", ">=");
    mathOperators.put("lte", "<=");
    mathOperators.put("lt", "<");
    mathOperators.put("gt", ">");
    mathOperators.put("equals", "=");
    mathOperators.put("notequals", "!=");


    stringOperator.put("startswith", "STARTSWITH");
    stringOperator.put("endswith", "ENDSWITH");
    stringOperator.put("contains", "CONTAINS");
    stringOperator.put("equals", "=");

    //Incident
    serviceNowField.put("Incident Id", "number");
    serviceNowField.put("Incident State", "incident_state");
    serviceNowField.put("Impact", "impact");
    serviceNowField.put("Urgency", "urgency");

    //Case
    serviceNowField.put("Case Id", "number");
    serviceNowField.put("Channel", "contact_type");// Need Function
    serviceNowField.put("Priority", "priority");// Need Function
    serviceNowField.put("Short Description", "short_description");
    serviceNowField.put("State", "state");

    //ChangeRequest
    serviceNowField.put("Request ID", "number");
    serviceNowField.put("Sys ID", "sys_id");
    serviceNowField.put("Assigned to", "assigned_to");
    serviceNowField.put("Assignment group", "assignment_group");
    serviceNowField.put("Requested By", "requested_by");

    //customer
    serviceNowField.put("Name", "name");
    serviceNowField.put("Customer Id", "number");
    serviceNowField.put("Phone", "phone");
    serviceNowField.put("City", "city");
    serviceNowField.put("Zip Code", "zip");
    serviceNowField.put("Sys Id", "sys_id");
    //User -Done
    serviceNowField.put("Last Name", "last_name");
    serviceNowField.put("Email", "email");
    serviceNowField.put("First Name", "first_name");
    //Incident
    fieldValueWithPickOne.add("Incident State");
    fieldValueWithPickOne.add("Impact");
    fieldValueWithPickOne.add("Urgency");
    //Case
    fieldValueWithPickOne.add("Channel");
    fieldValueWithPickOne.add("Priority");
    fieldValueWithPickOne.add("State");

    pickOneOperandValue.put("Moderate",3);
    pickOneOperandValue.put("Critical",1);
    //State
    pickOneOperandValue.put("New","1");
    pickOneOperandValue.put("In Progress","2");
    pickOneOperandValue.put("On Hold","3");
    pickOneOperandValue.put("Resolved","6");
    pickOneOperandValue.put("Closed","7");
    pickOneOperandValue.put("Cancelled","8");
    // Urgency || Impact
    pickOneOperandValue.put("High",1);
    pickOneOperandValue.put("Medium",2);
    pickOneOperandValue.put("Low",3);
    //Channel

    pickOneOperandValue.put("Virtual Agent","virtual_agent");
    pickOneOperandValue.put("Phone","phone");
    pickOneOperandValue.put("Community","community");
    pickOneOperandValue.put("Web","web");
    pickOneOperandValue.put("Social","social");
    pickOneOperandValue.put("Email","email");
    pickOneOperandValue.put("Chat","chat");
    pickOneOperandValue.put("Alert","alert");


  }





  /**
   * Builds a ServiceNow query string from field, operator, and value.
   * <p>
   * This method constructs a query string segment for ServiceNow API calls,
   * handling both numeric and string operators, and transforming field names
   * and values as needed for ServiceNow's query syntax. Values are automatically
   * sanitized to prevent injection attacks.
   * </p>
   *
   * @param field the Krista field name (e.g., "Incident Id", "Priority")
   * @param operator the search operator (e.g., "equals", "contains", "gte")
   * @param value the value to search for (will be sanitized)
   * @return the ServiceNow query string segment, or empty string if invalid
   */
  public static String buildQuery(String field, String operator, String value) {
    if (value == null || value.isEmpty()) {
      return "";
    }

    // Sanitize the value to prevent injection attacks
    String sanitizedValue = sanitizeQueryValue(value);

    if (isNumeric(sanitizedValue)) {

      if (operator.equals("gte") || operator.equals("lte") ||
          operator.equals("gt") || operator.equals("notequals")|| operator.equals("equals")|| operator.equals("lt")) {

        return (String)serviceNowField.get(field) + (String)mathOperators.get(operator) + returnCorrectOperandValue(field,sanitizedValue) + ServiceNowConstants.AND;
      } else {
        return "";
      }
    } else {
      if (operator.equals("startswith") || operator.equals("endswith") ||
          operator.equals("contains") || operator.equals("equals")) {
        return (String)serviceNowField.get(field) + (String)stringOperator.get(operator) + returnCorrectOperandValue(field,sanitizedValue) +ServiceNowConstants.AND;

      } else {
        return "";
      }
    }
  }

  /**
   * Returns the correct operand value for ServiceNow queries.
   * <p>
   * This method transforms pick-one field values from user-friendly text
   * (e.g., "High", "Critical") to ServiceNow's internal numeric or string
   * representations. For non-pick-one fields, returns the operand unchanged.
   * </p>
   *
   * @param fieldName the field name to check for pick-one transformation
   * @param operand the operand value to transform
   * @return the transformed operand value for ServiceNow, or original value if no transformation needed
   */
  public static String returnCorrectOperandValue(String fieldName , String operand ){
    if(fieldValueWithPickOne.contains(fieldName)&&pickOneOperandValue.containsKey(operand)){
      if(fieldName.equals("Priority")&&operand.equals("Low")){
        return "4";
      }
      else if(fieldName.equals("Priority")&&operand.equals("High")){
        return "2";}
     return String.valueOf(pickOneOperandValue.get(operand));
    }
    return operand;
  }

  /**
   * Checks if a string represents a numeric value.
   * <p>
   * This method attempts to parse the string as a double to determine
   * if it's a valid numeric value.
   * </p>
   *
   * @param str the string to check
   * @return true if the string is numeric, false otherwise
   */
  private static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sanitizes query values to prevent injection attacks.
   * <p>
   * This method escapes special characters that have meaning in ServiceNow queries
   * to prevent malicious input from breaking or manipulating queries.
   * </p>
   * <p>
   * <b>Escaped Characters:</b>
   * <ul>
   *   <li>^ (XOR/separator) - Escaped to \^</li>
   *   <li>& (AND operator prefix) - Escaped to \&</li>
   *   <li>| (OR operator) - Escaped to \|</li>
   *   <li>\ (escape character) - Escaped to \\</li>
   * </ul>
   * </p>
   *
   * @param value the query value to sanitize
   * @return the sanitized value safe for use in ServiceNow queries, or empty string if input is null
   */
  public static String sanitizeQueryValue(String value) {
    if (value == null) {
      return "";
    }

    // Escape backslash first to prevent double-escaping
    String sanitized = value.replace("\\", "\\\\");

    // Escape ServiceNow query special characters
    sanitized = sanitized.replace("^", "\\^")
                         .replace("&", "\\&")
                         .replace("|", "\\|");

    return sanitized;
  }
}



