package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.user.UserGender;

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
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            uuid;

    private String            firstName;

    private String            lastName;

    private String            email;

    private String            phoneNumber;

    private LocalDate              dateOfBirth;

    private UserGender        gender;

    private AddressUpdateDTO  address;

    private String            aboutMe;

}
