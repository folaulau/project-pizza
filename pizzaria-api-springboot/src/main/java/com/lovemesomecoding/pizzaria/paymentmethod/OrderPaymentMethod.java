package com.lovemesomecoding.pizzaria.paymentmethod;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Embeddable
public class OrderPaymentMethod implements Serializable {

    @Transient
    @JsonIgnore
    private Logger            log              = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    @Column(name = "payment_method_id")
    private Long              id;

    @Column(name = "payment_gateway_id")
    private String            paymentGatewayId;

    @Column(name = "source_token")
    private String            sourceToken;

    @Column(name = "payment_method_type")
    private String            type;

    @Column(name = "payment_method_name")
    private String            name;

    @Column(name = "payment_method_last4")
    private String            last4;

    @Column(name = "payment_method_brand")
    private String            brand;

}
