package com.lehaitien.gym.presentation.controller;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.User.CoachRequest;
import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.application.service.UserService;
import com.lehaitien.gym.domain.constant.PredefinedRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User Management", description = "Endpoints for managing users and coaches")
public class UserController {
    UserService userService;


    @PostMapping("/registration")
    @Operation(
            summary = "Register a new client user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Client registration payload",
                    content = @Content(schema = @Schema(implementation = UserCreationRequest.class))
            )
    )
    ApiResponse<UserResponse> createUser( @RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    @PostMapping("/registration-coach")
    @Operation(
            summary = "Register a new coach",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Coach registration payload",
                    content = @Content(schema = @Schema(implementation = CoachRequest.class))
            )
    )
    public ApiResponse<UserResponse> registerCoach(@RequestBody @Valid CoachRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createCoach(request))
                .build();
    }

    @GetMapping("/role/{roleName}")
    @Operation(
            summary = "Get users by role",
            parameters = {
                    @Parameter(name = "roleName", description = "Role name (e.g. CLIENT, COACH)", required = true)
            }
    )
    ApiResponse<List<UserResponse>> getUsersByRole(@PathVariable("roleName") String roleName) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsersByRole(roleName))
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Get all users"
    )
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get a specific user by ID",
            parameters = {
                    @Parameter(name = "userId", description = "UUID of the user", required = true)
            }
    )
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    @Operation(
            summary = "Get authenticated user's info"
    )
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    @Operation(
            summary = "Delete a user by ID",
            parameters = {
                    @Parameter(name = "userId", description = "UUID of the user", required = true)
            }
    )
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    @Operation(
            summary = "Update user details",
            parameters = {
                    @Parameter(name = "userId", description = "UUID of the user", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update payload",
                    content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
            )
    )
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
