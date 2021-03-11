package com.lovemesomecoding.pizzaria.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Random Generator Utility is designed to use accross this API - It randomly generate String, Number, and AlphaNumeric
 * 
 * @author fkaveinga
 * @see RandomStringUtils
 * @see RandomUtils
 */
public final class RandomGeneratorUtils {

    private static List<String> specialCharacters = Arrays.asList("@", "#", "$", "%", "!", "^", "&", "*", "()", "\"", "_", ",", "~", "`", "-", "=", "[", "]", "{", "}", "|", ";", ":", "'", ",", ".",
            "/", "<", ">", "?");

    // 30 length
    private static final int    UUID_LENGTH       = 30;

    public static String getString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static int getInteger() {
        return RandomUtils.nextInt();
    }

    public static int getIntegerWithin(int start, int end) {
        return RandomUtils.nextInt(start, end);
    }

    public static Float getFloat() {
        return RandomUtils.nextFloat();
    }

    public static Float getIntegerWithin(float start, float end) {
        return RandomUtils.nextFloat(start, end);
    }

    public static Double getDouble() {
        return RandomUtils.nextDouble();
    }

    public static Double getDoubleWithin(double start, double end) {
        return RandomUtils.nextDouble(start, end);
    }

    public static long getLongWithin(long start, long end) {
        return RandomUtils.nextLong(start, end);
    }

    public static long getLong() {
        return RandomUtils.nextLong();
    }

    public static String getAlphaNumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String getJWTId() {
        return UUID.randomUUID().toString() + "-" + RandomStringUtils.randomAlphabetic(10);
    }

    public static String getSecret(int length, int numOfSpecialCharacters) {
        StringBuilder secret = new StringBuilder(RandomStringUtils.randomAlphanumeric(length - numOfSpecialCharacters));

        if (numOfSpecialCharacters > 0) {
            int size = specialCharacters.size();

            for (int i = 0; i < numOfSpecialCharacters; i++) {
                secret.append(specialCharacters.get(getIntegerWithin(0, size - 1)));
            }

        }
        return secret.toString();
    }

    public static String getXApiKey() {
        StringBuilder uuid = new StringBuilder("xapi_");
        for (int i = 0; i < 5; i++) {
            uuid.append(RandomStringUtils.randomAlphabetic(5));
        }
        return uuid.toString();
    }

    public static String getAwsS3FileKey(String fileName) {
        // TODO Auto-generated method stub
        return UUID.randomUUID().toString() + "-" + fileName;
    }

    public static String getS3FileKey(String name) {
        return RandomStringUtils.randomAlphabetic(30) + "_" + name;
    }

}
