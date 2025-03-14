package com.lehaitien.gym.application.dto.response.Subscription;

import com.lehaitien.gym.domain.constant.SubscriptionStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserSubscriptionResponse(
        String id,
        String userId,
        String planId,
        String planName,
        LocalDate startDate,
        LocalDate endDate,
        SubscriptionStatus status,

        int leftDays
) {}