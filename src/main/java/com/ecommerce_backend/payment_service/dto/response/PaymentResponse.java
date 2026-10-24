package com.ecommerce_backend.payment_service.dto.response;

import com.ecommerce_backend.payment_service.entity.PaymentMethod;
import com.ecommerce_backend.payment_service.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
