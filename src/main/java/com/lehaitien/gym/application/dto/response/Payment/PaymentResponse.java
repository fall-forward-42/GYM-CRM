package com.lehaitien.gym.application.dto.response.Payment;

import com.lehaitien.gym.domain.constant.PaymentMethod;
import com.lehaitien.gym.domain.constant.PaymentStatus;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        String paymentId,
        String userId,
        PaymentMethod paymentMethod,
        Integer amount,
        PaymentStatus status,
        String transactionId,
        LocalDateTime paymentDate
) {}