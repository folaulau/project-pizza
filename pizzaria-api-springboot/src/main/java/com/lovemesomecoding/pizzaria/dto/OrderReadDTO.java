package com.lovemesomecoding.pizzaria.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

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
public class OrderReadDTO {

    private Long             id;

    private String           uid;

    private Set<LineItemDTO> lineItems;

    private int              totalItemCount;

    private double           total;

    private AddressDTO       location;

    private boolean          delivered;

    private LocalDateTime    deliveredAt;

    private LocalDateTime    createdAt;

    private LocalDateTime    updatedAt;

    private PaymentReadDTO   payment;

    private boolean          paid;

    private LocalDateTime    paidAt;

}
