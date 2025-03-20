package com.lehaitien.gym.application.service;

import com.lehaitien.gym.application.dto.request.Payment.PaymentRequest;
import com.lehaitien.gym.application.dto.response.Payment.PaymentResponse;
import com.lehaitien.gym.domain.constant.PaymentStatus;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.PaymentMapper;
import com.lehaitien.gym.domain.model.Payment.Payment;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.PaymentRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Processing payment for user: {}", request.userId());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Payment payment = Payment.builder()
                .user(user)
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .transactionId(UUID.randomUUID().toString()) // Generate unique transaction ID
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // Simulate Payment Processing (Assuming success) - need to split into other service
        payment.setStatus(PaymentStatus.PAID);
        user.setBalance(user.getBalance() + payment.getAmount()); // Cộng tiền vào balance
        userRepository.save(user);
        paymentRepository.save(payment);

        log.info("Payment successful for user: {}", request.userId());

        return paymentMapper.toPaymentResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUser(String userId) {
        return paymentRepository.findByUser_UserId(userId).stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentResponse(payment);
    }
}
