package com.lovemesomecoding.pizzaria.entity.address;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.dto.helper.ApiSession;
import com.lovemesomecoding.pizzaria.utils.ApiSessionUtils;

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
@DynamicUpdate
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @Column(name = "street")
    private String            street;

    @Column(name = "street2")
    private String            street2;

    @Column(name = "city")
    private String            city;

    @Column(name = "state")
    private String            state;

    @Column(name = "zipcode")
    private String            zipcode;

    @Column(name = "country")
    private String            country;

    @Column(name = "longitude")
    private Double            longitude;

    @Column(name = "latitude")
    private Double            latitude;

    @Column(name = "timezone")
    private String            timezone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime     updatedAt;

    /*
     * uuid of the user creating this user
     */
    @CreatedBy
    @Column(name = "created_by_user", updatable = false)
    private String            createdByUser;

    /*
     * uuid of the user updating this user
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String            updatedBy;

    @Column(name = "updated_by_type")
    private String            updatedByType;

    public Map<String, Object> getPaymentGatewayAddress() {
        Map<String, Object> add = new HashMap<>();
        if (street != null && street.length() > 0) {
            add.put("line1", street);

            if (street2 != null && street2.length() > 0) {
                add.put("line2", street2);
            }

            if (city != null && city.length() > 0) {
                add.put("city", city);
            }

            if (country != null && country.length() > 0) {
                add.put("country", country);
            }

            if (zipcode != null && zipcode.length() > 0) {
                add.put("postal_code", zipcode);
            }
        }

        return add;
    }

    @PrePersist
    private void preCreate() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = "addr-" + UUID.randomUUID().toString();
        }

        ApiSession currentUser = ApiSessionUtils.getApiSession();

        if (currentUser != null) {
            this.createdByUser = currentUser.getUserUuid();
            this.updatedBy = currentUser.getUserUuid();
            this.updatedByType = currentUser.getRolesAsStr();
        } else {
            this.createdByUser = "SYSTEM";
            this.updatedBy = "SYSTEM";
            this.updatedByType = "SYSTEM";
        }

    }

    @PreUpdate
    private void preUpdate() {
        ApiSession currentUser = ApiSessionUtils.getApiSession();

        if (currentUser != null) {
            this.updatedBy = currentUser.getUserUuid();
            this.updatedByType = currentUser.getRolesAsStr();
        } else {
            this.updatedBy = "SYSTEM";
            this.updatedByType = "SYSTEM";
        }
    }

}
