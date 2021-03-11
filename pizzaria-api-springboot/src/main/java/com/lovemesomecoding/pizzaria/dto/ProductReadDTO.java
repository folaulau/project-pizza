package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.utils.MathUtils;

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
public class ProductReadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              id;

    private String            uuid;

    private String            name;

    private String            imageUrl;

    private Set<String>       sizes;

    private String            sizeAsString;

    private Set<String>       pages;

    private String            type;

    private String            category;

    private Integer           rating;

    private Double            price;

    private String            priceAsString;

    private String            vendor;

    private String            description;

    private Boolean           active;

    private boolean           deleted;

    private LocalDateTime              createdAt;

    private LocalDateTime              updatedAt;

    public String getPriceAsString() {
        if (price != null) {
            this.priceAsString = MathUtils.getTwoDecimalPlacesAsString(price);
        }
        return priceAsString;
    }

    public void setPrice(Double price) {
        this.price = price;

        if (price != null) {
            this.priceAsString = MathUtils.getTwoDecimalPlacesAsString(price);
        }
    }

}
