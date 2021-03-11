package com.lovemesomecoding.pizzaria.cache;

import java.util.Map;
import java.util.Optional;

import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;

public interface CacheService {

    public void addUpdate(String token, ApiSession apiSession);

    /**
     * might not be there. called at authentication
     * 
     * @param token
     * @return ApiSession
     */
    public Optional<ApiSession> findApiSessionToken(String token);

    /**
     * must be there. called after token has been authenticated
     * 
     * @param token
     * @return ApiSession
     */
    public ApiSession getApiSessionToken(String token);

    public long delete(String token);

    public Map<Object, Object> findAllApiSessions();
    
}
