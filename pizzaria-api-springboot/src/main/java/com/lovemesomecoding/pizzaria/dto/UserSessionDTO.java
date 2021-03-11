package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class UserSessionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;

    private String            userUuid;

    private Long              userId;

    private String            authToken;

    private String            userAgent;

    private String            ipAddress;

    private String            country;

    private String            street;

    private String            street2;

    private String            city;

    private String            state;

    private String            county;

    private String            zipcode;

    private String            timezone;

    private Double            lat;

    private Double            lng;

    private LocalDateTime              loginTime;

    private LocalDateTime              logoutTime;

    private LocalDateTime              expired;

    private Boolean           active;
}
