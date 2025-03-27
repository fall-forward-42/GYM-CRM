package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Branch.BranchFacilityRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchFacilityResponse;
import com.lehaitien.gym.application.service.BranchFacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
@Tag(name = "Facility Management", description = "Manage branch facilities like gym rooms, equipment, etc.")
public class BranchFacilityController {

    private final BranchFacilityService facilityService;

    @PostMapping("/branches/{branchId}")
    @Operation(summary = "Create a new facility for a branch")
    public ApiResponse<BranchFacilityResponse> createFacility(
            @PathVariable String branchId, @Valid @RequestBody BranchFacilityRequest request) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.createFacility(branchId, request))
                .build();
    }

    @GetMapping("/branches/{branchId}")
    @Operation(summary = "Get all facilities for a specific branch")
    public ApiResponse<List<BranchFacilityResponse>> getFacilities(@PathVariable String branchId) {
        return ApiResponse.<List<BranchFacilityResponse>>builder()
                .result(facilityService.getFacilitiesByBranch(branchId))
                .build();
    }

    @GetMapping("")
    @Operation(summary = "Get all facilities across all branches")
    public ApiResponse<List<BranchFacilityResponse>> getAllFacilities() {
        return ApiResponse.<List<BranchFacilityResponse>>builder()
                .result(facilityService.getFacilities())
                .build();
    }

    @PutMapping("/{facilityId}")
    @Operation(summary = "Update an existing facility by its ID")
    public ApiResponse<BranchFacilityResponse> updateFacility(
            @PathVariable String facilityId, @Valid @RequestBody BranchFacilityRequest request) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.updateFacility(facilityId, request))
                .build();
    }

    @GetMapping("/{facilityId}")
    @Operation(summary = "Get details of a specific facility by ID")
    public ApiResponse<BranchFacilityResponse> getFacility(
            @PathVariable String facilityId) {
        return ApiResponse.<BranchFacilityResponse>builder()
                .result(facilityService.getFacilityById(facilityId))
                .build();
    }

    @DeleteMapping("/{facilityId}")
    @Operation(summary = "Delete a facility by its ID")
    public ApiResponse<String> deleteFacility(@PathVariable String facilityId) {
        facilityService.deleteFacility(facilityId);
        return ApiResponse.<String>builder()
                .result("Facility has been deleted")
                .build();
    }
}

