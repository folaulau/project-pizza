package com.lovemesomecoding.pizzaria.entity.order;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lovemesomecoding.pizzaria.dto.CardPMCreateDTO;
import com.lovemesomecoding.pizzaria.dto.CustomPage;
import com.lovemesomecoding.pizzaria.dto.EntityDTOMapper;
import com.lovemesomecoding.pizzaria.dto.LineItemDTO;
import com.lovemesomecoding.pizzaria.dto.OrderPayDTO;
import com.lovemesomecoding.pizzaria.dto.OrderReadDTO;
import com.lovemesomecoding.pizzaria.entity.address.Address;
import com.lovemesomecoding.pizzaria.entity.order.lineitem.LineItem;
import com.lovemesomecoding.pizzaria.entity.order.lineitem.LineItemService;
import com.lovemesomecoding.pizzaria.entity.product.Product;
import com.lovemesomecoding.pizzaria.entity.product.ProductService;
import com.lovemesomecoding.pizzaria.entity.user.UserService;
import com.lovemesomecoding.pizzaria.exception.ApiException;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethod;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethodService;
import com.lovemesomecoding.pizzaria.paymentmethod.PaymentMethodType;
import com.lovemesomecoding.pizzaria.utils.ObjMapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "orders", tags = "Orders")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private Logger               log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService         oderService;

    @Autowired
    private UserService          userService;

    @Autowired
    private LineItemService      lineItemService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private ProductService       productService;

    @Autowired
    private EntityDTOMapper      entityMapper;

    @ApiOperation(value = "Get Order By Uuid")
    @GetMapping("/{uid}")
    public ResponseEntity<OrderReadDTO> getOrderByUid(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "uid", required = true, value = "uid") @PathVariable("uid") String uid) {
        log.debug("getOrderByUid({})", uid);

        Order order = this.oderService.getByUid(uid);

        if (order == null) {
            throw new ApiException("Order not found", "Order not found for uid=" + uid);
        }

        if (order.getCustomer() != null && (token == null || token.length() == 0)) {
            throw new ApiException("Order not found", "You can't access this order");
        }

        if (order.getCustomer() != null && (token == null || token.length() == 0)) {
            throw new ApiException("Order not found", "You can't access this order");
        }

        OrderReadDTO orderReadDTO = this.entityMapper.mapOrderToOrderReadDTO(order);

        return new ResponseEntity<>(orderReadDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Order LineItem By OrderUid and ProductUid")
    @GetMapping("/lineitems")
    public ResponseEntity<LineItemDTO> getOrderLineItemByOrderUidAndProductUid(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "productUid", required = true, value = "product uid") @RequestParam("productUid") String productUid,
            @ApiParam(name = "orderUid", required = true, value = "order uid") @RequestParam("orderUid") String orderUid) {
        log.debug("getOrderLineItemByOrderUidAndProductUid(...)");

        LineItem lineItem = this.lineItemService.getLineItemByOrderUidAndProductUid(orderUid, productUid);

        if (lineItem == null) {
            throw new ApiException("Lineitem not found", "Lineitem not found by order uid=" + orderUid + ", productUid=" + productUid);

        }

        LineItemDTO lineItemDTO = this.entityMapper.mapLineItemToLineItemDTO(lineItem);

        return new ResponseEntity<>(lineItemDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Order Per Member")
    @GetMapping
    public ResponseEntity<CustomPage<OrderReadDTO>> getOdersByCustomer(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "customerUid", required = true, value = "customer uid") @RequestParam("customerUid") String customerUid, Pageable pageable,
            @ApiParam(name = "page", required = false, value = "page", defaultValue = "0") @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @ApiParam(name = "size", required = false, value = "size", defaultValue = "10") @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
            @ApiParam(name = "sort", required = false, value = "Sorting. format -> sort=attribute:direction&... direction values[asc,desc] ") @RequestParam(required = false, name = "sort") String[] sorts) {
        log.debug("getOdersByCustomer({})", customerUid);

        Page<Order> orderPage = this.oderService.getPage(customerUid, pageable);

        CustomPage<OrderReadDTO> result = new CustomPage<OrderReadDTO>(orderPage);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Pay Order")
    @PostMapping("/pay")
    public ResponseEntity<OrderReadDTO> payOrder(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "OrderPayment", required = true, value = "order payment") @Valid @RequestBody OrderPayDTO orderPayDTO) {
        log.debug("payOrderPublic(...)");

        log.debug("orderPayDTO={}", ObjMapperUtils.toJson(orderPayDTO));

        Order order = this.oderService.getByUid(orderPayDTO.getOrder().getUid());

        if (order.isPaid()) {
            throw new ApiException("You have paid for this order already", "order field paid is true");
        }

        if (order.getCustomer() != null && (token == null || token.length() == 0)) {
            throw new ApiException("Order not found", "You can't access this order");
        }

        if (orderPayDTO.isUseProfileAddress() && order.getCustomer() == null) {
            throw new ApiException("You don't have address on file", "You can't use address on file but you haven't signed up. Please sign up");
        }

        if (orderPayDTO.isSavePaymentMethodForFutureUse() && order.getCustomer() == null) {
            throw new ApiException("You don't have an account on file", "Please sign up");
        }

        CardPMCreateDTO cardPMCreateDTO = orderPayDTO.getPaymentMethod();

        PaymentMethod paymentMethod = null;

        if (cardPMCreateDTO.getUid() != null && cardPMCreateDTO.getUid().length() > 0) {

            paymentMethod = paymentMethodService.getByUid(cardPMCreateDTO.getUid());

        } else {
            paymentMethod = this.entityMapper.mapCardPMCreateDTOToPaymentMethod(orderPayDTO.getPaymentMethod());

            if (orderPayDTO.isSavePaymentMethodForFutureUse()) {
                paymentMethod.setUser(order.getCustomer());
                paymentMethod.setType(PaymentMethodType.CARD);
                paymentMethod = this.paymentMethodService.create(paymentMethod);
            }
        }

        Address address = null;

        if (orderPayDTO.isUseProfileAddress()) {
            address = order.getCustomer().getAddress();
        } else {
            address = entityMapper.mapAddressDTOToAddress(orderPayDTO.getLocation());
        }

        order.setLocation(address);

        order = oderService.payOrder(orderPayDTO.isUseCardOnFile(), order, paymentMethod);

        log.debug("paid order={}", ObjMapperUtils.toJson(order));

        return new ResponseEntity<>(entityMapper.mapOrderToOrderReadDTO(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Add Order Line Item to Shopping Cart", notes = "if incrementing is true, lineItem count will increment by one. Else lineItem count will set new count")
    @PutMapping("/lineitems")
    public ResponseEntity<OrderReadDTO> addOrderLineItem(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "incrementing", required = false, value = "incrementing") @RequestParam(required = false, name = "incrementing") boolean incrementing,
            @ApiParam(name = "orderUid", required = false, value = "order uid") @RequestParam(required = false, name = "orderUid") String orderUuid,
            @ApiParam(name = "lineItem", required = true, value = "line item") @Valid @RequestBody LineItemDTO lineItemDTO) {
        log.debug("addOrderLineItem(...)");
        log.debug("orderUuid={}", orderUuid);
        log.debug("lineItemDTO={}", ObjMapperUtils.toJson(lineItemDTO));

        if (lineItemDTO.getProduct() == null) {
            throw new ApiException("Product could not be added to cart", "product is null");
        }

        if (lineItemDTO.getProduct().getUuid() == null) {
            throw new ApiException("Product could not be added to cart", "product uuid is null");
        }

        if (lineItemDTO.getCount() < 0) {
            throw new ApiException("Product could not be added to cart", "count is less than 0. count=" + lineItemDTO.getCount());
        }

        Product product = productService.getByUid(lineItemDTO.getProduct().getUuid());

        if (product == null) {
            throw new ApiException("Product could not be added to cart", "Product not found for uid. uid=" + lineItemDTO.getProduct().getUuid());
        }

        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setCount(lineItemDTO.getCount());

        Order order = null;

        if (orderUuid != null && orderUuid.length() > 0) {
            order = oderService.getByUid(orderUuid);
            if (order == null) {
                throw new ApiException("Product could not be added to cart", "Order uid not found. uid=" + orderUuid);
            }

            if (order.isPaid()) {
                throw new ApiException("You have paid for this order already", "order field paid is true");
            }

            if (order.getCustomer() != null && (token == null || token.length() == 0)) {
                throw new ApiException("Order cannot be modified", "You can't access this order");
            }

            order = this.oderService.addLineItem(order, lineItem, incrementing);

        } else {

            order = this.oderService.addLineItem(orderUuid, lineItem, incrementing);

        }

        OrderReadDTO orderReadDTO = this.entityMapper.mapOrderToOrderReadDTO(order);

        log.debug("orderReadDTO={}", ObjMapperUtils.toJson(orderReadDTO));

        return new ResponseEntity<>(orderReadDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Remove Order Line Item to Shopping Cart")
    @DeleteMapping("/lineitems")
    public ResponseEntity<OrderReadDTO> deleteOrderLineItem(@RequestHeader(name = "token", required = false) String token,
            @ApiParam(name = "lineItem", required = true, value = "line item") @Valid @RequestBody LineItemDTO lineItemDTO,
            @ApiParam(name = "orderUid", required = true, value = "order uid") @RequestParam(required = false, name = "orderUid") String orderUuid) {
        log.debug("deleteOrderLineItem(...)");
        log.debug("orderUuid={}", orderUuid);
        log.debug("lineItemDTO={}", ObjMapperUtils.toJson(lineItemDTO));

        Order order = null;

        if (orderUuid != null && orderUuid.length() > 0) {
            order = oderService.getByUid(orderUuid);
            if (order == null) {
                throw new ApiException("Lineitem could not be removed from cart", "Order uid not found. uid=" + orderUuid);
            }
        } else {
            throw new ApiException("Lineitem could not be removed from cart", "Order uid not found. uid=" + orderUuid);
        }

        if (order.isPaid()) {
            throw new ApiException("You have paid for this order already", "order field paid is true");
        }

        if (order.getCustomer() != null && (token == null || token.length() == 0)) {
            throw new ApiException("Order cannot be modified", "You can't access this order");
        }

        LineItem lineItem = null;

        for (LineItem li : order.getLineItems()) {
            if (li.getUuid().equals(lineItemDTO.getUid())) {
                lineItem = li;
                break;
            }
        }

        if (lineItem == null) {
            throw new ApiException("Lineitem could not be removed from cart", "lineItem not found in order. uid=" + lineItemDTO.getUid());
        }

        order = this.oderService.removeLineItem(order, lineItem);

        OrderReadDTO orderReadDTO = this.entityMapper.mapOrderToOrderReadDTO(order);

        log.debug("orderReadDTO={}", ObjMapperUtils.toJson(orderReadDTO));

        return new ResponseEntity<>(orderReadDTO, HttpStatus.OK);
    }
}
