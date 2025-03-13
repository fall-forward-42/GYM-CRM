package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.application.service.BranchFacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class BranchFacilityController {

    private final BranchFacilityService facilityService;

    @PostMapping("/branches/{branchId}")
    public ApiResponse<BranchFacilityResponse> createFacility(
            @PathVariable String branchId, @Valid @RequestBody BranchFacilityRequest request) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.createFacility(branchId, request))
                .build();
    }

    @GetMapping("/branches/{branchId}")
    public ApiResponse<List<BranchFacilityResponse>> getFacilities(@PathVariable String branchId) {
        return ApiResponse.<List<BranchFacilityResponse>>builder()
                .result(facilityService.getFacilitiesByBranch(branchId))
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<BranchFacilityResponse>> getAllFacilities() {
        return ApiResponse.<List<BranchFacilityResponse>>builder()
                .result(facilityService.getFacilities())
                .build();
    }

    @PutMapping("/{facilityId}")
    public ApiResponse<BranchFacilityResponse> updateFacility(
            @PathVariable String facilityId, @Valid @RequestBody BranchFacilityRequest request) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.updateFacility(facilityId, request))
                .build();
    }

    @GetMapping("/{facilityId}")
    public ApiResponse<BranchFacilityResponse> getFacility(
            @PathVariable String facilityId) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.getFacilityById(facilityId))
                .build();
    }

    @DeleteMapping("/{facilityId}")
    public ApiResponse<String> deleteFacility(@PathVariable String facilityId) {
        facilityService.deleteFacility(facilityId);
        return ApiResponse.<String>builder()
                .result("Facility has been deleted")
                .build();
    }
}

