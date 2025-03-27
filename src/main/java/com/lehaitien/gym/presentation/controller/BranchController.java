package com.lehaitien.gym.presentation.controller;


import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Branch.BranchCreationRequest;
import com.lehaitien.gym.application.dto.request.Branch.BranchUpdateRequest;
import com.lehaitien.gym.application.dto.response.Branch.BranchResponse;
import com.lehaitien.gym.application.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
@Tag(name = "Branch Management", description = "Endpoints for managing gym branches and user assignments")
public class BranchController {

    private final BranchService branchService;


    @PutMapping("/{branchId}/add-user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    @Operation(
            summary = "Add a user to a branch",
            parameters = {
                    @Parameter(name = "branchId", description = "Branch UUID", required = true),
                    @Parameter(name = "userId", description = "User UUID", required = true)
            }
    )
    public ApiResponse<BranchResponse> addUserToBranch(
            @PathVariable String branchId, @PathVariable String userId) {
        return ApiResponse.<BranchResponse>builder().result(branchService.addUserToBranch(branchId, userId)).build();
    }

    @DeleteMapping("/{branchId}/delete-users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    @Operation(
            summary = "Remove a user from a branch",
            parameters = {
                    @Parameter(name = "branchId", description = "Branch UUID", required = true),
                    @Parameter(name = "userId", description = "User UUID", required = true)
            }
    )
    public ApiResponse<BranchResponse> removeUserFromBranch(
            @PathVariable String branchId, @PathVariable String userId) {

        BranchResponse response = branchService.removeUserFromBranch(branchId, userId);

        return ApiResponse.<BranchResponse>builder().result(response).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new branch",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload to create a new branch",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BranchCreationRequest.class))
            )
    )
    public ApiResponse<BranchResponse> createBranch(@Valid @RequestBody BranchCreationRequest request) {
        return ApiResponse.<BranchResponse>builder().result(branchService.createBranch(request)).build();
    }

    @GetMapping
    @Operation(
            summary = "Get all branches"
    )
    public ApiResponse<List<BranchResponse>> getAllBranches() {
        return ApiResponse.<List<BranchResponse>>builder().result(branchService.getAllBranches()).build();
    }

    @GetMapping("/{branchId}")
    @Operation(
            summary = "Get branch by ID",
            parameters = {
                    @Parameter(name = "branchId", description = "Branch UUID", required = true)
            }
    )
    public ApiResponse<BranchResponse> getBranchById(@PathVariable String branchId) {
        return ApiResponse.<BranchResponse>builder().result(branchService.getBranchById(branchId)).build();
    }

    @PutMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update a branch",
            parameters = {
                    @Parameter(name = "branchId", description = "Branch UUID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload to update branch info",
                    content = @Content(schema = @Schema(implementation = BranchUpdateRequest.class))
            )
    )
    public ApiResponse<BranchResponse> updateBranch(
            @PathVariable String branchId, @Valid @RequestBody BranchUpdateRequest request) {
        return ApiResponse.<BranchResponse>builder().result(branchService.updateBranch(branchId,request)).build();
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete a branch",
            parameters = {
                    @Parameter(name = "branchId", description = "Branch UUID", required = true)
            }
    )
    public ApiResponse<String> deleteBranch(@PathVariable String branchId) {
        branchService.deleteBranch(branchId);
        return ApiResponse.<String>builder().result("Branch has been deleted").build();

    }
}
