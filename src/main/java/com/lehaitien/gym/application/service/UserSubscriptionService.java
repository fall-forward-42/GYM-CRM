package com.lehaitien.gym.application.service;


import com.lehaitien.gym.application.dto.request.Subcription.UserSubscriptionRequest;
import com.lehaitien.gym.application.dto.response.Subscription.UserSubscriptionResponse;
import com.lehaitien.gym.domain.constant.SortDirection;
import com.lehaitien.gym.domain.constant.SubscriptionStatus;
import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import com.lehaitien.gym.domain.mapper.UserSubscriptionMapper;
import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import com.lehaitien.gym.domain.model.Subsription.UserSubscription;
import com.lehaitien.gym.domain.model.User.User;
import com.lehaitien.gym.domain.repository.SubscriptionPlanRepository;
import com.lehaitien.gym.domain.repository.UserRepository;
import com.lehaitien.gym.domain.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSubscriptionService {

    private final UserSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserSubscriptionMapper subscriptionMapper;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserSubscriptionResponse createSubscription(UserSubscriptionRequest request) {
        log.info("Creating subscription for user: {} with plan: {}", request.getUserId(), request.getPlanId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        SubscriptionPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));


        //Cộng dồn ngày tháng cho thành viên
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDuration());

        UserSubscription subscription = UserSubscription.builder()
                .user(user)
                .subscriptionPlan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscription = subscriptionRepository.save(subscription);
        log.info("Subscription created successfully for user {}", user.getUsername());

        return subscriptionMapper.toUserSubscriptionResponse(subscription);
    }

    @Transactional(readOnly = true)
    public List<UserSubscriptionResponse> getSubscriptionsByStatus( SubscriptionStatus status, SortDirection sortDirection) {
        // Lấy ngày hiện tại theo múi giờ Việt Nam
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Lọc danh sách subscription theo status
        List<UserSubscription> subscriptions = subscriptionRepository.findByStatus(status);

        // Tính leftDays và sắp xếp theo yêu cầu
        List<UserSubscriptionResponse> responses = subscriptions.stream()
                .map(subscription -> {
                    int leftDays = (int) ChronoUnit.DAYS.between(currentDate, subscription.getEndDate()); // Tính số ngày còn lại
                    return subscriptionMapper.toUserSubscriptionResponse(subscription, leftDays);
                })
                .sorted((s1, s2) -> sortDirection == SortDirection.ASC
                        ? Integer.compare(s1.leftDays(), s2.leftDays())   // Sắp xếp tăng dần
                        : Integer.compare(s2.leftDays(), s1.leftDays()))  // Sắp xếp giảm dần
                .toList();

        return responses;
    }


    @Transactional(readOnly = true)
    public List<UserSubscriptionResponse> getSubscriptionsByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy ngày hiện tại theo múi giờ Việt Nam
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));


        return subscriptionRepository.findByUser(user).stream()
                .map(userSubscription -> {
                    int leftDays = (int) ChronoUnit.DAYS.between(currentDate, userSubscription.getEndDate()); // Tính số ngày còn lại
                    return subscriptionMapper.toUserSubscriptionResponse(userSubscription, leftDays);
                })
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelSubscription(String subscriptionId) {
        UserSubscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

        log.info("Subscription {} has been canceled", subscriptionId);
    }
}

