package com.lovemesomecoding.pizzaria.paymentgateway.card;

import java.util.Map;

import com.stripe.model.Card;

public interface CardService {

	Card getCardById(String id);
	
	Card getCardBySourceToken(String sourceToken);
	
	Card add(String customerId, String sourceToken, Map<String,Object> metadata);
	
	boolean remove(String customerId, String cardId);
	
	
}
