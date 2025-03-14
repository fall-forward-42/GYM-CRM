package com.lehaitien.gym.presentation.controller.Subscription;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Subcription.SubscriptionPlanRequest;
import com.lehaitien.gym.application.dto.response.Subscription.SubscriptionPlanResponse;
import com.lehaitien.gym.application.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService planService;

    /**
     * Tạo gói tập mới
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    public ApiResponse<List<SubscriptionPlanResponse>> getAllPlans() {
        return ApiResponse.<List<SubscriptionPlanResponse>>builder()
                .result(planService.getAllPlans())
                .build();
    }

    /**
     * Lấy gói tập theo ID
     */
    @GetMapping("/{planId}")
    public ApiResponse<SubscriptionPlanResponse> getPlanById(@PathVariable String planId) {
        return ApiResponse.<SubscriptionPlanResponse>builder()
                .result(planService.getPlanById(planId))
                .build();
    }

    /**
     * Lấy gói tập theo tên
     */
    @GetMapping("/name/{planName}")
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
    public ApiResponse<String> deletePlan(@PathVariable String planId) {
        planService.deletePlan(planId);
        return ApiResponse.<String>builder()
                .result("Subscription plan has been deleted")
                .build();
    }
}
