package com.lehaitien.gym.application.service;


import com.lehaitien.gym.application.dto.request.Subcription.SubscriptionPlanRequest;
import com.lehaitien.gym.application.dto.response.Subscription.SubscriptionPlanResponse;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.SubscriptionPlanMapper;
import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import com.lehaitien.gym.domain.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanMapper planMapper;

    /**
     * Tạo gói tập mới
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SubscriptionPlanResponse createPlan(SubscriptionPlanRequest request) {
        if (planRepository.existsByPlanName(request.planName())) {
            throw new AppException(ErrorCode.PLAN_ALREADY_EXISTS);
        }

        SubscriptionPlan plan = planMapper.toSubscriptionPlan(request);
        plan = planRepository.save(plan);
        log.info("Created new subscription plan: {}", plan.getPlanName());

        return planMapper.toSubscriptionPlanResponse(plan);
    }

    /**
     * Lấy danh sách tất cả các gói tập
     */
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planMapper::toSubscriptionPlanResponse)
                .toList();
    }

    /**
     * Lấy gói tập theo ID
     */
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getPlanById(String planId) {
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        return planMapper.toSubscriptionPlanResponse(plan);
    }

    /**
     * Lấy gói tập theo tên
     */
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getPlanByName(String planName) {
        SubscriptionPlan plan = planRepository.findByPlanName(planName)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        return planMapper.toSubscriptionPlanResponse(plan);
    }

    /**
     * Cập nhật thông tin gói tập
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SubscriptionPlanResponse updatePlan(String planId, SubscriptionPlanRequest request) {
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        planMapper.updateSubscriptionPlan(plan, request);
        plan = planRepository.save(plan);

        return planMapper.toSubscriptionPlanResponse(plan);
    }

    /**
     * Xóa gói tập theo ID
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlan(String planId) {
        if (!planRepository.existsById(planId)) {
            throw new AppException(ErrorCode.PLAN_NOT_FOUND);
        }

        planRepository.deleteById(planId);
        log.info("Deleted subscription plan with ID: {}", planId);
    }
}
