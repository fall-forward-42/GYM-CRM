package com.lehaitien.gym.application.dto.request.Subcription;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Request object for creating or updating a subscription plan")
public record SubscriptionPlanRequest(
        @NotBlank
        @Schema(description = "Name of the subscription plan", example = "Premium Plus")
        String planName,

        @NotNull
        @Min(1)
        @Schema(description = "Duration of the plan in days", example = "30")
        Integer duration,

        @NotNull
        @Min(0)
        @Schema(description = "Price of the plan in VND", example = "500000")
        Integer price,

        @Schema(description = "Description of the plan", example = "Access to all facilities, free PT sessions")
        String description
) {}
