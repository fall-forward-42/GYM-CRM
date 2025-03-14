package com.lehaitien.gym.domain.mapper;


import com.lehaitien.gym.application.dto.request.Subcription.SubscriptionPlanRequest;
import com.lehaitien.gym.application.dto.response.Subscription.SubscriptionPlanResponse;
import com.lehaitien.gym.domain.model.Subsription.SubscriptionPlan;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubscriptionPlanMapper {

    @Mapping(target = "planId", ignore = true)
    SubscriptionPlan toSubscriptionPlan(SubscriptionPlanRequest request);

    SubscriptionPlanResponse toSubscriptionPlanResponse(SubscriptionPlan plan);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubscriptionPlan(@MappingTarget SubscriptionPlan plan, SubscriptionPlanRequest request);
}