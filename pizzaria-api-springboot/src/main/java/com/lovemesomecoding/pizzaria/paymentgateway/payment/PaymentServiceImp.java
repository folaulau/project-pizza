package com.lovemesomecoding.pizzaria.paymentgateway.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.entity.order.Order;
import com.lovemesomecoding.pizzaria.paymentgateway.charge.ChargeService;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethod;
import com.lovemesomecoding.pizzaria.utils.MathUtils;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;
import com.stripe.model.Charge;

@Service
public class PaymentServiceImp implements PaymentService {

    private Logger             log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityDTOMapper    entityMapper;

    @Autowired
    private PaymentRespository paymentRespository;

    @Autowired
    private ChargeService      chargeService;

    private Payment create(Payment payment) {
        return paymentRespository.saveAndFlush(payment);
    }

    @Override
    public Payment payOrder(Order order, PaymentMethod paymentMethod) {
        log.debug("payOrder(..)");

        Payment payment = new Payment();
        payment.setType(PaymentType.PIZZARIA_ORDER);
        payment.setDescription("Order Payment");
        payment.setPaymentMethod(this.entityMapper.mapPaymentMethodToOrderPaymentMethod(paymentMethod));
        payment.setOrderId(order.getId());
        payment.setAmountPaid(order.getTotal());

        Charge charge = chargeService.chargeOrderPaymentWithToken(payment, PaymentUtils.generateOrderPaymentMetadata(payment));

        payment.setPaid(charge.getPaid());
        payment.setAmountPaid(MathUtils.getDollarsFromCents(charge.getAmount()));
        payment.setStripeChargeId(charge.getId());

        payment = create(payment);

        log.debug("payment={}", ObjMapperUtils.toJson(payment));

        return payment;
    }

    @Override
    public Payment payOrder(String customerPaymentGatewayId, Order order, PaymentMethod paymentMethod) {
        log.debug("payOrder(..)");

        Payment payment = new Payment();
        payment.setType(PaymentType.PIZZARIA_ORDER);
        payment.setDescription("Order Payment");
        payment.setPaymentMethod(this.entityMapper.mapPaymentMethodToOrderPaymentMethod(paymentMethod));
        payment.setOrderId(order.getId());
        payment.setAmountPaid(order.getTotal());

        Charge charge = chargeService.chargeOrderPayment(customerPaymentGatewayId, payment, PaymentUtils.generateOrderPaymentMetadata(payment));

        payment.setPaid(charge.getPaid());
        payment.setAmountPaid(MathUtils.getDollarsFromCents(charge.getAmount()));
        payment.setStripeChargeId(charge.getId());

        payment = create(payment);

        log.debug("payment={}", ObjMapperUtils.toJson(payment));

        return payment;
    }

}
