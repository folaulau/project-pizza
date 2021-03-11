package com.lovemesomecoding.pizzaria.paymentmethod;

import java.io.Serializable;
import java.util.Date;
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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.exception.ApiException;

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
@SQLDelete(sql = "UPDATE payment_method SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "payment_method", indexes = {@Index(columnList = "uuid")})
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Transient
    private Logger            log              = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long              id;

    @Column(name = "type", updatable = false, nullable = false)
    private String            type;

    @Column(name = "uuid", unique = true, updatable = false, nullable = false)
    private String            uuid;

    @Column(name = "name")
    private String            name;

    @Column(name = "last4")
    private String            last4;

    @Column(name = "position")
    private Integer           position;

    @Column(name = "brand")
    private String            brand;

    @Column(name = "source_token")
    private String            sourceToken;

    @Column(name = "payment_gateway_id")
    private String            paymentGatewayId;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date              createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", updatable = true)
    private Date              updatedAt;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User              user;

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.uuid).append(this.user).toHashCode();
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
        PaymentMethod other = (PaymentMethod) obj;
        return new EqualsBuilder().append(this.id, other.id).append(this.uuid, other.uuid).append(this.user, other.user).isEquals();
    }

    @PrePersist
    @PreUpdate
    private void preCreateUpdate() {
        log.debug("preCreateUpdate...");
        if (this.type == null) {
            throw new ApiException("type is null");
        }
        if (this.uuid == null || this.uuid.length() == 0) {
            this.uuid = "pm-" + UUID.randomUUID().toString();
        }
    }

}
