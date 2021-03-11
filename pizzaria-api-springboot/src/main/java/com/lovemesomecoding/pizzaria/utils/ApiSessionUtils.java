package com.lovemesomecoding.pizzaria.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.security.jwt.JwtPayload;
import com.lovemesomecoding.pizzaria.security.jwt.JwtTokenService;

public interface ApiSessionUtils {

    static Logger log = LoggerFactory.getLogger(ApiSessionUtils.class);

    // /**
    // * save sidecarBrokerApiSession as principal for duration of rest call
    // *
    // * @param sidecarBrokerApiSession
    // */
    public static void setSessionToken(WebAuthenticationDetails authDetails, ApiSession apiSession) {

        if (apiSession == null) {
            return;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (apiSession.getUserRoles() != null || apiSession.getUserRoles().isEmpty() == false) {
            for (String role : apiSession.getUserRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
        }

        UsernamePasswordAuthenticationToken updateAuth = new UsernamePasswordAuthenticationToken(apiSession, apiSession.getUserUuid(), authorities);

        if (authDetails != null) {
            updateAuth.setDetails(authDetails);
        }

        SecurityContextHolder.getContext().setAuthentication(updateAuth);
    }

    public static void setSessionToken(ApiSession apiSession) {
        setSessionToken(null, apiSession);
    }

    public static ApiSession getApiSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                ApiSession session = (ApiSession) auth.getPrincipal();
                return session;
            } catch (Exception e) {
                log.warn("Exception, msg={}", e.getLocalizedMessage());
            }

        }
        return null;
    }

    public static String getUserUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                ApiSession session = (ApiSession) auth.getPrincipal();
                return session.getUserUuid();
            } catch (Exception e) {
                log.warn("Exception, msg={}", e.getLocalizedMessage());
            }

        }
        return null;
    }

    public static String getActiveProfile(Environment environment) {
        String env = "";
        try {
            env = Arrays.asList(environment.getActiveProfiles()).get(0);
        } catch (Exception e) {
        }
        return env;
    }

    public static long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                ApiSession session = (ApiSession) auth.getPrincipal();
                return session.getUserId();
            } catch (Exception e) {
                log.warn("Exception, msg={}", e.getLocalizedMessage());
            }

        }
        return 0;
    }

    public static User getUser(Long userId) {
        // TODO Auto-generated method stub
        return new User(userId);
    }

    public static User getUser() {
        // TODO Auto-generated method stub
        return new User(getUserId());
    }
}
