package com.lovemesomecoding.pizzaria.paymentmethod;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovemesomecoding.pizzaria.dto.CardPMCreateDTO;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.PaymentMethodDeleteDTO;
import com.lovemesomecoding.pizzaria.dto.PaymentMethodReadDTO;
import com.lovemesomecoding.pizzaria.dto.UserDTO;
import com.lovemesomecoding.pizzaria.entity.user.User;
import com.lovemesomecoding.pizzaria.entity.user.UserService;
import com.lovemesomecoding.pizzaria.utils.ApiSessionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author fkaveinga
 *
 */
@Api(value = "paymentmethods", tags = "PaymentMethods")
@RestController
@RequestMapping("/paymentmethods")
public class PaymentMethodController {
    private Logger               log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService          userService;

    @Autowired
    private EntityDTOMapper      entityMapper;

    /**
     * Add Card PaymentMethod
     * 
     * @param Customer
     * @return Boolean - true: proceed to buy, false: show error message
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @ApiOperation(value = "Add Card PaymentMethod")
    @PostMapping({"/card"})
    public ResponseEntity<List<PaymentMethodReadDTO>> addCardPaymentMethod(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "customerUid", required = true, value = "customer uid") @RequestParam("customerUid") String customerUid,
            @ApiParam(name = "Card", required = true, value = "card") @Valid @RequestBody CardPMCreateDTO cardPMCreateDTO) {
        log.debug("addCardPaymentMethod(...)");

        PaymentMethod paymentMethod = entityMapper.mapCardPMCreateDTOToPaymentMethod(cardPMCreateDTO);

        UserDTO user = this.userService.getByUuid(customerUid);

        paymentMethod.setType(PaymentMethodType.CARD);

        List<PaymentMethod> paymentMethods = paymentMethodService.add(user.getId(), user.getPaymentGatewayId(), paymentMethod);

        return new ResponseEntity<>(entityMapper.mapPaymentMethodsToPaymentMethodReadDTOs(paymentMethods), HttpStatus.OK);
    }

    @ApiOperation(value = "Get PaymentMethods By User")
    @GetMapping
    public ResponseEntity<List<PaymentMethodReadDTO>> getPaymentMethods(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "customerUid", required = true, value = "customer uid") @RequestParam("customerUid") String customerUid) {
        log.debug("getPaymentMethods(...)");

        List<PaymentMethod> paymentMethods = paymentMethodService.getByCustomerUid(customerUid);

        return new ResponseEntity<>(entityMapper.mapPaymentMethodsToPaymentMethodReadDTOs(paymentMethods), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Card PaymentMethod")
    @PutMapping({"/card"})
    public ResponseEntity<List<PaymentMethodReadDTO>> updateCardPaymentMethod(@ApiParam(name = "Card", required = true, value = "card") @Valid @RequestBody CardPMCreateDTO cardPMCreateDTO,
            @ApiParam(name = "currentPMUid", required = true, value = "current payment method uid") @RequestParam("currentPMUid") String currentPMUid) {
        log.debug("updateCardPaymentMethod(...)");

        PaymentMethod newPaymentMethod = entityMapper.mapCardPMCreateDTOToPaymentMethod(cardPMCreateDTO);

        PaymentMethod currentPaymentMethod = paymentMethodService.getByUid(currentPMUid);

        List<PaymentMethod> paymentMethods = paymentMethodService.update(ApiSessionUtils.getUserId(), currentPaymentMethod, newPaymentMethod);

        return new ResponseEntity<>(entityMapper.mapPaymentMethodsToPaymentMethodReadDTOs(paymentMethods), HttpStatus.OK);

    }

    @ApiOperation(value = "Delete PaymentMethod")
    @DeleteMapping
    public ResponseEntity<List<PaymentMethodReadDTO>> deletePaymentMethod(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "customerUid", required = true, value = "customer uid") @RequestParam("customerUid") String customerUid,
            @ApiParam(name = "paymentMethod", required = true, value = "Payment Method") @Valid @RequestBody PaymentMethodDeleteDTO paymentMethodDeleteDTO) {
        log.debug("deletePaymentMethod(...)");

        PaymentMethod paymentMethod = paymentMethodService.getByUid(paymentMethodDeleteDTO.getUuid());

        List<PaymentMethod> paymentMethods = paymentMethodService.remove(ApiSessionUtils.getUserId(), paymentMethod);

        return new ResponseEntity<>(entityMapper.mapPaymentMethodsToPaymentMethodReadDTOs(paymentMethods), HttpStatus.OK);
    }
}
