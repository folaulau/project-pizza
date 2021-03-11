package com.lovemesomecoding.pizzaria.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.lovemesomecoding.pizzaria.security.CustomLoginFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtilsTests {

    @Test
    public void test_generateBasicAuthenticationToken() {
        String email = "foladev@gmail.com";
        String password = "Test1234!";
        String authorizationHeader = HttpUtils.generateBasicAuthenticationToken(email, password);

        assertThat(authorizationHeader).isNotNull();

        log.info("email={},password={},authorizationHeader={}", email, password, authorizationHeader);

        String uEmail = HttpUtils.getUsername(authorizationHeader);
        String uPassword = HttpUtils.getPassword(authorizationHeader);

        assertThat(uEmail).isEqualTo("foladev@gmail.com");
        assertThat(uPassword).isEqualTo("Test1234!");

        log.info("uEmail={},uPassword={}", uEmail, uPassword);

    }

    @Test
    public void generateBasicAuthenticationToken() {
        String email = "folaudev@gmail.com";
        String password = "Test1234!";
        String authorizationHeader = HttpUtils.generateBasicAuthenticationToken(email, password);

        log.info("email={},password={}", email, password);
        log.info("authorizationHeader={}", authorizationHeader);

    }

}
