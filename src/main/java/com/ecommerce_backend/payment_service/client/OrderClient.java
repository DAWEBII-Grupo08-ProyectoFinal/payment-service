package com.ecommerce_backend.payment_service.client;

import com.ecommerce_backend.payment_service.client.dto.OrderPaymentInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/internal/{orderId}/payment-info")
    OrderPaymentInfoResponse getPaymentInfo(@PathVariable Long orderId);

    @PatchMapping("/api/orders/internal/{orderId}/status")
    void updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status
    );
}
