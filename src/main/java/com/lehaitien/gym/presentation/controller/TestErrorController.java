package com.lehaitien.gym.presentation.controller;

import com.lehaitien.gym.domain.exception.AppException;
import com.lehaitien.gym.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/errors")
@RequiredArgsConstructor
@Tag(name = "Error Testing", description = "API chỉ để test các mã lỗi HTTP")
public class TestErrorController {

    @GetMapping("/400")
    @Operation(summary = "Trigger 400 Bad Request")
    public void throwBadRequest() {
        throw new AppException(ErrorCode.INVALID_KEY);
    }

    @GetMapping("/401")
    @Operation(summary = "Trigger 401 Unauthorized")
    public void throwUnauthorized(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @GetMapping("/403")
    @Operation(summary = "Trigger 403 Forbidden")
    public void throwForbidden(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        throw new AppException(ErrorCode.UNAUTHORIZED); // Bạn có thể custom thêm ErrorCode.FORBIDDEN
    }

    @GetMapping("/404")
    @Operation(summary = "Trigger 404 Not Found")
    public void throwNotFound() {
        throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    @GetMapping("/500")
    @Operation(summary = "Trigger 500 Internal Server Error")
    public void throwInternalError() {
        throw new RuntimeException("Fake Internal Server Error");
    }
}
