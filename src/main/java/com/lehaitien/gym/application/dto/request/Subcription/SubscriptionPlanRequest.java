package com.lehaitien.gym.application.dto.request.Subcription;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SubscriptionPlanRequest(
        String planName,
        Integer duration,
        Integer price,
        String description
) {}