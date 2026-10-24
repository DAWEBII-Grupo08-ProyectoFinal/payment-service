package com.ecommerce_backend.payment_service.event;

import com.ecommerce_backend.payment_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(PaymentStatusChangedEvent event) {
        String routingKey = "payment." + event.status().toLowerCase();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                routingKey,
                event
        );
    }

}
