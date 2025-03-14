package com.lehaitien.gym.application.dto.response.Subscription;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SubscriptionPlanResponse(
        String planId,
        String planName,
        Integer duration,
        Integer price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}