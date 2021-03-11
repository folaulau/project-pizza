package com.lovemesomecoding.pizzaria.entity.user.session;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lovemesomecoding.pizzaria.dto.AuthenticationResponseDTO;

public interface UserSessionService {

    boolean signIn(UserSession userSession);

    boolean signOut(String authToken);

    void expire(String authToken);

    Page<UserSession> getSessionsByUserId(Long userId, Pageable pageable);

    // Get logged in sessions
    Page<UserSession> getActiveSessionsByUserId(Long userId, Pageable pageable);

    Page<UserSession> getSessionsByUserUuid(String userUuid, Pageable pageable);

    // Get logged in sessions
    Page<UserSession> getActiveSessionsByUserUuid(String userUuid, Pageable pageable);

    UserSession getLatestSessionByRefreshToken(String refreshToken);

    void update(UserSession userSession);
}
