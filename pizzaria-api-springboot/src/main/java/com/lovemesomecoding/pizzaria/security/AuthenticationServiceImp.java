package com.lovemesomecoding.pizzaria.security;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.cache.CacheService;
import com.lovemesomecoding.pizzaria.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.UserDAO;
import com.lovemesomecoding.pizzaria.entity.user.session.UserSession;
import com.lovemesomecoding.pizzaria.entity.user.session.UserSessionService;
import com.lovemesomecoding.pizzaria.exception.ApiErrorResponse;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.security.jwt.JwtPayload;
import com.lovemesomecoding.pizzaria.security.jwt.JwtTokenService;
import com.lovemesomecoding.pizzaria.utils.ApiSessionUtils;
import com.lovemesomecoding.pizzaria.utils.HttpUtils;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;
import com.lovemesomecoding.pizzaria.utils.RandomGeneratorUtils;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthenticationServiceImp implements AuthenticationService {

    @Autowired
    private HttpServletRequest  request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private EntityDTOMapper     entityMapper;

    @Autowired
    private CacheService        cacheService;

    @Autowired
    private JwtTokenService     jwtTokenService;

    @Autowired
    private UserSessionService  userSessionService;

    @Autowired
    private UserDAO             userDAO;

    @Override
    public AuthenticationResponseDTO authenticate(User user) {
        log.debug("authenticate, user={}", ObjMapperUtils.toJson(user));
        String userAgent = HttpUtils.getRequestUserAgent(request);
        String userIPAddress = HttpUtils.getRequestIP(request);

        String jwtToken = jwtTokenService.generateToken(new JwtPayload(RandomGeneratorUtils.getJWTId(), user.getUuid()));
        String refreshToken = "ref-token-" + UUID.randomUUID().toString();

        AuthenticationResponseDTO authenticatedSessionDTO = entityMapper.mapUserToUserAuthSuccessDTO(user);
        authenticatedSessionDTO.setToken(jwtToken);
        authenticatedSessionDTO.setRefreshToken(refreshToken);

        ApiSession apiSession = new ApiSession();
        apiSession.setToken(jwtToken);
        apiSession.setUserId(user.getId());
        apiSession.setUserUuid(user.getUuid());
        apiSession.setUserRoles(user.generateStrRoles());
        apiSession.setClientIPAddress(userIPAddress);
        apiSession.setLastUsedTime(LocalDateTime.now());

        // next 24 hours
        apiSession.setExpiredTime(LocalDateTime.now().plusDays(1));
        apiSession.setDeviceId(userAgent);

        cacheService.addUpdate(jwtToken, apiSession);

        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setUserUuid(user.getUuid());
        userSession.setAuthToken(jwtToken);
        userSession.setLoginTime(LocalDateTime.now());
        userSession.setUserAgent(userAgent);
        userSession.setRefreshToken(refreshToken);

        userSessionService.signIn(userSession);

        return authenticatedSessionDTO;
    }

    @Override
    public boolean logOutUser(String token) {
        log.debug("logOutUser, token={}", token);
        ApiSession apiSession = cacheService.getApiSessionToken(token);

        if (apiSession != null) {
            long deleteCount = cacheService.delete(token);

            log.debug("deleteCount={}", deleteCount);

            boolean signOutInDB = userSessionService.signOut(token);

            log.debug("signOutInDB={}", signOutInDB);

            return deleteCount > 0;
        } else {
            return false;
        }

    }

    @Override
    public boolean authenticateRequest(String token, JwtPayload jwtPayload) {

        log.debug("jwtPayload={}", ObjMapperUtils.toJson(jwtPayload));

        if (jwtPayload == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Invalid token in header";
            log.debug("Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Unable to verify token"));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        ApiSession apiSession = cacheService.getApiSessionToken(token);

        if (apiSession == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Invalid token";
            log.debug("Token does not exist in cache. Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Session not found in cache for requested token"));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        String userAgent = HttpUtils.getRequestUserAgent(request);

        if (apiSession.getDeviceId() != null && apiSession.getDeviceId().equals(userAgent) == false) {
            log.warn("Device id or user-agent does not match the request user-agent");
        }

        log.debug("expiredTime={}, now={}", apiSession.getExpiredTime().toString(), new Date().toInstant().toString());

        // is now after the expiration time?
        if (LocalDateTime.now().isAfter(apiSession.getExpiredTime())) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Expired token";
            log.debug("Session expired. Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper()
                        .writeValue(response.getWriter(),
                                new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Token has been expired. expired at " + apiSession.getExpiredTime().toString()));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        /*
         * Valid token
         */

        ApiSessionUtils.setSessionToken(new WebAuthenticationDetailsSource().buildDetails(request), apiSession);

        return true;
    }

    @Override
    public AuthenticationResponseDTO refreshAuthToken(String refreshToken) {
        // TODO Auto-generated method stub

        UserSession userSession = userSessionService.getLatestSessionByRefreshToken(refreshToken);

        if (userSession == null) {
            throw new ApiException("Refresh token not found");
        }

        if (userSession.getActive() != null && userSession.getActive().equals(false) && userSession.getLogoutTime() != null) {
            throw new ApiException("Refresh token has been logged out");
        }

        User user = userDAO.getByUuid(userSession.getUserUuid());

        log.info("user={}", ObjMapperUtils.toJson(user));

        String jwtToken = jwtTokenService.generateToken(new JwtPayload(RandomGeneratorUtils.getJWTId(), user.getUuid()));

        AuthenticationResponseDTO authenticatedSessionDTO = entityMapper.mapUserToUserAuthSuccessDTO(user);
        authenticatedSessionDTO.setToken(jwtToken);
        authenticatedSessionDTO.setRefreshToken(refreshToken);

        /**
         * Delete old token
         */

        cacheService.delete(userSession.getAuthToken());

        /**
         * Create new token
         */
        String userAgent = HttpUtils.getRequestUserAgent(request);
        String userIPAddress = HttpUtils.getRequestIP(request);

        ApiSession apiSession = new ApiSession();
        apiSession.setToken(jwtToken);
        apiSession.setUserId(user.getId());
        apiSession.setUserUuid(user.getUuid());
        apiSession.setUserRoles(user.generateStrRoles());
        apiSession.setClientIPAddress(userIPAddress);
        apiSession.setLastUsedTime(LocalDateTime.now());

        // next 24 hours
        apiSession.setExpiredTime(LocalDateTime.now().plusDays(1));
        apiSession.setDeviceId(userAgent);

        cacheService.addUpdate(jwtToken, apiSession);
        
        // use the new auth token
        userSession.setAuthToken(jwtToken);
        this.userSessionService.update(userSession);

        return authenticatedSessionDTO;
    }

}
