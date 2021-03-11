package com.lovemesomecoding.pizzaria.security.jwt;

import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenService {

    private static final String      secret    = "pizzaria-api-springboot";

    private static final Algorithm   ALGORITHM = Algorithm.HMAC256(secret);

    private static final String      ISSUER    = "pizzaria-api";

    private static final int         LIFE_TIME = 100;

    private static final JWTVerifier VERIFIER  = JWT.require(ALGORITHM).withIssuer(ISSUER).build();

    /**
     * Use JwtPayload to generate a jwt token
     * 
     * @param payload
     *            - JwtPayload
     * @return token - String
     */
    public String generateToken(JwtPayload payload) {

        try {
            String token = JWT.create()
                    .withExpiresAt(DateUtils.addMinutes(new Date(), LIFE_TIME))
                    .withIssuedAt(new Date())
                    .withJWTId(payload.getJti())
                    .withSubject(payload.getSub())
                    .withIssuer(ISSUER)
                    .sign(ALGORITHM);
            return token;
        } catch (JWTCreationException e) {
            log.warn("JWTCreationException, msg: {}", e.getLocalizedMessage());
            return null;
        } catch (Exception e) {
            log.warn("Exception, msg: {}", e.getLocalizedMessage());
            return null;
        }
    }

    public JwtPayload getPayloadByToken(String token) {
        if (token == null || token.length() == 0) {
            return null;
        }

        try {

            // Reusable verifier instance
            DecodedJWT jwt = VERIFIER.verify(token);
            if (jwt != null) {
                JwtPayload jwtPayload = new JwtPayload();
                jwtPayload.setExp(jwt.getExpiresAt());
                jwtPayload.setIss(jwt.getIssuer());
                jwtPayload.setJti(jwt.getId());
                jwtPayload.setIat(jwt.getIssuedAt());
                jwtPayload.setSub(jwt.getSubject());

                return jwtPayload;
            }
        } catch (Exception e) {
            log.warn("Exception, msg: {}", e.getLocalizedMessage());
        }
        return null;
    }
}
