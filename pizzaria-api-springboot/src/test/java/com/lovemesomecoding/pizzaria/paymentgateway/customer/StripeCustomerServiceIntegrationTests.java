package com.lovemesomecoding.pizzaria.paymentgateway.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("local")
@SpringBootTest
public class StripeCustomerServiceIntegrationTests {

    @Autowired
    private CustomerService   customerService;

    com.stripe.model.Customer customer = null;

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void cleanup() {
        if (customer != null) {
            boolean customerDeleted = customerService.deleteByCustomerId(customer.getId());
            log.info("email={}, customerDeleted={}", customer.getEmail(), customerDeleted);
        }

    }

    @Test
    public void test_create_customer_successfully() {
        User user = new User();
        user.setUuid("user-" + UUID.randomUUID().toString());
        user.setFirstName("Folau");
        user.setLastName("Kaveinga");
        user.setEmail("folaudev+integration-test@gmail.com");
        user.setPhoneNumber("310123457");

        Address address = new Address();
        address.setStreet("1234 Lenox st");
        address.setCity("Inglewood");
        address.setCountry("United States");
        address.setState("CA");
        address.setZipcode("90432");
        user.setAddress(address);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userUuid", user.getUuid());
        metadata.put("environment", "local");

        customer = customerService.create(user, metadata);

        log.info("customer={}", customer.toJson());

        assertThat(customer).isNotNull();
        assertThat(customer.getEmail()).isNotNull().isEqualTo(user.getEmail());
        assertThat(customer.getName()).isNotNull().isEqualTo(user.getName());
        assertThat(customer.getPhone()).isNotNull().isEqualTo(user.getPhoneNumber());

        Map<String, String> stripeMetadata = customer.getMetadata();

        assertThat(stripeMetadata).isNotNull();

        assertThat(stripeMetadata.get("userUuid")).isNotNull().isEqualTo(user.getUuid());
        assertThat(stripeMetadata.get("environment")).isNotNull().isEqualTo("local");

        customer = customerService.getByCustomerId(customer.getId());

        assertThat(customer).isNotNull();

    }
}
