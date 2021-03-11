package com.lovemesomecoding.pizzaria.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ConversionUtils {

    public static String toDateString(Date date) {
        return new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(date);
    }

    public static String toJsonString(Object object) {
        try {
            return ObjMapperUtils.getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("ConversionUtils - toJsonString - JsonProcessingException - Msg: " + e.getLocalizedMessage());
            return "{}";
        }
    }

    public static <E> List<E> getListFromSet(Set<E> set) {
        return new ArrayList<E>(set);
    }

    public static <E> Set<E> getSetFromList(List<E> list) {
        return new HashSet<E>(list);
    }

    public static <T> T map(Object object, Class<T> clazz) {
        try {
            return ObjMapperUtils.getObjectMapper().convertValue(object, clazz);
        } catch (Exception e) {
            System.out.println("ObjectUtil - printJson - JsonProcessingException - Msg: " + e.getLocalizedMessage());
            return null;
        }

    }

    public static File convert(MultipartFile multipartFile) {
        File convFile = new File(multipartFile.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(multipartFile.getBytes());
            fos.close();
            return convFile;
        } catch (Exception e) {
            System.out.println("Exception, msg: " + e.getMessage());
            return null;
        }
    }

    public static Map<String, Object> convertJsonStringToMap(String jsonObj) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = new HashMap<String, Object>();

            map = mapper.readValue(jsonObj, new TypeReference<Map<String, Object>>() {});

            return map;

        } catch (JsonGenerationException e) {
            // System.out.println("JsonGenerationException, msg: "+e.getLocalizedMessage());
        } catch (JsonMappingException e) {
            // System.out.println("JsonMappingException, msg: "+e.getLocalizedMessage());
        } catch (IOException e) {
            // System.out.println("IOException, msg: "+e.getLocalizedMessage());
        }
        return null;
    }

    public static <T> Collection<T> getCollectionFromIterable(Iterable<T> itr) {
        // Create an empty Collection to hold the result
        Collection<T> cltn = new ArrayList<T>();

        return StreamSupport.stream(itr.spliterator(), false).collect(Collectors.toList());
    }

    public static <T> List<T> getListFromIterable(Iterable<T> itr) {
        // Create an empty Collection to hold the result
        Collection<T> cltn = new ArrayList<T>();
        return new ArrayList<>(StreamSupport.stream(itr.spliterator(), false).collect(Collectors.toList()));
    }

    public static <T> Set<T> getSetFromCollection(Collection<T> col) {
        return col.stream().collect(Collectors.toSet());
    }

    public static File unZip(String zipFilePath) throws IOException {
        File destDir = new File("");
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry zipEntry = zis.getNextEntry();

        File newFile = null;
        while (zipEntry != null) {
            newFile = newFile(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();

        return newFile;
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static double getOrZero(Double value) {
        if (value == null) {
            return 0;
        }

        if (value.isNaN()) {
            return 0;
        }

        return value.doubleValue();
    }

    public static double getOrZero(BigDecimal value) {
        if (value == null) {
            return 0;
        }

        try {
            return value.doubleValue();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return 0;
    }

    public static float getOrZero(Float value) {
        if (value == null) {
            return 0;
        }
        return value.floatValue();
    }

    public static int getOrZero(Integer value) {
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public static Double convertCentsToDollars(Long amount) {
        if (amount == null) {
            return 0.0;
        }
        return BigDecimal.valueOf((double) amount).divide(BigDecimal.valueOf(100.0)).doubleValue();

    }

    public static int convertDollarToCents(Double dollar) {
        if (dollar == null) {
            return 0;
        }
        return (int) (dollar.doubleValue() * 100);
    }

}
