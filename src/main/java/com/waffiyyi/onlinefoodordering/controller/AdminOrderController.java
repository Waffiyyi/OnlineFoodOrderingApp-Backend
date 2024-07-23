package com.waffiyyi.onlinefoodordering.controller;


import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import com.waffiyyi.onlinefoodordering.model.Order;
import com.waffiyyi.onlinefoodordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping("/order/restaurant/get-history")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) ORDER_STATUS orderStatus){
        List<Order> orders = orderService.getRestaurantsOrder(restaurantId, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/order/status")
    public ResponseEntity<Order> updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam ORDER_STATUS orderStatus){
        Order order = orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


}
