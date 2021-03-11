package com.lovemesomecoding.pizzaria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
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
public class ApiDefaultResponseDTO {

    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    protected String           message;

}
