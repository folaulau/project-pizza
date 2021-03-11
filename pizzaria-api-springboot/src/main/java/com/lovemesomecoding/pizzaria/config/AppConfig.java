package com.lovemesomecoding.pizzaria.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovemesomecoding.pizzaria.utils.Constants;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

@Profile({"local", "dev", "prod"})
@Configuration
public class AppConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = ObjMapperUtils.getObjectMapper();
        // Date and Time Format
        objectMapper.setDateFormat(new SimpleDateFormat(Constants.UTC_DATETIME_PATTERN, Locale.US));
        return objectMapper;
    }
}
