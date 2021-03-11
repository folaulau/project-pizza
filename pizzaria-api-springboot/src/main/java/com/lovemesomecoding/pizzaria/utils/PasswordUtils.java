package com.lovemesomecoding.pizzaria.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lovemesomecoding.pizzaria.exception.ApiException;

/***
 * 
 * @author fkaveinga
 *
 */
public final class PasswordUtils {

    private static Logger        log                  = LoggerFactory.getLogger(PasswordUtils.class);

    final static PasswordEncoder passwordEncoder      = new BCryptPasswordEncoder();

    public static final String   DEV_PASSWORD         = "Test1234!";

    private PasswordUtils() {
    }

    public static String hashPassword(final String password) {
        if (password == null || password.length() ==0) {
            log.warn("Password({}), is invalid", password);
            throw new ApiException("Password is invalid", "Password is empty");
        }
        if (ValidationUtils.isValidPassword(password) == false) {
            log.warn("Password({}), is invalid", password);
            throw new ApiException("Password is invalid", "Password is not strong enough");
        }
        return passwordEncoder.encode(password);
    }

    public static boolean verify(String password, String hashPassword) {
        return passwordEncoder.matches(password, hashPassword);
    }

}
