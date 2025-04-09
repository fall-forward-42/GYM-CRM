package com.lehaitien.gym.application.dto.request.User;

import lombok.Data;

@Data
public class SendOtpRequest {
    private String username;
    private String email;
}
