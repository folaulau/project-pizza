package com.lovemesomecoding.pizzaria.security.jwt;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * https://tools.ietf.org/html/rfc7519 JWT Payload <br>
 * 1. sub - user uid <br>
 * 
 * @author folaukaveinga
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class JwtPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    // issuer
    private String            iss;

    // id of jwt
    private String            jti;

    // user uuid
    private String            sub;

    // issued at
    private Date              iat;

    // expired at
    private Date              exp;

    // not before
    private Date              nbf;

    public JwtPayload(String jti, String uuid) {
        this(null, jti, uuid, null, null, null);
    }

    public String toJson() {
        try {
            return ObjMapperUtils.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("JwtPayload - toJson - JsonProcessingException, msg: " + e.getLocalizedMessage());
            return "{}";
        }
    }

}
