package com.project.ecommerce.infra.stripe;

import com.google.gson.JsonParser;
import com.project.ecommerce.domain.payment.service.PaymentService;
import com.project.ecommerce.shared.enums.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StripeWebhookHandler {

    private final PaymentService paymentService;

    @Value("${STRIPE_WEBHOOK_SECRET_KEY}")
    private String webhookSecret;

    public void handle(String payload, String sigHeader) throws StripeException {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

        PaymentStatus status = switch (event.getType()) {
            case "payment_intent.succeeded" -> PaymentStatus.PAID;
            case "payment_intent.payment_failed" -> PaymentStatus.FAILED;
            default -> null;
        };

        if (status == null) return;

        // busca diretamente pelo ID do PaymentIntent contido no evento
        String paymentIntentId = JsonParser.parseString(payload)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonObject("object")
                .get("id")
                .getAsString();

        paymentService.handleWebhook(paymentIntentId, status);
    }
}