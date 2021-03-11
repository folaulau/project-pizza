package com.lovemesomecoding.pizzaria.paymentgateway.customer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.entity.user.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImp implements CustomerService {

    @Value("${stripe.secret.key}")
    private String STRIPE_API_KEY;

    @Override
    public Customer create(User user, Map<String, Object> metadata) {
        log.info("create(..)");
        Stripe.apiKey = STRIPE_API_KEY;

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Pizzaria Customer");
        customerParams.put("email", user.getEmail());
        customerParams.put("name", user.getName());

        if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0) {
            customerParams.put("phone", user.getPhoneNumber());
        }

        if (user.getAddress() != null) {
            Map<String, Object> address = user.getAddress().getPaymentGatewayAddress();

            if (address != null) {
                customerParams.put("address", address);
            }
        }

        com.stripe.model.Customer customer = null;

        try {
            customerParams.put("metadata", metadata);
            customer = com.stripe.model.Customer.create(customerParams);
        } catch (StripeException e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public Customer update(User user, Map<String, Object> metadata) {
        log.info("update(..)");
        Stripe.apiKey = STRIPE_API_KEY;

        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Monomono Customer");
        customerParams.put("email", user.getEmail());
        customerParams.put("name", user.getName());

        if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0) {
            customerParams.put("phone", user.getPhoneNumber());
        }

        Map<String, Object> address = user.getAddress().getPaymentGatewayAddress();

        if (address != null) {
            customerParams.put("address", address);
        }

        com.stripe.model.Customer customer = null;

        try {
            customerParams.put("metadata", metadata);
            customer = com.stripe.model.Customer.retrieve(user.getPaymentGatewayId());
            customer = customer.update(customerParams);
        } catch (StripeException e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public Customer getByCustomerId(String customerId) {
        log.info("getByCustomerId({})", customerId);
        Stripe.apiKey = STRIPE_API_KEY;

        com.stripe.model.Customer customer = null;

        try {
            customer = com.stripe.model.Customer.retrieve(customerId);
        } catch (StripeException e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public boolean deleteByCustomerId(String customerId) {
        // TODO Auto-generated method stub

        try {
            com.stripe.model.Customer customer = com.stripe.model.Customer.retrieve(customerId);

            customer = customer.delete();

            return customer.getDeleted();

        } catch (StripeException e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }

}
