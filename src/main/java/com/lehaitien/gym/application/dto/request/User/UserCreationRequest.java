package com.lehaitien.gym.application.dto.request.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    String email;

    String fullName;
    String phone;
    String gender;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;

    String cccd;
    String address;

    BigDecimal height;
    BigDecimal weight;

    String healthIssues;

    Set<String> roles; // tên của các Role, xử lý logic ánh xạ trong service

    UserStatus status;

    String coachId; // UUID của Coach quản lý client, xử lý ánh xạ trong service

    String branchId;
}