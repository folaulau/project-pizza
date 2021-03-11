package com.lovemesomecoding.pizzaria.config;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.lovemesomecoding.pizzaria.utils.CacheUtils;

import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties(CacheConfigurationProperties.class)
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {
    // Let spring configure connection factory using the configuration in configure
    // file.
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }
    
    @Bean
    public RedisCacheConfiguration cacheConfiguration(CacheConfigurationProperties properties) {
        return createCacheConfiguration(properties.getTimeoutSeconds());
    }
    
    private static RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheConfigurationProperties properties) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        for (Entry<String, Long> cacheNameAndTimeout : properties.getCacheExpirations().entrySet()) {
            final String key = cacheNameAndTimeout.getKey();
            log.info("configuring cache {} timeout={}", key, cacheNameAndTimeout.getValue());
            cacheConfigurations.put(key, createCacheConfiguration(cacheNameAndTimeout.getValue()));
        }

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration(properties))
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

    private static class CustomKeyGenerator implements KeyGenerator {
        public Object generate(Object target, Method method, Object... params) {
            StringBuilder strParams = new StringBuilder(target.getClass().getSimpleName());
            strParams.append("_");
            strParams.append(method.getName());
            for (Object param : params) {
                strParams.append("_");
                strParams.append(param);
            }
            return strParams.toString();
        }
    }

    @Bean
    public CacheUtils cacheUtil() {
        return new CacheUtils();
    }

}
