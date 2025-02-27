package com.lehaitien.gym.application.dto.request.Branch;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BranchCreationRequest(
        @NotBlank String branchName,
        @NotBlank String address,
        @NotBlank String phone
) {}
