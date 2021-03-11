package com.lovemesomecoding.pizzaria.security;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.lovemesomecoding.pizzaria.security.jwt.JwtPayload;
import com.lovemesomecoding.pizzaria.security.jwt.JwtTokenService;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenUtilsTests {
    
    JwtTokenService jwtTokenService = new JwtTokenService();

    @Test
    public void test_generateToken_valid() {
        String jti = "test-jti";
        String uuid = "test-uuid";

        JwtPayload payload = new JwtPayload(jti, uuid);

        String token = jwtTokenService.generateToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThan(0);
    }

    @Test
    public void test_generateToken_invalid() {

        JwtPayload payload = null;

        String token = jwtTokenService.generateToken(payload);

        assertThat(token).isNull();
    }

    @Test
    public void test_getPayloadByToken_valid() {
        String jti = "test-jti";
        String uuid = "test-uuid";
        JwtPayload payload = new JwtPayload(jti, uuid);

        log.info("payload={}", ObjMapperUtils.toJson(payload));

        String token = jwtTokenService.generateToken(payload);

        log.info("token={}", token);

        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThan(0);

        JwtPayload updatedPayload = jwtTokenService.getPayloadByToken(token);

        log.info("updatedPayload={}", ObjMapperUtils.toJson(updatedPayload));

        assertThat(updatedPayload).isNotNull();
        assertThat(updatedPayload.getJti()).isEqualTo("test-jti");
        assertThat(updatedPayload.getSub()).isEqualTo("test-uuid");

    }

}
