package com.lovemesomecoding.pizzaria.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.user.UserGender;
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
public class PaymentReadDTO {

    private Long                      id;

    private String                    uid;

    private String                    type;

    private Boolean                   paid;

    private String                    description;

    private Double                    amountPaid;

    private OrderPaymentMethodReadDTO paymentMethod;

    private LocalDateTime             createdAt;

    private LocalDateTime             updatedAt;

}
