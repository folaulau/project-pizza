package com.lovemesomecoding.pizzaria.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class OrderCreateDTO {

	private Set<LineItemDTO> lineItems;
	
	private PaymentMethodReadDTO paymentMethod;

	public OrderCreateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<LineItemDTO> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<LineItemDTO> lineItems) {
		this.lineItems = lineItems;
	}

	public PaymentMethodReadDTO getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodReadDTO paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
}
