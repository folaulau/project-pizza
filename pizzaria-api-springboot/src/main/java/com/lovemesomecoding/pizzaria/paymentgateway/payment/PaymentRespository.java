package com.lovemesomecoding.pizzaria.paymentgateway.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRespository extends JpaRepository<Payment, Long> {

}
