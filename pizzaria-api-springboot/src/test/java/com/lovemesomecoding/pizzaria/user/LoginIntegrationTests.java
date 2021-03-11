package com.lovemesomecoding.pizzaria.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.utils.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class LoginIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_login_with_invalid_email() throws Exception {
        String email = "invalid-email";
        String password = "Test1234!";

        String authorization = HttpUtils.generateBasicAuthenticationToken(email, password);

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/login", "")
                .header("Authorization", authorization)
                .header("x-api-key", "test-key")
                .queryParam("type", "password");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        result.andExpect(MockMvcResultMatchers.status().is(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Email or password is invalid")));
        
        // @formatter:on
    }

    @Test
    public void test_login_with_invalid_password() throws Exception {
        String email = "folaudev@gmail.com";
        String password = "invalid password";

        String authorization = HttpUtils.generateBasicAuthenticationToken(email, password);

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/login", "")
                .header("Authorization", authorization)
                .header("x-api-key", "test-key")
                .queryParam("type", "password");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        result.andExpect(MockMvcResultMatchers.status().is(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Email or password is invalid")));
        
        // @formatter:on
    }

    @Test
    public void test_login_successfully() throws Exception {
        String email = "folaudev@gmail.com";
        String password = "Test1234!";

        String authorization = HttpUtils.generateBasicAuthenticationToken(email, password);

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/login", "")
                .header("Authorization", authorization)
                .header("x-api-key", "test-key")
                .queryParam("type", "password");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.equalToObject("folaudev@gmail.com")));
        // @formatter:on
    }

}
