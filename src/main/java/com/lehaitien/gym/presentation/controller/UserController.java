package com.lehaitien.gym.presentation.controller;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.User.UserCreationRequest;
import com.lehaitien.gym.application.dto.request.User.UserUpdateRequest;
import com.lehaitien.gym.application.dto.response.User.UserResponse;
import com.lehaitien.gym.application.service.UserService;
import com.lehaitien.gym.domain.constant.PredefinedRole;
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
public class UserController {
    UserService userService;

//    @PostMapping
//    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.createUser(request))
//                .build();
//    }

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestParam(name = "role", defaultValue = PredefinedRole.CLIENT_ROLE) String  role, @RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request,role))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
