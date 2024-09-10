package com.waffiyyi.onlinefoodordering.controller;

import com.stripe.exception.StripeException;
import com.waffiyyi.onlinefoodordering.DTOs.AddCartItemRequest;
import com.waffiyyi.onlinefoodordering.DTOs.OrderRequest;
import com.waffiyyi.onlinefoodordering.DTOs.PaymentResponse;
import com.waffiyyi.onlinefoodordering.model.CartItem;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.OrderService;
import com.waffiyyi.onlinefoodordering.service.PaymentService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;
  private final UserService userService;
  private final PaymentService paymentService;

  @PostMapping("/order/create")
  public ResponseEntity<PaymentResponse> createOrder(@RequestBody OrderRequest req,
                                                     @RequestParam Long addressId,
                                                     @RequestHeader("Authorization")
                                                     String jwt) throws StripeException {
    User user = userService.findUserByJWTToken(jwt);
    Order order = orderService.createOrder(req, user, addressId);
    PaymentResponse res = paymentService.createPaymentLink(order);
    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @GetMapping("/order/get-history")
  public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization")
                                                     String jwt) {
    User user = userService.findUserByJWTToken(jwt);
    List<Order> orders = orderService.getUsersOrder(user.getId());
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }
}
