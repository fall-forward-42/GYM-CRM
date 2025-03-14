package com.lehaitien.gym.domain.repository;

import com.lehaitien.gym.domain.model.Payment.Payment;
import com.lehaitien.gym.domain.constant.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByUser_UserId(String userId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByTransactionId(String transactionId);

}

