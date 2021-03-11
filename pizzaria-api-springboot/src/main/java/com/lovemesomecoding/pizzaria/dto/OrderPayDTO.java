package com.lovemesomecoding.pizzaria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class OrderPayDTO {
	
	private boolean savePaymentMethodForFutureUse;
	
	private boolean useCardOnFile;
	
	private boolean useProfileAddress;
	
	private CardPMCreateDTO paymentMethod;
	
	private OrderUidDTO order;
	
	private AddressDTO location;

	public OrderPayDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isSavePaymentMethodForFutureUse() {
		return savePaymentMethodForFutureUse;
	}

	public void setSavePaymentMethodForFutureUse(boolean savePaymentMethodForFutureUse) {
		this.savePaymentMethodForFutureUse = savePaymentMethodForFutureUse;
	}

	public CardPMCreateDTO getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(CardPMCreateDTO paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderUidDTO getOrder() {
		return order;
	}

	public void setOrder(OrderUidDTO order) {
		this.order = order;
	}

	public AddressDTO getLocation() {
		return location;
	}

	public void setLocation(AddressDTO location) {
		this.location = location;
	}

	public boolean isUseProfileAddress() {
		return useProfileAddress;
	}

	public void setUseProfileAddress(boolean useProfileAddress) {
		this.useProfileAddress = useProfileAddress;
	}

	public boolean isUseCardOnFile() {
		return useCardOnFile;
	}

	public void setUseCardOnFile(boolean useCardOnFile) {
		this.useCardOnFile = useCardOnFile;
	}
}
