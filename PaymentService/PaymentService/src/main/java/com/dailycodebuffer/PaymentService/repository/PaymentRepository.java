package com.dailycodebuffer.PaymentService.repository;

import com.dailycodebuffer.PaymentService.entity.TransactionsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<TransactionsDetails,Long> {

    TransactionsDetails findByOrderId(long orderId);
}
