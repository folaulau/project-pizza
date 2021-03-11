package com.lovemesomecoding.pizzaria.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lovemesomecoding.pizzaria.dto.SignUpDTO;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.utils.HttpUtils;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class SignupIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_signup_with_invalid_email() throws Exception {
        String email = "folaudev.com";
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword("Test1234!");

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/signup")
                .header("x-api-key", "test-key")
                .content(ObjMapperUtils.toJson(signUpDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(400));
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Email is invalid")));
        // @formatter:on
    }

    @Test
    public void test_login_with_invalid_password() throws Exception {
        String email = "folaudev+" + UUID.randomUUID().toString() + "@gmail.com";
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword("test");

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/signup")
                .header("x-api-key", "test-key")
                .content(ObjMapperUtils.toJson(signUpDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(400));
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Password is invalid")));
        // @formatter:on
    }

    @Test
    public void test_signup_successfully() throws Exception {

        String email = "folaudev+" + UUID.randomUUID().toString() + "@gmail.com";
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail(email);
        signUpDTO.setPassword("Test1234!");

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users/signup")
                .header("x-api-key", "test-key")
                .content(ObjMapperUtils.toJson(signUpDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.equalToObject(email)));
        // @formatter:on
    }

}
