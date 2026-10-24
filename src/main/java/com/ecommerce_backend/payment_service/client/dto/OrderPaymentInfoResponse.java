package com.ecommerce_backend.payment_service.client.dto;

import java.math.BigDecimal;

public record OrderPaymentInfoResponse(
        Long id,
        String status,
        BigDecimal total
) {
}
