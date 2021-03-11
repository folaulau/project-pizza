package com.lovemesomecoding.pizzaria.paymentgateway.charge;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.paymentgateway.payment.Payment;
import com.lovemesomecoding.pizzaria.utils.MathUtils;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Service
public class ChargeServiceImp implements ChargeService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${stripe.secret.key}")
	private String STRIPE_API_KEY;
	
	@Override
	public Charge chargeOrderPayment(String customerPaymentGateyId, Payment payment, Map<String,Object> metadata) {
		log.debug("chargeOrderPayment(..)");
		Stripe.apiKey = STRIPE_API_KEY;

		int amountInCents = MathUtils.getCentsForDollars(payment.getAmountPaid());
		
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", amountInCents);
		chargeParams.put("currency", "usd");
		chargeParams.put("description", payment.getDescription());
		chargeParams.put("source", payment.getPaymentMethod().getPaymentGatewayId());
		chargeParams.put("customer", customerPaymentGateyId);
		
		if(metadata!=null) {
			chargeParams.put("metadata", metadata);
		}
		
		log.debug("chargeParams={}", ObjMapperUtils.toJson(chargeParams));
		
		Charge charge = null;
		try {
			charge = Charge.create(chargeParams);
			log.debug("successful charge={}", charge.toJson());
		} catch (StripeException e) {
			log.debug("StripeException, msg={}",e.getStripeError().getMessage());
			e.printStackTrace();
		}
		
		return charge;
	}

	@Override
	public Charge chargeOrderPaymentWithToken(Payment payment, Map<String, Object> metadata) {
		log.debug("chargeOrderPaymentWithToken(..)");
		Stripe.apiKey = STRIPE_API_KEY;

		int amountInCents = MathUtils.getCentsForDollars(payment.getAmountPaid());
		
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", amountInCents);
		chargeParams.put("currency", "usd");
		chargeParams.put("description", payment.getDescription());
		chargeParams.put("source", payment.getPaymentMethod().getSourceToken());
		
		if(metadata!=null) {
			chargeParams.put("metadata", metadata);
		}
		
		log.debug("chargeParams={}", ObjMapperUtils.toJson(chargeParams));
		
		Charge charge = null;
		try {
			charge = Charge.create(chargeParams);
			
			log.debug("successful charge={}", charge.toJson());
		} catch (StripeException e) {
			log.debug("StripeException, msg={}",e.getStripeError().getMessage());
			e.printStackTrace();
		}
		
		return charge;
	}

	
}
