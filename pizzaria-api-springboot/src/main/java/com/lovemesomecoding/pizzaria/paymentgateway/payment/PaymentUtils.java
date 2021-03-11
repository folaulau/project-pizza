package com.lovemesomecoding.pizzaria.paymentgateway.payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentUtils {

	
	public static Map<String, Object> generateOrderPaymentMetadata(Payment payment){
		Map<String, Object> metadata = new HashMap<>();
		try {
			metadata.put("description", payment.getDescription());
			metadata.put("orderId", payment.getOrderId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return metadata;
		
	}
}
