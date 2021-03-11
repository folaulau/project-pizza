package com.lovemesomecoding.pizzaria.entity.user.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@DynamicUpdate
@Entity
@Table(name = "user_session", indexes = {@Index(columnList = "user_id"), @Index(columnList = "user_uuid")})
public class UserSession implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long              id;

    @Column(name = "user_uuid", nullable = false, updatable = false)
    private String            userUuid;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long              userId;

    @Lob
    @Column(name = "auth_token", nullable = false, updatable = false, length = 2000, columnDefinition = "BLOB")
    private String            authToken;
    
    @Column(name = "refresh_token", nullable = false, updatable = false)
    private String            refreshToken;

    @Column(name = "user_agent", nullable = false, updatable = false, length = 1000)
    private String            userAgent;

    @Column(name = "device_app_name")
    private String            deviceAppName;

    @Column(name = "device_app_version")
    private String            deviceAppVersion;

    @Column(name = "device_OS_name")
    private String            deviceOSName;

    @Column(name = "device_OS_version")
    private String            deviceOSVersion;

    @Column(name = "ip_address")
    private String            ipAddress;

    // ======================location=======================
    @Column(name = "country")
    private String            country;

    @Column(name = "street")
    private String            street;

    @Column(name = "street2")
    private String            street2;

    @Column(name = "city")
    private String            city;

    // state or region
    @Column(name = "state")
    private String            state;

    @Column(name = "county")
    private String            county;

    @Column(name = "zipcode")
    private String            zipcode;

    @Column(name = "timezone")
    private String            timezone;

    @Column(name = "lat")
    private Double            lat;

    @Column(name = "lng")
    private Double            lng;
    // ======================*location*=======================

    @Column(name = "log_in_time")
    private LocalDateTime     loginTime;

    @Column(name = "log_out_time")
    private LocalDateTime     logoutTime;

    @Column(name = "expired_at")
    private LocalDateTime     expiredAt;

    @Type(type = "true_false")
    @Column(name = "active", nullable = false, updatable = true)
    private Boolean           active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime     updatedAt;

    /*
     * uuid of the user updating this user
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String            updatedBy;

    @Column(name = "updated_by_type")
    private String            updatedByType;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.authToken).toHashCode();
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
        UserSession other = (UserSession) obj;
        return new EqualsBuilder().append(this.id, other.id).append(this.authToken, other.authToken).isEquals();
    }

    @PrePersist
    private void preCreate() {
        ApiSession currentUser = ApiSessionUtils.getApiSession();

        if (currentUser != null) {
            this.updatedBy = currentUser.getUserUuid();
            this.updatedByType = currentUser.getRolesAsStr();
        } else {
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