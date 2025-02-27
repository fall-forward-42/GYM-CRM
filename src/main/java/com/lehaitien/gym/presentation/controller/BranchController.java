package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Branch.BranchCreationRequest;
import com.lehaitien.gym.application.dto.request.Branch.BranchUpdateRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchResponse;
import com.lehaitien.gym.application.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;


    @PutMapping("/{branchId}/add-user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ApiResponse<BranchResponse> addUserToBranch(
            @PathVariable String branchId, @PathVariable String userId) {
        return ApiResponse.<BranchResponse>builder().result(branchService.addUserToBranch(branchId, userId)).build();
    }

    @DeleteMapping("/{branchId}/delete-users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ApiResponse<BranchResponse> removeUserFromBranch(
            @PathVariable String branchId, @PathVariable String userId) {

        BranchResponse response = branchService.removeUserFromBranch(branchId, userId);

        return ApiResponse.<BranchResponse>builder().result(response).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BranchResponse> createBranch(@Valid @RequestBody BranchCreationRequest request) {
        return ApiResponse.<BranchResponse>builder().result(branchService.createBranch(request)).build();
    }

    @GetMapping
    public ApiResponse<List<BranchResponse>> getAllBranches() {
        return ApiResponse.<List<BranchResponse>>builder().result(branchService.getAllBranches()).build();
    }

    @GetMapping("/{branchId}")
    public ApiResponse<BranchResponse> getBranchById(@PathVariable String branchId) {
        return ApiResponse.<BranchResponse>builder().result(branchService.getBranchById(branchId)).build();
    }

    @PutMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BranchResponse> updateBranch(
            @PathVariable String branchId, @Valid @RequestBody BranchUpdateRequest request) {
        return ApiResponse.<BranchResponse>builder().result(branchService.updateBranch(branchId,request)).build();
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteBranch(@PathVariable String branchId) {
        branchService.deleteBranch(branchId);
        return ApiResponse.<String>builder().result("Branch has been deleted").build();

    }
}
