package com.lovemesomecoding.pizzaria.entity.order;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.pizzaria.cache.CacheService;
import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;
import com.lovemesomecoding.pizzaria.utils.ApiSessionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class OrderControllerAspect {

    @Autowired
    private CacheService cacheService;

    @Before("execution(* com.lovemesomecoding.pizzaria.entity.order.OrderController.*(..))")
    public void before(JoinPoint joinPoint) {

        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            Object tokenObj = joinPoint.getArgs()[0];

            if (tokenObj != null && tokenObj.toString() != null && tokenObj.toString().length() > 0) {
                // log.debug(" auth token {} ", tokenObj.toString());
                ApiSession apiSession = cacheService.getApiSessionToken(tokenObj.toString());
                ApiSessionUtils.setSessionToken(apiSession);
            }
        }

    }
}
