package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.waffiyyi.onlinefoodordering.DTOs.PaymentResponse;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Value("${stripe.secret.key}")
  private String stripeSecretKey;
  @Value("${app.redirect.success}")
  private String successURL;

  @Value("${app.redirect.fail}")
  private String cancelURL;

  @Override
  public PaymentResponse createPaymentLink(Order order)
     throws StripeException {
    Stripe.apiKey = stripeSecretKey;

    SessionCreateParams params =
       SessionCreateParams.builder()
          .addPaymentMethodType(
             SessionCreateParams.PaymentMethodType.CARD)
          .setMode(
             SessionCreateParams.Mode.PAYMENT)
          .setSuccessUrl(successURL + order.getId())
          .setCancelUrl(cancelURL)
          .addLineItem(
             SessionCreateParams.LineItem.builder().setQuantity(
                1L).setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                   .setCurrency(
                      "ngn")
                   .setUnitAmount(
                      (long) order.getTotalPrice() * 100)
                   .setProductData(
                      SessionCreateParams.LineItem.PriceData.ProductData.builder()
                         .setName(
                            "Demure Deliveries")
                         .build()
                   ).build()
             ).build()
          ).build();

    Session session = Session.create(params);

    PaymentResponse response = new PaymentResponse();
    response.setPayment_url(session.getUrl());

    return response;
  }
}
