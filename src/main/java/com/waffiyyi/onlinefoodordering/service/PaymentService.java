package com.waffiyyi.onlinefoodordering.service;

import com.stripe.exception.StripeException;
import com.waffiyyi.onlinefoodordering.DTOs.PaymentResponse;
import com.waffiyyi.onlinefoodordering.model.Order;

public interface PaymentService {
  public PaymentResponse createPaymentLink(Order order)
      throws StripeException;
}
