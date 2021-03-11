package com.lovemesomecoding.pizzaria.entity.order.lineitem;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.order.Order;
import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.user.UserGender;
import com.lovemesomecoding.pizzaria.entity.user.UserMaritalStatus;
import com.lovemesomecoding.pizzaria.entity.user.UserStatus;
import com.lovemesomecoding.pizzaria.entity.user.role.Role;
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
@SQLDelete(sql = "UPDATE order_line_item SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "order_line_item", indexes = {@Index(columnList = "uuid")})
public class LineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id", nullable = false)
    private Product           product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order             order;

    @Column(name = "count")
    private int               count;

    @Column(name = "total")
    private double            total;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @Column(name = "deleted_at")
    private Date              deletedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date              createdAt;

    public double getTotal() {
        if (this.product != null) {
            this.total = this.product.getPrice() * this.count;
        }
        return total;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.product).toHashCode();
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
        LineItem other = (LineItem) obj;
        return new EqualsBuilder().append(this.product, other.product).isEquals();
    }

    @PrePersist
    private void preCreate() {
        this.uuid = "line-item-" + UUID.randomUUID();
        if (this.product != null) {
            this.total = MathUtils.getTwoDecimalPlaces(this.product.getPrice() * this.count);
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (this.product != null) {
            this.total = MathUtils.getTwoDecimalPlaces(this.product.getPrice() * this.count);
        }
    }

}
