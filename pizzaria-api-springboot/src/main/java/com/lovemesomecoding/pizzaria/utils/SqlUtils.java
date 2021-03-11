package com.lovemesomecoding.pizzaria.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SqlUtils {

    private static Map<String, Set<String>> tableColumns = new HashMap<String, Set<String>>();

    public static Set<String> getTableColumns(JdbcTemplate jdbcTemplate, String tableName) {
        String schema = null;
        try {
            schema = jdbcTemplate.getDataSource().getConnection().getSchema();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        log.info("jdbc schema={}", schema);

        if (tableName != null && tableColumns.containsKey(schema + "-" + tableName)) {
            return tableColumns.get(schema + "-" + tableName);
        }

        log.info("getTableColumns(..)");
        Set<String> columnNames = new HashSet<String>();

        try {

            tableColumns.put(schema + "-" + tableName, columnNames);

        } catch (Exception e) {
            log.info("Exception, msg={}", e.getMessage());
        }

        return columnNames;
    }

    public static String extractSqlError(String error) {
        log.info("error: {}", error);
        final String DUPLICATE_ENTRY = "Duplicate entry";
        final String MULTIPLE_REPRESENTATIONS = "Multiple representations";
        StringBuilder result = new StringBuilder();
        if (error.contains(DUPLICATE_ENTRY)) {
            log.info("{} issue.", DUPLICATE_ENTRY);
            int firstQuote = error.indexOf("'");
            int secondQuote = error.indexOf("'", DUPLICATE_ENTRY.length() + 2);
            log.info("{}, {}", firstQuote, secondQuote);
            result.append(error.substring(firstQuote + 1, secondQuote) + " is taken already");
        } else if (error.contains(MULTIPLE_REPRESENTATIONS)) {
            log.info("{} issue.", MULTIPLE_REPRESENTATIONS);
            // int firstQuote = error.indexOf("'");
            // int secondQuote = error.indexOf("'", DUPLICATE_ENTRY.length()+2);
            // log.info("{}, {}",firstQuote, secondQuote);
            // result.append(error.substring(firstQuote+1, secondQuote)+" taken already");
            result.append(MULTIPLE_REPRESENTATIONS);
        }

        return result.toString();
    }

    public static Class populate(Class clazz, ResultSet rs) {
        List<String> untouchedAttributes = Arrays.asList("serialVersionUID");

        Field[] fields = clazz.getClass().getDeclaredFields();

        for (Field field : fields) {
            log.info("name={}, access={},type={}", field.getName(), field.isAccessible(), field.getType());
            try {
                if (untouchedAttributes.contains(field.getName())) {
                    continue;
                }

                field.setAccessible(true);

                if (field.getType().equals(String.class)) {
                    field.set(clazz, rs.getString(field.getName()));
                } else if (field.getType().equals(Boolean.class)) {
                    field.setBoolean(clazz, rs.getBoolean(field.getName()));
                } else if (field.getType().equals(Long.class)) {
                    field.setLong(clazz, Long.valueOf(rs.getLong(field.getName())));
                } else if (field.getType().equals(Integer.class)) {
                    field.setInt(clazz, rs.getInt(field.getName()));
                } else if (field.getType().equals(Short.class)) {
                    field.setShort(clazz, rs.getShort(field.getName()));
                } else if (field.getType().equals(Double.class)) {
                    field.setDouble(clazz, rs.getDouble(field.getName()));
                }
            } catch (Exception e) {
                System.out.println("Patch - Exception, msg: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return clazz;
    }

    public static <T> T maps(Class<T> clazz, ResultSet rs, int rowNum) {
        ObjectNode objectNode = ObjMapperUtils.getObjectMapper().createObjectNode();
        ResultSetMetaData rsmd;
        try {
            rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            for (int index = 1; index <= columnCount; index++) {
                String column = JdbcUtils.lookupColumnName(rsmd, index);
                Object value = rs.getObject(column);
                if (value == null) {
                    objectNode.putNull(column);
                } else if (value instanceof Integer) {
                    objectNode.put(column, (Integer) value);
                } else if (value instanceof String) {
                    objectNode.put(column, (String) value);
                } else if (value instanceof Boolean) {
                    objectNode.put(column, (Boolean) value);
                } else if (value instanceof Date) {
                    objectNode.put(column, ((Date) value).getTime());
                } else if (value instanceof Long) {
                    objectNode.put(column, (Long) value);
                } else if (value instanceof Double) {
                    objectNode.put(column, (Double) value);
                } else if (value instanceof Float) {
                    objectNode.put(column, (Float) value);
                } else if (value instanceof BigDecimal) {
                    objectNode.put(column, (BigDecimal) value);
                } else if (value instanceof Byte) {
                    objectNode.put(column, (Byte) value);
                } else if (value instanceof byte[]) {
                    objectNode.put(column, (byte[]) value);
                } else {
                    throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        clazz = (Class<T>) ObjMapperUtils.getObjectMapper().convertValue(objectNode, clazz);

        return (T) clazz;
    }

}
