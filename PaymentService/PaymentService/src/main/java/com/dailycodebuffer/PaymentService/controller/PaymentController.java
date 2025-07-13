package com.dailycodebuffer.PaymentService.controller;

import com.dailycodebuffer.PaymentService.model.PaymentRequest;
import com.dailycodebuffer.PaymentService.model.PaymentResponse;
import com.dailycodebuffer.PaymentService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest){
        long paymentId=paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>(paymentId, HttpStatus.OK);

    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId){
        PaymentResponse paymentResponse=paymentService.getPaymentDetailsByOrderId(orderId);
        return new ResponseEntity<>(paymentResponse,HttpStatus.OK);
    }
}
