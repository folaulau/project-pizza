package com.lovemesomecoding.pizzaria.entity.order.lineitem;

public interface LineItemService {

	LineItem getLineItemByUid(String uid);
	
	LineItem getLineItemByUidAndOrderUid(String uid, String orderUid);
	
	LineItem getLineItemByUidAndOrderId(String uid, Long orderId);
	
	LineItem getLineItemByOrderUidAndProductUid(String orderUid, String productUid);
}
