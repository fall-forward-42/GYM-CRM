package com.lehaitien.gym.application.dto.request.Subcription;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSubscriptionRequest {
    @NotNull
    private String userId;
    @NotNull
    private String planId;
}