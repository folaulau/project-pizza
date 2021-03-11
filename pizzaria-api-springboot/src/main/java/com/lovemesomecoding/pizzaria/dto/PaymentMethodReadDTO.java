package com.lovemesomecoding.pizzaria.dto;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class PaymentMethodReadDTO {

    private Long          id;

    private String        type;

    private String        uid;

    private String        name;

    private String        last4;

    private int           position;

    private String        brand;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean       deleted;

    private String        sourceToken;

    private String        paymentGatewayId;

}
