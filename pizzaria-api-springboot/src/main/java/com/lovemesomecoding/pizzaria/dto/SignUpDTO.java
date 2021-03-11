package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.security.AuthenticationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(value = Include.NON_NULL)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO implements Serializable {

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    private AuthenticationType type;

    private String             email;

    private String             password;

    /**
     * Third party code
     */

    private String             thirdPartyAuthCode;

}
