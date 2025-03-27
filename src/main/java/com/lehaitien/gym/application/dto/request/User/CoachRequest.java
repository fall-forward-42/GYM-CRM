package com.lehaitien.gym.application.dto.request.User;

import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.validator.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Payload for creating a new coach")
public class CoachRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 4, message = "USERNAME_INVALID")
    @Schema(description = "Coach's unique username", example = "coach_nghia")
    String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 6, message = "INVALID_PASSWORD")
    @Schema(description = "Password for coach login", example = "123123")
    String password;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    @Schema(description = "Valid email address", example = "coach@example.com")
    String email;

    @Schema(description = "Full name of the coach", example = "Nguyen Van A")
    String fullName;

    @Schema(description = "Phone number", example = "0987654321")
    String phone;

    @Schema(description = "Gender", example = "Male")
    String gender;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    @Schema(description = "Date of birth (coach must be at least 10 years old)", example = "1995-06-15")
    LocalDate dob;

    @Schema(description = "National ID card number (CCCD)", example = "012345678912")
    String cccd;

    @Schema(description = "Address", example = "123 Lê Lợi, District 1, HCM")
    String address;

    @Schema(description = "Height in meters", example = "1.75")
    BigDecimal height;

    @Schema(description = "Weight in kilograms", example = "68.5")
    BigDecimal weight;

    @Schema(description = "Health issues if any", example = "None")
    String healthIssues;

    @Schema(description = "Assigned roles", example = "[\"COACH\"]")
    Set<String> roles;

    @Schema(description = "User status", example = "ACTIVE")
    UserStatus status;

    @Schema(description = "Branch ID where coach is working", example = "branch-uuid-xyz")
    String branchId;

    @Schema(description = "Monthly salary", example = "15000000")
    Integer salary;

    @Schema(description = "Specialization field", example = "Weight Loss, Muscle Gain")
    String specialization;

    @Schema(description = "Years of experience", example = "5")
    Integer experienceYears;

    @Schema(description = "Certifications the coach holds", example = "ACE Certified, CPT Level 3")
    String certifications;
}
