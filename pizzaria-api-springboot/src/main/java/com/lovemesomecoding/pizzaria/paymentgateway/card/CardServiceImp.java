package com.lovemesomecoding.pizzaria.paymentgateway.card;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Token;

@Service
public class CardServiceImp implements CardService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${stripe.secret.key}")
	private String STRIPE_API_KEY;
	
	@Override
	public Card getCardById(String id) {
		Stripe.apiKey = STRIPE_API_KEY;
		
		return null;
	}

	@Override
	public Card getCardBySourceToken(String sourceToken) {
		
		Stripe.apiKey = STRIPE_API_KEY;
		Card card = null;
		try {
			Token token = Token.retrieve(sourceToken);
			
			card = token.getCard();
			
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return card;
	}


	@Override
	public Card add(String customerId, String sourceToken, Map<String, Object> metadata) {
		log.info("addCard(..)");
		Stripe.apiKey = STRIPE_API_KEY;
		log.debug("customer id: {}",customerId);
		log.debug("payment token: {}",sourceToken);
		com.stripe.model.Customer stripeCustomer = null;
		com.stripe.model.Card card = null;
		try {
			stripeCustomer = com.stripe.model.Customer.retrieve(customerId);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("source", sourceToken);
			params.put("metadata", metadata);
			
			card = (com.stripe.model.Card)stripeCustomer.getSources().create(params);
			//log.debug(card.toJson());
		} catch (StripeException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return card;
	}

	@Override
	public boolean remove(String customerId, String cardId) {
		log.info("removeCard(..)");
		//log.info(card.toJson());
		Stripe.apiKey = STRIPE_API_KEY;
		com.stripe.model.Customer stripeCustomer = null;
		try {
			stripeCustomer = com.stripe.model.Customer.retrieve(customerId);
			Card stripeCard = (Card)stripeCustomer.getSources().retrieve(cardId);
			stripeCard.delete();
			return true;
		} catch (StripeException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return false;
	}

}
