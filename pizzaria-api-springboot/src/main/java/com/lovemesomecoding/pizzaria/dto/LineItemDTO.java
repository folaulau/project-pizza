package com.lovemesomecoding.pizzaria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class LineItemDTO {

	private Long id;
	
	private ProductReadDTO product;
	
	private int count;
	
	private double total;
	
	private String uid;

	public LineItemDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductReadDTO getProduct() {
		return product;
	}

	public void setProduct(ProductReadDTO product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
