package com.lovemesomecoding.pizzaria.paymentgateway.payment;

import com.lovemesomecoding.pizzaria.entity.order.Order;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethod;

public interface PaymentService {

	Payment payOrder(Order order, PaymentMethod paymentMethod);
	
	Payment payOrder(String paymentGatewayId, Order order, PaymentMethod paymentMethod);
}
