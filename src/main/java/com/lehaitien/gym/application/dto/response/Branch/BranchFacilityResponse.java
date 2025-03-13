package com.lehaitien.gym.application.dto.response.Branch;


import com.lehaitien.gym.domain.constant.FacilityStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BranchFacilityResponse(
        String facilityId,
        String branchId,
        String facilityName,
        FacilityStatus status,
        Integer capacity,
        String thumbnailUrl,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

