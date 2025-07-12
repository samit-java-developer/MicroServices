package com.dailycodebuffer.PaymentService.service;

import com.dailycodebuffer.PaymentService.entity.TransactionsDetails;
import com.dailycodebuffer.PaymentService.model.PaymentRequest;
import com.dailycodebuffer.PaymentService.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details {}",paymentRequest);
        TransactionsDetails transactionsDetails= TransactionsDetails
                .builder()
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .paymentDate(Instant.now())
                .amount(paymentRequest.getAmount())
                .build();
        paymentRepository.save(transactionsDetails);
        log.info("Transactions completed with id {}",transactionsDetails.getId());
        return transactionsDetails.getId();
    }
}
