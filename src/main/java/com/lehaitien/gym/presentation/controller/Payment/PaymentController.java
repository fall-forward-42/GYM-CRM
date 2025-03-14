package com.lehaitien.gym.presentation.controller.Payment;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Payment.PaymentRequest;
import com.lehaitien.gym.application.dto.response.Payment.PaymentResponse;
import com.lehaitien.gym.application.service.PaymentService;
import com.lehaitien.gym.domain.constant.PaymentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("")
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createPayment(request))
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<PaymentResponse>> getPaymentsByUser(@PathVariable String userId) {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getPaymentsByUser(userId))
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getPaymentsByStatus(status))
                .build();
    }

    @GetMapping("/{transactionId}")
    public ApiResponse<PaymentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentByTransactionId(transactionId))
                .build();
    }
}
