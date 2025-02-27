package com.lehaitien.gym.application.dto.response.Branch;


import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.domain.constant.BranchStatus;
import java.time.LocalDateTime;
import java.util.List;

public record BranchResponse(
        String branchId,
        String branchName,
        String address,
        String phone,
        BranchStatus status,

        List<UserResponse> users,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
