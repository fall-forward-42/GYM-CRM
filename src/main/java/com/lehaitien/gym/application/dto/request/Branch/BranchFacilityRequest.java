package com.lehaitien.gym.application.dto.request.Branch;


import com.lehaitien.gym.domain.constant.FacilityStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
public class BranchFacilityRequest {
    @NotBlank
    private String facilityName;

    @NotNull
    private FacilityStatus status;

    @Min(1)
    private Integer capacity;

    private String thumbnailFile;

    private List<String> imageFiles;
}

