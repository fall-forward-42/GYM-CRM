package com.lehaitien.gym.presentation.controller;

import com.lehaitien.gym.application.dto.request.*;
import com.lehaitien.gym.application.dto.request.User.ResetPasswordByOtpRequest;
import com.lehaitien.gym.application.dto.request.User.SendOtpRequest;
import com.lehaitien.gym.application.dto.response.AuthenticationResponse;
import com.lehaitien.gym.application.dto.response.IntrospectResponse;
import com.lehaitien.gym.application.service.AuthenticationService;
import com.lehaitien.gym.application.service.OtpService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "API for user authentication and token management")
public class AuthenticationController {
    AuthenticationService authenticationService;
    OtpService otpService;

    @PostMapping("/send-otp")
    @Operation(summary = "Gửi mã OTP đến email để đặt lại mật khẩu")
    public ApiResponse<String> sendOtp(@RequestBody @Valid SendOtpRequest request) {
        otpService.sendOtp(request.getUsername(), request.getEmail());
        return ApiResponse.<String>builder()
                .result("OTP đã được gửi đến email: " + request.getEmail())
                .build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Đặt lại mật khẩu bằng mã OTP")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordByOtpRequest request) {
        otpService.resetPasswordByOtp(
                request.getUsername(),
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return ApiResponse.<String>builder()
                .result("Mật khẩu đã được đặt lại thành công.")
                .build();
    }



    @PostMapping("/token")
    @Operation(summary = "Authenticate user and get token", description = "Validates user credentials and returns a JWT token")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    @Operation(summary = "Check token validity", description = "Validates whether a token is active or expired")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token is valid", content = @Content(schema = @Schema(implementation = IntrospectResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid token")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Issues a new JWT token when the old one expires")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token is invalid")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidates the current access token")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User logged out successfully")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
