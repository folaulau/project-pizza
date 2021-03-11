package com.lovemesomecoding.pizzaria.entity.user.session;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.lang.String;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Page<UserSession> findByUserUuid(String userUuid, Pageable pageable);

    Page<UserSession> findByUserUuidAndActive(String userUuid, Boolean active, Pageable pageable);

    Page<UserSession> findByUserId(Long userId, Pageable pageable);

    Page<UserSession> findByUserIdAndActive(Long memberId, Boolean active, Pageable pageable);

    UserSession findByAuthToken(String authtoken);
    
    List<UserSession> findByRefreshToken(String refreshToken);
    
}
