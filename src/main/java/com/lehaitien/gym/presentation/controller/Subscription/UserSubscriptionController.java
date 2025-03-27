package com.lehaitien.gym.presentation.controller.Subscription;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Subcription.UserSubscriptionRequest;
import com.lehaitien.gym.application.dto.response.Subscription.UserSubscriptionResponse;
import com.lehaitien.gym.application.service.UserSubscriptionService;
import com.lehaitien.gym.domain.constant.SortDirection;
import com.lehaitien.gym.domain.constant.SubscriptionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Tag(name = "User Subscriptions", description = "Manage user-subscription assignments, cancellations, and queries")
public class UserSubscriptionController {

    private final UserSubscriptionService subscriptionService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign a subscription plan to a user")
    public ApiResponse<UserSubscriptionResponse> createSubscription(@Valid @RequestBody UserSubscriptionRequest request) {
        return ApiResponse.<UserSubscriptionResponse>builder()
                .result(subscriptionService.createSubscription(request))
                .build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get all subscriptions filtered by status and sorted by start date")
    public ApiResponse<List<UserSubscriptionResponse>> getSubscriptionsByStatus(
            @PathVariable SubscriptionStatus status,
            @RequestParam(required = false, defaultValue = "ASC") SortDirection sortDirection) {

        return ApiResponse.<List<UserSubscriptionResponse>>builder()
                .result(subscriptionService.getSubscriptionsByStatus(status, sortDirection))
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all subscriptions of a specific user")
    public ApiResponse<List<UserSubscriptionResponse>> getUserSubscriptions(@PathVariable String userId) {
        return ApiResponse.<List<UserSubscriptionResponse>>builder()
                .result(subscriptionService.getSubscriptionsByUser(userId))
                .build();
    }

    @PutMapping("/{subscriptionId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancel an active subscription")
    public ApiResponse<String> cancelSubscription(@PathVariable String subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ApiResponse.<String>builder()
                .result("Subscription has been canceled")
                .build();
    }
}
