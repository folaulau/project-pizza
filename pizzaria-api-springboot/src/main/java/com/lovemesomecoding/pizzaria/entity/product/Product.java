package com.lovemesomecoding.pizzaria.entity.product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.user.UserMaritalStatus;
import com.lovemesomecoding.pizzaria.utils.ApiSessionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author folaukaveinga
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@SQLDelete(sql = "UPDATE product SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "product", indexes = {@Index(columnList = "uuid")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @Column(name = "name")
    private String            name;

    @ElementCollection
    private Set<String>       pages;

    // pizza, drink, chicken
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductType       type;

    // i.e pizza[cheese,pepperoni,etc]
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory   category;

    // image of the product
    @Column(name = "image_url")
    private String            imageUrl;

    // possible sizes
    @ElementCollection
    private Set<String>       sizes;

    // star rating
    @Column(name = "rating")
    private Integer           rating;

    @Column(name = "price")
    private Double            price;

    // names of people whose products are on the site
    @Column(name = "vendor")
    private String            vendor;

    @Column(name = "description", length = 2500)
    private String            description;

    @Type(type = "true_false")
    @Column(name = "active", nullable = false)
    private Boolean           active;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime              createdAt;

    @Column(name = "created_by", updatable = false, nullable = false)
    private Long              createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    private LocalDateTime              updatedAt;
    
    
    public Product(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.uuid).append(this.name).toHashCode();
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
        Product other = (Product) obj;
        return new EqualsBuilder().append(this.id, other.id).append(this.uuid, other.uuid).append(this.name, other.name).isEquals();
    }

    @PrePersist
    private void prePersist() {
        if (this.active == null) {
            this.active = true;
        }

        if(this.createdBy==null) {
            long userId = ApiSessionUtils.getUserId();

            this.createdBy = new Long(userId);
        }
        

        this.uuid = "product-" + UUID.randomUUID().toString();
    }

    @PreUpdate
    private void preUpdate() {

        if (this.uuid == null || this.uuid.length() == 0) {
            this.uuid = "product-" + UUID.randomUUID().toString();
        }

    }

}
