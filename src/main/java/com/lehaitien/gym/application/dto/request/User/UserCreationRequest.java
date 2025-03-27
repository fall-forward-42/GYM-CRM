package com.lehaitien.gym.application.dto.request.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.lehaitien.gym.domain.constant.UserStatus;
import com.lehaitien.gym.domain.validator.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Payload for creating a new user")
public class UserCreationRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 4, message = "USERNAME_INVALID")
    @Schema(description = "Unique username for the user", example = "john_doe")
    String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 6, message = "INVALID_PASSWORD")
    @Schema(description = "Password with at least 6 characters", example = "securePass123")
    String password;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    @Schema(description = "Valid email address", example = "john@example.com")
    String email;

    @Schema(description = "Full name of the user", example = "John Doe")
    String fullName;

    @Schema(description = "User's phone number", example = "0123456789")
    String phone;

    @Schema(description = "Gender of the user", example = "Male")
    String gender;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    @Schema(description = "Date of birth (at least 10 years old)", example = "2000-01-01")
    LocalDate dob;

    @Schema(description = "National ID card number (CCCD)", example = "123456789012")
    String cccd;

    @Schema(description = "Home address", example = "123 Main St, Hanoi")
    String address;

    @Schema(description = "Height in meters", example = "1.75")
    BigDecimal height;

    @Schema(description = "Weight in kilograms", example = "70.5")
    BigDecimal weight;

    @Schema(description = "Known health issues, if any", example = "Diabetes")
    String healthIssues;

    @Schema(description = "Set of assigned roles", example = "[\"CLIENT\"]")
    Set<String> roles;

    @Schema(description = "Account status", example = "ACTIVE")
    UserStatus status;

    @Schema(description = "ID of assigned coach (if any)", example = "coach-uuid-123")
    String coachId;

    @Schema(description = "ID of the branch this user belongs to", example = "branch-uuid-456")
    String branchId;

}
