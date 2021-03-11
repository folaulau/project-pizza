/*******************************************************************************
 * @ ObjectUtils.java @ Project: SideCar Health Corporation
 *
 *   Copyright (c) 2018 SideCar Health Corporation. - All Rights Reserved El Segundo, California, U.S.A.
 *
 *   This software is the confidential and proprietary information of SideCar Health Corporation. ("Confidential
 *   Information").
 *
 *   You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with SideCar Corporation.
 *
 *   Contributors: SideCar Health Corporation. - Software Engineering Team
 ******************************************************************************/
package com.lovemesomecoding.pizzaria.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjMapperUtils {

    private static Logger log = LoggerFactory.getLogger(ObjMapperUtils.class);

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        // Serialization
        // Serialization
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        // Deserialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

        objectMapper.setSerializationInclusion(Include.NON_NULL);

        // Date and Time Format
        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.UTC_DATETIME_PATTERN, Locale.US));


        objectMapper.setSerializationInclusion(Include.NON_NULL);
        return objectMapper;
    }

    public static ObjectNode getObjectNode() {
        return getObjectMapper().createObjectNode();
    }

    public static ArrayNode getArrayNode() {
        return getObjectMapper().createArrayNode();
    }

    public static <T> T map(Object object, Class<T> clazz) {
        try {
            return getObjectMapper().convertValue(object, clazz);
        } catch (Exception e) {
            log.warn("ObjectUtil - printJson - JsonProcessingException - Msg: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            return ObjMapperUtils.getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("JsonProcessingException, msg: " + e.getLocalizedMessage());
            return "{}";
        }
    }

    public static <T> T fromJsonString(String json, Class<T> clazz) {
        try {
            return ObjMapperUtils.getObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            log.warn("JsonProcessingException, msg: " + e.getLocalizedMessage());
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, String> fromJsonStringToMap(String json) {
        try {
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
            return ObjMapperUtils.getObjectMapper().readValue(json, typeRef);
        } catch (Exception e) {
            log.warn("JsonProcessingException, msg: " + e.getLocalizedMessage());
            return null;
        }

    }

    public static List<Field> getNonNullFields(Object thisObject, List<String> ignoredFieldNames) {
        List<Field> returnFields = null;
        if (thisObject != null) {
            Field[] fields = thisObject.getClass().getDeclaredFields();
            returnFields = new ArrayList<Field>();
            for (Field field : fields) {
                boolean accessFlag = field.isAccessible();
                try {

                    if (!accessFlag) {
                        field.setAccessible(true);
                    }
                    if (field.get(thisObject) != null) {
                        if (ignoredFieldNames != null && ignoredFieldNames.contains(field.getName())) {
                            continue;
                        }
                        returnFields.add(field);
                    }
                } catch (Exception e) {
                }
                if (!accessFlag) {
                    field.setAccessible(false);
                }
            }
        }
        return returnFields;
    }

    public static ObjectNode mapSqlResultSetToObject(ResultSet rs) {
        ObjectNode objectNode = getObjectMapper().createObjectNode();
        ResultSetMetaData rsmd;
        int columnCount;
        try {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

            for (int index = 1; index <= columnCount; index++) {
                String column = JdbcUtils.lookupColumnName(rsmd, index);
                Object value = rs.getObject(column);
                // log.info("{}, {}", value, value.getClass());
                if (value == null) {
                    objectNode.putNull(column);
                } else if (value instanceof Byte) {
                    objectNode.put(column, (Byte) value);

                } else if (value instanceof Short) {
                    objectNode.put(column, (Short) value);

                } else if (value instanceof Integer) {
                    objectNode.put(column, (Integer) value);

                } else if (value instanceof Long) {
                    objectNode.put(column, (Long) value);

                } else if (value instanceof Float) {
                    objectNode.put(column, (Float) value);

                } else if (value instanceof Double) {
                    objectNode.put(column, (Double) value);

                } else if (value instanceof Character) {
                    objectNode.put(column, (Character) value);

                } else if (value instanceof String) {
                    objectNode.put(column, (String) value);

                } else if (value instanceof Boolean) {
                    objectNode.put(column, (Boolean) value);

                } else if (value instanceof Date) {
                    objectNode.put(column, ((Date) value).getTime());

                } else if (value instanceof java.sql.Date) {
                    objectNode.put(column, ((java.sql.Date) value).getTime());

                } else if (value instanceof java.sql.Timestamp) {
                    objectNode.put(column, ((java.sql.Timestamp) value).getTime());

                } else if (value instanceof BigDecimal) {
                    objectNode.put(column, (BigDecimal) value);

                } else if (value instanceof byte[]) {
                    objectNode.put(column, (byte[]) value);

                } else {
                    throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
                }
            }

        } catch (SQLException e) {
            log.info(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return objectNode;
    }

}
