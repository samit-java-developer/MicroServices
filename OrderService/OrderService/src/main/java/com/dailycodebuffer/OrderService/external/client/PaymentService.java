package com.dailycodebuffer.OrderService.external.client;

import com.dailycodebuffer.OrderService.exception.CustomException;
import com.dailycodebuffer.OrderService.model.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CircuitBreaker(name = "external",fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception e){
        throw new CustomException("Payment Service is not available","UNAVAILABLE",500);
    }
}
