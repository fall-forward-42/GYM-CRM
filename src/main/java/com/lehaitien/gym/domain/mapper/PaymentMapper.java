package com.lehaitien.gym.domain.mapper;

import com.lehaitien.gym.application.dto.response.Payment.PaymentResponse;
import com.lehaitien.gym.domain.model.Payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "user.userId", target = "userId")
    PaymentResponse toPaymentResponse(Payment payment);
}
