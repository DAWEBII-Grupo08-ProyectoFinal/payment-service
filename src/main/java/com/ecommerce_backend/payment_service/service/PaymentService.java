package com.ecommerce_backend.payment_service.service;

import com.ecommerce_backend.payment_service.client.OrderClient;
import com.ecommerce_backend.payment_service.client.dto.OrderPaymentInfoResponse;
import com.ecommerce_backend.payment_service.dto.request.CreatePaymentRequest;
import com.ecommerce_backend.payment_service.dto.response.PaymentResponse;
import com.ecommerce_backend.payment_service.entity.Payment;
import com.ecommerce_backend.payment_service.entity.PaymentStatus;
import com.ecommerce_backend.payment_service.event.PaymentEventPublisher;
import com.ecommerce_backend.payment_service.event.PaymentStatusChangedEvent;
import com.ecommerce_backend.payment_service.exception.PaymentException;
import com.ecommerce_backend.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional(readOnly = true)
    public PaymentResponse findByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new PaymentException("Pago no encontrado")
                );

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {

        if (paymentRepository.existsByOrderId(request.orderId())) {
            throw new PaymentException(
                    "El pedido ya tiene un pago registrado"
            );
        }

        OrderPaymentInfoResponse order =
                orderClient.getPaymentInfo(request.orderId());

        if (!"PENDING_PAYMENT".equals(order.status())) {
            throw new PaymentException(
                    "El pedido no está pendiente de pago"
            );
        }

        Payment payment = Payment.builder()
                .orderId(order.id())
                .amount(order.total())
                .method(request.method())
                .status(PaymentStatus.PENDING)
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse approve(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new PaymentException("Pago no encontrado")
                );

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentException(
                    "Solo se puede aprobar un pago pendiente"
            );
        }

        payment.setStatus(PaymentStatus.APPROVED);

        paymentEventPublisher.publish(
                new PaymentStatusChangedEvent(
                        payment.getId(),
                        payment.getOrderId(),
                        payment.getAmount(),
                        payment.getStatus().name()
                )
        );

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse fail(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new PaymentException("Pago no encontrado")
                );

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentException(
                    "Solo se puede rechazar un pago pendiente"
            );
        }

        payment.setStatus(PaymentStatus.FAILED);

        paymentEventPublisher.publish(
                new PaymentStatusChangedEvent(
                        payment.getId(),
                        payment.getOrderId(),
                        payment.getAmount(),
                        payment.getStatus().name()
                )
        );

        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
