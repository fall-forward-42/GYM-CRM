package com.lehaitien.gym.application.dto.request.Branch;


import com.lehaitien.gym.domain.constant.FacilityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "Request payload for creating or updating a facility in a branch")
@Getter
@Setter
@Builder
public class BranchFacilityRequest {

    @NotBlank(message = "FACILITY_NAME_REQUIRED")
    @Schema(description = "Name of the facility", example = "Yoga Room")
    private String facilityName;

    @NotNull(message = "STATUS_REQUIRED")
    @Schema(description = "Operational status of the facility", example = "OPEN")
    private FacilityStatus status;

    @Min(value = 1, message = "CAPACITY_MINIMUM_ONE")
    @Schema(description = "Maximum number of people allowed", example = "30")
    private Integer capacity;

    @Schema(description = "Thumbnail image file URL", example = "https://cdn.example.com/images/facility-thumb.jpg")
    private String thumbnailFile;

    @Schema(description = "List of image file URLs for the facility", example = "[\"https://cdn.example.com/img1.jpg\", \"https://cdn.example.com/img2.jpg\"]")
    private List<String> imageFiles;
}

