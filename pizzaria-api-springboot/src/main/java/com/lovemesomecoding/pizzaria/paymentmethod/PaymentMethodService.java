package com.lovemesomecoding.pizzaria.paymentmethod;

import java.util.List;

public interface PaymentMethodService {

	PaymentMethod create(PaymentMethod paymentMethod);
	PaymentMethod update(PaymentMethod paymentMethod);
	PaymentMethod getById(Long id);
	PaymentMethod getByUid(String uid);
	
	List<PaymentMethod> getByCustomerId(Long customerId);
	List<PaymentMethod> getByCustomerUid(String customerUid);
	
	List<PaymentMethod> add(Long memberId, String paymentGatewayId, PaymentMethod paymentMethod);
	List<PaymentMethod> remove(Long memberId, PaymentMethod paymentMethod);
	List<PaymentMethod> update(Long memberId, PaymentMethod currentPaymentMethod,PaymentMethod newPaymentMethod);
}
