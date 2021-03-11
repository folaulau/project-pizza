package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.util.Set;

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
public class ProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            uuid;

    private String            name;

    private Set<String>       pages;

    private String            type;

    private String            category;

    private String            imageUrl;

    private Set<String>       sizes;

    private Integer           rating;

    private Double            price;

    private String            vendor;

    private String            description;

    private Boolean           active;

}
