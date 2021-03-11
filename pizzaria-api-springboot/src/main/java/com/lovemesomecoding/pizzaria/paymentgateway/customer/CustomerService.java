package com.lovemesomecoding.pizzaria.paymentgateway.customer;

import java.util.Map;

import com.lovemesomecoding.pizzaria.entity.user.User;

public interface CustomerService {

    com.stripe.model.Customer create(User user, Map<String, Object> metadata);

    com.stripe.model.Customer update(User user, Map<String, Object> metadata);

    com.stripe.model.Customer getByCustomerId(String customerId);

    boolean deleteByCustomerId(String customerId);
}
