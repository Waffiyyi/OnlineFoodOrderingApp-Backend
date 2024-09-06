package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.OrderRequest;
import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.model.User;

import java.util.List;

public interface OrderService {
  Order createOrder(OrderRequest order, User user);

  Order updateOrder(Long orderId, ORDER_STATUS orderStatus);

  void cancelOrder(Long orderId);

  List<Order> getUsersOrder(Long userId);

  List<Order> getRestaurantsOrderByStatus(Long restaurantId, ORDER_STATUS orderStatus);

  List<Order> getRestaurantsOrder(Long restaurantId);

  Order findOrderById(Long id);
}
