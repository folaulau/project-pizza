package com.lovemesomecoding.pizzaria.entity.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lovemesomecoding.pizzaria.dto.OrderAdminSearchResponseItemDTO;
import com.lovemesomecoding.pizzaria.entity.order.lineitem.LineItem;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethod;
import com.lovemesomecoding.pizzaria.utils.Sorting;

public interface OrderService {

    Order create(Order order);

    Order update(Order order);

    Order getByUid(String uid);

    // String getLatestOrderUid(Long customerId);
    //
    // Order getLatestOrder(Long customerId);

    Order getCurrentByCustomerId(Long customerId);

    Order getById(Long id);

    Order addLineItem(Order order, LineItem lineItem, boolean incrementing);

    Order removeLineItem(Order order, LineItem lineItem);

    Order addLineItem(String orderUid, LineItem lineItem, boolean incrementing);

    Order payOrder(boolean useCardOnFile, Order order, PaymentMethod paymentMethod);

    Page<Order> getPage(Pageable pageable);

    Page<Order> getPage(String customerUid, Pageable pageable);

    Page<OrderAdminSearchResponseItemDTO> search(Pageable pageable, List<Integer> amounts, String query, List<Sorting> sortings);

}
