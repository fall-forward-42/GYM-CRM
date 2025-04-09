package com.lehaitien.gym.application.dto.request.User;

import lombok.Data;

@Data
public class ResetPasswordByOtpRequest {
    private String username;
    private String email;
    private String otp;
    private String newPassword;
}
