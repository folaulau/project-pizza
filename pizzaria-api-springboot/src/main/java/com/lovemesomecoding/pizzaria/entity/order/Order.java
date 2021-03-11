package com.lovemesomecoding.pizzaria.entity.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.order.lineitem.LineItem;
import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.UserGender;
import com.lovemesomecoding.pizzaria.entity.user.UserMaritalStatus;
import com.lovemesomecoding.pizzaria.entity.user.UserStatus;
import com.lovemesomecoding.pizzaria.entity.user.role.Role;
import com.lovemesomecoding.pizzaria.paymentgateway.payment.Payment;
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
@Entity
@SQLDelete(sql = "UPDATE customer_order SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "customer_order", indexes = {@Index(columnList = "uuid")})
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<LineItem>     lineItems;

    @JsonIgnoreProperties(value = {"orders"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "customer_id", nullable = true)
    private User              customer;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "payment_id")
    private Payment           payment;

    // can be admin
    @JsonIgnoreProperties(value = {"orders"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "helper_user_id", nullable = true)
    private User              helper;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_address_id")
    private Address           location;

    @Type(type = "true_false")
    @Column(name = "delivered", nullable = false)
    private boolean           delivered;

    @Type(type = "true_false")
    @Column(name = "current", nullable = false)
    private boolean           current;

    @Column(name = "delivered_at")
    private LocalDateTime              deliveredAt;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @Column(name = "deleted_at")
    private LocalDateTime              deletedAt;

    @Type(type = "true_false")
    @Column(name = "paid", nullable = false)
    private boolean           paid;

    @Column(name = "paid_at")
    private LocalDateTime              paidAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime              createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    private LocalDateTime              updatedAt;

    @Column(name = "total")
    private double            total;

    public double getTotal() {
        this.total = 0;
        if (this.lineItems != null && this.lineItems.size() > 0) {
            this.lineItems.forEach((lineItem) -> {
                if (lineItem != null) {
                    this.total += lineItem.getTotal();
                }
            });
            // log.debug("total={}", this.total);
            this.total = MathUtils.getTwoDecimalPlaces(this.total);
            // log.debug("rounded total={}", this.total);
        }
        return this.total;
    }

    public int getTotalItemCount() {
        int totalItemCount = 0;
        if (this.lineItems != null) {
            for (LineItem lineItem : this.lineItems) {
                totalItemCount += lineItem.getCount();
            }
        }
        return totalItemCount;
    }

    public void stampPayment(Payment payment) {
        this.payment = payment;
        this.paid = payment.getPaid();
        this.paidAt = LocalDateTime.now();
    }

    public void addLineItem(LineItem lineItem) {
        if (this.lineItems == null) {
            this.lineItems = new HashSet<>();
        }
        this.lineItems.add(lineItem);
    }

    public LineItem getLineItem(Product product) {
        LineItem lineItem = null;
        if (this.lineItems != null) {
            for (LineItem lI : this.lineItems) {
                if (product.equals(lI.getProduct())) {
                    lineItem = lI;
                    break;
                }
            }
        }
        return lineItem;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.uuid).toHashCode();

        // return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Order other = (Order) obj;
        return new EqualsBuilder().append(this.id, other.id).append(this.uuid, other.uuid).isEquals();
    }

    public void removeLineItem(LineItem lineItem) {
        lineItem.setDeleted(true);
        lineItem.setDeletedAt(new Date());
        this.lineItems.remove(lineItem);
    }

    @PrePersist
    private void preCreate() {
        this.current = true;
        this.uuid = "order-" + UUID.randomUUID();
    }
}
