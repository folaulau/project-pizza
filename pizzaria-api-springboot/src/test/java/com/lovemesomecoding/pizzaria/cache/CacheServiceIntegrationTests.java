package com.lovemesomecoding.pizzaria.cache;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("local")
@SpringBootTest
public class CacheServiceIntegrationTests {

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void test_addUpdate_failed() {
        String token = "test-token-4e810961-fd73-472a-a8b5-25936d6986d6-nGVHiqSnzo";
        ApiSession apiSession = new ApiSession();
        apiSession.setToken(token);
        apiSession.setUserId(1L);
        apiSession.setUserUuid("user-4e810961-fd73-472a-a8b5-25936d6986d6-nGVHiqSnzo");

        cacheService.addUpdate(token, apiSession);

        ApiSession savedApiSession = cacheService.getApiSessionToken(token);

        log.info("savedApiSession={}", ObjMapperUtils.toJson(savedApiSession));

        assertThat(savedApiSession).isNotNull();
        assertThat(savedApiSession.getToken()).isNotNull().isEqualTo("test-token-4e810961-fd73-472a-a8b5-25936d6986d6-nGVHiqSnzo");
        assertThat(savedApiSession.getUserUuid()).isNotNull().isEqualTo("user-4e810961-fd73-472a-a8b5-25936d6986d6-nGVHiqSnzo");

    }
}
