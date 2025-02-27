package com.lehaitien.gym.application.dto.response.User;

import com.lehaitien.gym.domain.constant.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String fullName;
    String email;
    String phone;
    String gender;
    LocalDate dob;
    String cccd;
    String address;
    BigDecimal height;
    BigDecimal weight;
    String healthIssues;
    Set<String> roles;
    UserStatus status;
    String coachId;
    String branchId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
