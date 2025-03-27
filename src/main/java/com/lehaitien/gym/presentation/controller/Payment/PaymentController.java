package com.lehaitien.gym.presentation.controller.Payment;

import com.lehaitien.gym.application.dto.request.ApiResponse;
import com.lehaitien.gym.application.dto.request.Payment.PaymentRequest;
import com.lehaitien.gym.application.dto.response.Payment.PaymentResponse;
import com.lehaitien.gym.application.service.PaymentService;
import com.lehaitien.gym.domain.constant.PaymentMethod;
import com.lehaitien.gym.domain.constant.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "APIs for handling payment transactions")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add")
    @Operation(
            summary = "Create a new payment transaction",
            description = "Creates a payment based on the subscription or service"
    )
    public ApiResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request,
                  @RequestParam
                          PaymentMethod paymentMethod) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createPayment(request,paymentMethod))
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get all payments by user ID",
            description = "Fetches all payments made by a specific user"
    )
    public ApiResponse<List<PaymentResponse>> getPaymentsByUser(@PathVariable String userId) {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getPaymentsByUser(userId))
                .build();
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Get payments by status",
            description = "Returns a list of payments filtered by status (e.g., PAID, PENDING, FAILED)"
    )
    public ApiResponse<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ApiResponse.<List<PaymentResponse>>builder()
                .result(paymentService.getPaymentsByStatus(status))
                .build();
    }

    @GetMapping("/{transactionId}")
    @Operation(
            summary = "Get a payment by transaction ID",
            description = "Fetches payment details based on the unique transaction ID"
    )
    public ApiResponse<PaymentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentByTransactionId(transactionId))
                .build();
    }
}
