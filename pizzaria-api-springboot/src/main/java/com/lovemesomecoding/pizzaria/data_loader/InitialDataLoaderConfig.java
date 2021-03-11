package com.lovemesomecoding.pizzaria.data_loader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataLoaderConfig {

    @Bean
    public UserLoader userLoader() {
        return new UserLoader();
    }

    @Bean
    public ProductLoader productLoader() {
        return new ProductLoader();
    }
}
