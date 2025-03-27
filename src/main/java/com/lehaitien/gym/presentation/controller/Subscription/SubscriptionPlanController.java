package com.lehaitien.gym.presentation.controller.Subscription;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Subcription.SubscriptionPlanRequest;
import com.lehaitien.gym.application.dto.response.Subscription.SubscriptionPlanResponse;
import com.lehaitien.gym.application.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
@Tag(name = "Subscription Plans", description = "Manage gym subscription plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    /**
     * Tạo gói tập mới
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new subscription plan",
            description = "Only ADMINs can create new gym subscription plans."
    )
    public ApiResponse<SubscriptionPlanResponse> createPlan(
            @Valid @RequestBody SubscriptionPlanRequest request) {
        return ApiResponse.<SubscriptionPlanResponse>builder()
                .result(planService.createPlan(request))
                .build();
    }

    /**
     * Lấy danh sách tất cả gói tập
     */
    @GetMapping
    @Operation(
            summary = "Get all subscription plans",
            description = "Retrieve all available gym subscription plans"
    )
    public ApiResponse<List<SubscriptionPlanResponse>> getAllPlans() {
        return ApiResponse.<List<SubscriptionPlanResponse>>builder()
                .result(planService.getAllPlans())
                .build();
    }

    /**
     * Lấy gói tập theo ID
     */
    @GetMapping("/{planId}")
    @Operation(
            summary = "Get a subscription plan by ID",
            description = "Provide a subscription plan ID to retrieve its details"
    )
    public ApiResponse<SubscriptionPlanResponse> getPlanById(@PathVariable String planId) {
        return ApiResponse.<SubscriptionPlanResponse>builder()
                .result(planService.getPlanById(planId))
                .build();
    }

    /**
     * Lấy gói tập theo tên
     */
    @GetMapping("/name/{planName}")
    @Operation(
            summary = "Get a subscription plan by name",
            description = "Search and retrieve a plan by its name"
    )
    public ApiResponse<SubscriptionPlanResponse> getPlanByName(@PathVariable String planName) {
        return ApiResponse.<SubscriptionPlanResponse>builder()
                .result(planService.getPlanByName(planName))
                .build();
    }

    /**
     * Cập nhật thông tin gói tập
     */
    @PutMapping("/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update a subscription plan",
            description = "Only ADMINs can update existing subscription plans"
    )
    public ApiResponse<SubscriptionPlanResponse> updatePlan(
            @PathVariable String planId, @Valid @RequestBody SubscriptionPlanRequest request) {
        return ApiResponse.<SubscriptionPlanResponse>builder()
                .result(planService.updatePlan(planId, request))
                .build();
    }

    /**
      Xóa gói tập theo ID
     */
    @DeleteMapping("/{planId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete a subscription plan",
            description = "Delete a plan permanently by its ID. Only ADMINs can perform this action"
    )
    public ApiResponse<String> deletePlan(@PathVariable String planId) {
        planService.deletePlan(planId);
        return ApiResponse.<String>builder()
                .result("Subscription plan has been deleted")
                .build();
    }
}
