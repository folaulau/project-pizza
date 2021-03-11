package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.user.UserStatus;

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
public class AuthenticationResponseDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Jwt token used for API calls.
     */
    private String            token;

    private String            refreshToken;

    private Long              id;

    private String            uuid;

    private String            firstName;

    private String            middleName;

    private String            lastName;

    private String            email;

    private UserStatus        status;

    private AddressDTO        address;

    private String            profileImageUrl;

}
