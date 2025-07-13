package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.exception.CustomException;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.*;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity-- Save the data with status order created
        //Product service--Block the products (reduceQuantity)
        //Payment Service--Payments-success then Completed else canceled.
        log.info("Placing order request {} ",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("creating order with status created");
        Order order= Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .amount(orderRequest.getTotalAmount())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();
        order =orderRepository.save(order);
        log.info("Calling payment Service to complete the payment");
        PaymentRequest paymentRequest=PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus=null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing the Order status to placed");
            orderStatus="PLACED";
        } catch (Exception e) {
            log.info("Error occurred  in payment. Changing the Order status to failed");
            orderStatus="PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order placed successfully with order Id {} ",order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for ");
        Order order=orderRepository.findById(orderId).orElseThrow(()->new CustomException("Order not available for the given id","NOT_FOUND", 404));
        log.info("Invoking product service to fetch the product info {} ",order.getProductId());
        ProductResponse productResponse=restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(), ProductResponse.class);
        OrderResponse.ProductDetails productDetails=OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();

        log.info("Invoking payment service to fetch the data of payment info  of gioven order Id {}",orderId);
        PaymentResponse paymentResponse=restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);
        OrderResponse.PaymentDetails paymentDetails=OrderResponse.PaymentDetails.builder()
                .paymentDate(paymentResponse.getPaymentDate())
                .status(paymentResponse.getStatus())
                .paymentId(paymentResponse.getPaymentId())
                .amount(paymentResponse.getAmount())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
