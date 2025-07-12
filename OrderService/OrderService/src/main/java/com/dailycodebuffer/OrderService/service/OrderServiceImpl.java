package com.dailycodebuffer.OrderService.service;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PaymentService paymentService;

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
}
