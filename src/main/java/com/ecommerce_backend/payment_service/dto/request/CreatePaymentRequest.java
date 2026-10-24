package com.ecommerce_backend.payment_service.dto.request;

import com.ecommerce_backend.payment_service.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull Long orderId,
        @NotNull PaymentMethod method
        ) {
}
