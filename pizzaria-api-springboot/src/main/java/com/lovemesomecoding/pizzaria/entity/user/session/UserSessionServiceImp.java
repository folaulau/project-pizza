package com.lovemesomecoding.pizzaria.entity.user.session;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.lovemesomecoding.pizzaria.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserSessionServiceImp implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    private UserSession save(UserSession userSession) {
        return this.userSessionRepository.saveAndFlush(userSession);
    }

    @Override
    public void update(UserSession userSession) {
        this.save(userSession);
    }

    @Override
    public boolean signIn(UserSession memberSession) {
        log.debug("signIn(..)");
        memberSession.setActive(true);
        memberSession.setLoginTime(LocalDateTime.now());

        memberSession = this.save(memberSession);
        return true;
    }

    @Override
    public boolean signOut(String authToken) {
        log.debug("signOut(..)");
        UserSession memberSession = userSessionRepository.findByAuthToken(authToken);

        if (memberSession != null && memberSession.getId() != null && memberSession.getId() > 0) {
            memberSession.setLogoutTime(LocalDateTime.now());
            memberSession.setActive(false);
            this.save(memberSession);
            return true;
        }
        return false;

    }

    @Override
    public void expire(String authToken) {
        UserSession memberSession = userSessionRepository.findByAuthToken(authToken);
        if (memberSession != null && memberSession.getId() != null && memberSession.getId() > 0) {
            memberSession.setExpiredAt(LocalDateTime.now());
            memberSession.setActive(false);
            this.save(memberSession);
        }
    }

    @Override
    public Page<UserSession> getSessionsByUserId(Long userId, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<UserSession> getActiveSessionsByUserId(Long userId, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserIdAndActive(userId, true, pageable);
    }

    @Override
    public Page<UserSession> getSessionsByUserUuid(String userUuid, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserUuid(userUuid, pageable);
    }

    @Override
    public Page<UserSession> getActiveSessionsByUserUuid(String userUuid, Pageable pageable) {
        // TODO Auto-generated method stub
        return userSessionRepository.findByUserUuidAndActive(userUuid, true, pageable);
    }

    @Override
    public UserSession getLatestSessionByRefreshToken(String refreshToken) {
        List<UserSession> userSessions = userSessionRepository.findByRefreshToken(refreshToken);

        if (userSessions == null || userSessions.size() == 0) {
            return null;
        }
        
        return userSessions.stream().findFirst().get();

    }

}
