package com.lovemesomecoding.pizzaria.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CacheServiceImp implements CacheService {

    private static final String                    HASH_KEY = "pizzaria-api-springboot-hash-key";

    @Autowired
    private RedisTemplate<String, Object>          redisTemplate;

    // Use Hash to hash key and store that key instead of plain text key
    private HashOperations<String, Object, Object> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    @Override
    public void addUpdate(String token, ApiSession apiSession) {
        // log.debug("addUpdate({})", token);
        try {
            this.hashOperations.put(HASH_KEY, token, apiSession);
        } catch (Exception e) {
            log.error("Exception, msg:{} ", e.getMessage(), e);
        }
    }

    /**
     * might not be there. called at authentication
     * 
     * @param token
     * @return ApiSession
     */
    @Override
    public Optional<ApiSession> findApiSessionToken(String token) {
        // log.debug("findApiSessionToken({})", token);
        if (null == this.hashOperations) {
            log.warn("hash is null");
            return Optional.empty();
        }
        if (null == token) {
            log.warn("token is null");
            return Optional.empty();
        }
        try {
            return Optional.ofNullable((ApiSession) hashOperations.get(HASH_KEY, token));
        } catch (Exception e) {
            log.warn("token is null ", e);
            return Optional.empty();
        }
    }

    /**
     * must be there. called after token has been authenticated
     * 
     * @param token
     * @return ApiSession
     */
    @Override
    public ApiSession getApiSessionToken(String token) {
        // log.debug("getApiSessionToken({})", token);
        if (null == this.hashOperations) {
            log.warn("hash is null");
            return null;
        }
        if (null == token) {
            log.warn("token is null");
            return null;
        }
        try {
            return (ApiSession) hashOperations.get(HASH_KEY, token);
        } catch (Exception e) {
            log.warn("Exception, msg: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public long delete(String token) {
        return hashOperations.delete(HASH_KEY, token);
    }

    @Override
    public Map<Object, Object> findAllApiSessions() {
        return hashOperations.entries(HASH_KEY);
    }
}
