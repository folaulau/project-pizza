package com.lovemesomecoding.pizzaria.paymentgateway.payment;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.paymentmethod.OrderPaymentMethod;

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
@Entity
@SQLDelete(sql = "UPDATE payment SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "payment", indexes = {@Index(columnList = "uuid")})
public class Payment implements Serializable {

    /**
     * 
     */
    private static final long  serialVersionUID = 1L;

    @JsonIgnore
    @Transient
    private Logger             log              = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long               id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String             uuid;

    @Column(name = "type")
    private String             type;

    @Column(name = "order_id")
    private Long               orderId;

    @Type(type = "true_false")
    @Column(name = "paid")
    private Boolean            paid;

    @Column(name = "description")
    private String             description;

    @Column(name = "amount_paid")
    private Double             amountPaid;

    @Column(name = "stripe_charge_id")
    private String             stripeChargeId;

    @Embedded
    private OrderPaymentMethod paymentMethod;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean            deleted;

    @Column(name = "deleted_at")
    private Date               deletedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date               createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", updatable = true, nullable = false)
    private Date               updatedAt;

    @PrePersist
    private void prePersist() {
        this.uuid = "payment-" + UUID.randomUUID().toString();
    }

}
