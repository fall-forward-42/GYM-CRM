package com.lehaitien.gym.domain.mapper;


import com.lehaitien.gym.application.dto.response.Subscription.UserSubscriptionResponse;
import com.lehaitien.gym.domain.model.Subsription.UserSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper {

    UserSubscriptionMapper INSTANCE = Mappers.getMapper(UserSubscriptionMapper.class);


    @Mapping(source = "subscription.user.userId", target = "userId")
    @Mapping(source = "subscription.subscriptionPlan.planId", target = "planId")
    @Mapping(source = "subscription.subscriptionPlan.planName", target = "planName")
    @Mapping(source = "subscription.startDate", target = "startDate")
    @Mapping(source = "subscription.endDate", target = "endDate")
    @Mapping(source = "subscription.status", target = "status")
    UserSubscriptionResponse toUserSubscriptionResponse(UserSubscription subscription);

    @Mapping(source = "subscription.user.userId", target = "userId")
    @Mapping(source = "subscription.subscriptionPlan.planId", target = "planId")
    @Mapping(source = "subscription.subscriptionPlan.planName", target = "planName")
    @Mapping(source = "subscription.startDate", target = "startDate")
    @Mapping(source = "subscription.endDate", target = "endDate")
    @Mapping(source = "subscription.status", target = "status")
    @Mapping(source = "leftDays", target = "leftDays")
    UserSubscriptionResponse toUserSubscriptionResponse(UserSubscription subscription, int leftDays);
}
