package com.lehaitien.gym.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request payload for user authentication (login)")
public class AuthenticationRequest {
    @Schema(description = "Username or email used for login", example = "admin")
    String username;
    @Schema(description = "User password (plaintext or hashed, depending on API)", example = "admin")
    String password;
}