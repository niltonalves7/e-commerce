package com.project.ecomerce.payment.stripe.controller;

import com.project.ecomerce.common.enums.PaymentStatus;
import com.project.ecomerce.payment.stripe.service.StripePaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe/webhook")
public class WebhookController {

    //@Value("${STRIPE_WEBHOOK_SECRET_KEY}")
    private String webhookSecret;

    private final StripePaymentService stripepaymentService;

    public WebhookController(StripePaymentService stripepaymentService) {
        this.stripepaymentService = stripepaymentService;
    }

    @PostMapping
    public void handleStripeWebhook(@RequestBody String payload,
                                    @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid webhook");
        }

        String json = event.getData().getObject().toJson();
        PaymentIntent intent = PaymentIntent.GSON.fromJson(json, PaymentIntent.class);

        switch (event.getType()) {
            case "payment_intent.succeeded" ->
                    stripepaymentService.updateStripePaymentStatus(intent.getId(), PaymentStatus.PAID);

            case "payment_intent.payment_failed" ->
                    stripepaymentService.updateStripePaymentStatus(intent.getId(), PaymentStatus.FAILED);
        }
    }
}
