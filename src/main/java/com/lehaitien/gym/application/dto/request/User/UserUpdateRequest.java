package com.lehaitien.gym.application.dto.request.User;

import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String fullName;

    @Email(message = "EMAIL_INVALID")
    String email;

    String phone;
    String gender;

    @DobConstraint(min = 18, message = "INVALID_DOB")
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

}
