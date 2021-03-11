package com.lovemesomecoding.pizzaria.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
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
public class OrderAdminSearchResponseItemDTO {

    private BigInteger    orderId;
    private String        orderUid;
    private Double        total;
    private Character     delivered;
    private Character     paid;
    private LocalDateTime paidAt;
    private LocalDateTime updatedAt;
    private String        city;
    private String        state;
    private String        customerUid;
    private String        customerName;

}
