package com.lovemesomecoding.pizzaria.entity.product.deal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.product.ProductCategory;
import com.lovemesomecoding.pizzaria.entity.product.ProductType;
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
@Entity
@SQLDelete(sql = "UPDATE product_deal SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "product_deal", indexes = {@Index(columnList = "uuid")})
public class Deal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @JoinTable(name = "deal_products")
    @ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Set<Product>      products;

    @Column(name = "cost", nullable = false)
    private Double            cost;

    @Column(name = "name")
    private String            name;

    // image of the deal
    @Column(name = "image_url")
    private String            imageUrl;

    // From 100 to 1
    @Column(name = "rating", unique = true)
    private Integer           rating;

    @Type(type = "true_false")
    @Column(name = "active", nullable = false)
    private Boolean           active;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @Column(name = "created_by", updatable = false, nullable = false)
    private Long              createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime              createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = false)
    private LocalDateTime              updatedAt;

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.add(product);
    }

    @PrePersist
    private void prePersist() {
        if (this.active == null) {
            this.active = true;
        }

        if (this.createdBy == null) {
            long userId = ApiSessionUtils.getUserId();

            this.createdBy = new Long(userId);
        }

        this.uuid = "deal-" + UUID.randomUUID().toString();
    }

}
