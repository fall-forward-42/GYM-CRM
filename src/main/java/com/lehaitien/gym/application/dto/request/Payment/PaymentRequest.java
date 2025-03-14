package com.lehaitien.gym.application.dto.request.Payment;

import com.lehaitien.gym.domain.constant.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PaymentRequest(
        @NotNull String userId,
        @NotNull PaymentMethod paymentMethod,
        @Min(1) Integer amount
) {}

