package com.ecommerce_backend.payment_service.controller;

import com.ecommerce_backend.payment_service.dto.request.CreatePaymentRequest;
import com.ecommerce_backend.payment_service.dto.response.PaymentResponse;
import com.ecommerce_backend.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> findByOrderId(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                paymentService.findByOrderId(orderId)
        );
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.create(request));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<PaymentResponse> approve(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                paymentService.approve(id)
        );
    }

    @PatchMapping("/{id}/fail")
    public ResponseEntity<PaymentResponse> fail(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                paymentService.fail(id)
        );
    }
}
