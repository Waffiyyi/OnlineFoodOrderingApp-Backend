package com.waffiyyi.onlinefoodordering.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

   private final OrderRepository orderRepository;
   private final ObjectMapper objectMapper = new ObjectMapper();
   @Value("${stripe.signing.key}")
   private String signingKey;

   @PostMapping
   public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                   @RequestHeader("Stripe-Signature")
                                                   String sigHeader) {
      log.info("Received Stripe signature header: " + sigHeader);
      log.info("Webhook initiated with payload: " + payload);

      Event event;
      try {
         event = Webhook.constructEvent(payload, sigHeader, signingKey);
      } catch (SignatureVerificationException e) {
         log.error("Invalid Stripe signature: " + e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
      } catch (Exception e) {
         log.error("Failed to parse webhook event: " + e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
           "Webhook error");
      }

      String eventType = event.getType();
      log.info("Stripe event type: " + eventType);

      String clientReferenceId = extractClientReferenceId(payload);
      if (clientReferenceId == null || !orderRepository.existsById(
        Long.valueOf(clientReferenceId))) {
         log.info("Ignoring event; clientReferenceId does not match a known Order ID.");
         return ResponseEntity.ok("Event ignored");
      }

      switch (eventType) {
         case "checkout.session.completed":
         case "checkout.session.async_payment_succeeded":
            updateOrderStatus(payload, ORDER_STATUS.PAYMENT_COMPLETED);
            break;
         case "checkout.session.expired":
         case "checkout.session.async_payment_failed":
         case "payment_intent.canceled":
            updateOrderStatus(payload, ORDER_STATUS.PAYMENT_CANCELED);
            break;
         default:
            log.warn("Unhandled event type: " + eventType);
            break;
      }

      return ResponseEntity.ok("Webhook received successfully");
   }

   private void updateOrderStatus(String payload, ORDER_STATUS status) {
      log.info("Handling event with status: " + status);

      try {
         Map<String, Object> eventMap = objectMapper.readValue(payload,
                                                               new TypeReference<>() {
                                                               });
         Map<String, Object> dataMap = (Map<String, Object>) eventMap.get("data");
         Map<String, Object> objectMap = (Map<String, Object>) dataMap.get("object");

         log.info("Session map: " + objectMap);

         String clientReferenceId = (String) objectMap.get("client_reference_id");
         Long orderId = Long.valueOf(clientReferenceId);

         Order order = orderRepository.findById(orderId).orElse(null);
         log.info("Order: " + order);

         if (order != null) {
            order.setOrderStatus(status);
            orderRepository.save(order);
            log.info("Order status updated to: " + order.getOrderStatus());
         } else {
            log.warn("Order not found for ID: " + orderId);
         }
      } catch (Exception e) {
         log.error("Error handling event with status " + status + ": " + e.getMessage(),
                   e);
      }
   }

   private String extractClientReferenceId(String payload) {
      try {
         Map<String, Object> eventMap = objectMapper.readValue(payload,
                                                               new TypeReference<>() {
                                                               });
         Map<String, Object> dataMap = (Map<String, Object>) eventMap.get("data");
         Map<String, Object> objectMap = (Map<String, Object>) dataMap.get("object");
         return (String) objectMap.get("client_reference_id");
      } catch (Exception e) {
         log.error("Failed to extract clientReferenceId: " + e.getMessage(), e);
         return null;
      }
   }
}