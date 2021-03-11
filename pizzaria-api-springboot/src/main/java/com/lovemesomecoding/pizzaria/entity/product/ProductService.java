package com.lovemesomecoding.pizzaria.entity.product;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	Product save(Product product);
	
	Product update(Product product);
	
	List<Product> update(List<Product> products);
	
	Product getById(Long id);
	
	Product getByUid(String uid);
	
	Page<Product> getByPage(Pageable page);
	
	String uploadProductProfileImg(File file);
}
