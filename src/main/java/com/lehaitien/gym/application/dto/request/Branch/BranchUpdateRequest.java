package com.lehaitien.gym.application.dto.request.Branch;


import com.lehaitien.gym.domain.constant.BranchStatus;
import lombok.Builder;

@Builder
public record BranchUpdateRequest(
        String branchName,
        String address,
        String phone,
        BranchStatus status
) {}

