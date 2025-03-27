package com.lehaitien.gym.application.dto.request.Branch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Payload to create a new gym branch")
public record BranchCreationRequest(
        @NotBlank(message = "BRANCH_NAME_REQUIRED")
        @Schema(description = "Name of the branch", example = "Hanoi Fitness Center")
        String branchName,

        @NotBlank(message = "ADDRESS_REQUIRED")
        @Schema(description = "Physical address of the branch", example = "123 Main St, Hanoi")
        String address,

        @NotBlank(message = "PHONE_REQUIRED")
        @Schema(description = "Contact phone number", example = "0987654321")
        String phone
) {}
