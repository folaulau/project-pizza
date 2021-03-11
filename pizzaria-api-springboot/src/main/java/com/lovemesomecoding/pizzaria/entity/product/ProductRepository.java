package com.lovemesomecoding.pizzaria.entity.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.String;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByUuid(String uuid);
	
	Page<Product> findByActive(Boolean active, Pageable page);
}
