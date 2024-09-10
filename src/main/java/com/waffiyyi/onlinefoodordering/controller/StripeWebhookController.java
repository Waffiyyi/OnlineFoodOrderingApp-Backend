package com.waffiyyi.onlinefoodordering.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

  private final OrderRepository orderRepository;

  @Value("${stripe.signing.key}")
  private String signingKey;

  @PostMapping
  public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                  @RequestHeader("Stripe-Signature")
                                                  String sigHeader) {
    String endpointSecret = signingKey;
    Event event = null;

    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
    }

    if ("checkout.session.completed".equals(event.getType())) {
      Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(
         null);
      if (session != null) {
        // Payment was successful
        Long orderId = Long.valueOf(
           session.getClientReferenceId()); // Assuming you pass order ID as a reference
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null) {
          order.setOrderStatus(ORDER_STATUS.COMPLETED);
          orderRepository.save(order);
        }
      }
    } else if ("checkout.session.expired".equals(event.getType())) {
      // Handle payment failure case
      Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(
         null);
      if (session != null) {
        Long orderId = Long.valueOf(session.getClientReferenceId());
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null) {
          order.setOrderStatus(ORDER_STATUS.CANCELED);
          orderRepository.save(order);
        }
      }
    }

    return ResponseEntity.ok("Webhook received successfully");
  }
}