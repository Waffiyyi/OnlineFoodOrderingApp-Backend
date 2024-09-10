package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.OrderRequest;
import com.waffiyyi.onlinefoodordering.enums.ORDER_STATUS;
import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.*;
import com.waffiyyi.onlinefoodordering.repository.AddressRepository;
import com.waffiyyi.onlinefoodordering.repository.OrderItemRepository;
import com.waffiyyi.onlinefoodordering.repository.OrderRepository;
import com.waffiyyi.onlinefoodordering.repository.UserRepository;
import com.waffiyyi.onlinefoodordering.service.CartService;
import com.waffiyyi.onlinefoodordering.service.OrderService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final AddressRepository addressRepository;
  private final RestaurantService restaurantService;
  private final UserRepository userRepository;
  private final CartService cartService;

  @Override
  public Order createOrder(OrderRequest order, User user, Long addressId) {
    Address selectedAddress =
       addressRepository.findById(addressId).orElseThrow(
          () -> new ResourceNotFoundException("Address not found", HttpStatus.NOT_FOUND));


    Address deliveryAddress = new Address();

    if (order.getDeliveryAddress() != null) {
      deliveryAddress = order.getDeliveryAddress();
    }

    List<Address> existingAddresses =
       addressRepository.findAllByCityAndStateProvinceAndPostalCodeAndStreetAddress(
          deliveryAddress.getCity(),
          deliveryAddress.getStateProvince(),
          deliveryAddress.getPostalCode(),
          deliveryAddress.getStreetAddress()
       );


    Address savedAddress;
    if (selectedAddress != null) {
      savedAddress = selectedAddress;
    } else if (!existingAddresses.isEmpty()) {
      savedAddress = existingAddresses.get(0);
    } else {
      savedAddress = addressRepository.save(deliveryAddress);
    }


    if (!user.getAddresses().contains(savedAddress)) {
      user.getAddresses().add(savedAddress);
      userRepository.save(user);
    }

    Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());

    Order createdOrder = new Order();

    createdOrder.setCustomer(user);
    createdOrder.setCreatedAt(new Date());
    createdOrder.setOrderStatus(ORDER_STATUS.PENDING);
    if (selectedAddress != null) {
      createdOrder.setDeliveryAddress(selectedAddress);
    } else {
      createdOrder.setDeliveryAddress(savedAddress);
    }

    createdOrder.setRestaurant(restaurant);

    Cart cart = cartService.findCartByUserId(user.getId());

    List<OrderItem> orderItems = new ArrayList<>();

    for (CartItem cartItem : cart.getItems()) {
      OrderItem orderItem = new OrderItem();

      orderItem.setFood(cartItem.getFood());
      orderItem.setIngredients(new ArrayList<>(cartItem.getIngredients()));
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setTotalPrice(cartItem.getTotalPrice());

      OrderItem savedOrderItem = orderItemRepository.save(orderItem);
      orderItems.add(savedOrderItem);
    }
    Long totalPrice = cartService.calculateCartTotal(cart);

    createdOrder.setItems(orderItems);
    createdOrder.setTotalPrice(totalPrice);

    Order savedOrder = orderRepository.save(createdOrder);
    restaurant.getOrders().add(savedOrder);
    return createdOrder;
  }

  @Override
  public Order updateOrder(Long orderId, ORDER_STATUS orderStatus) {
    Order order = findOrderById(orderId);

    if (orderStatus.equals(ORDER_STATUS.PENDING) ||
           orderStatus.equals(ORDER_STATUS.OUT_FOR_DELIVERY) ||
           orderStatus.equals(ORDER_STATUS.DELIVERED) ||
           orderStatus.equals(ORDER_STATUS.COMPLETED)
    ) {
      order.setOrderStatus(orderStatus);
      return orderRepository.save(order);
    }
    throw new BadRequestException("Please select a valid order status",
                                  HttpStatus.BAD_REQUEST);
  }

  @Override
  public void cancelOrder(Long orderId) {
    findOrderById(orderId);
    orderRepository.deleteById(orderId);
  }

  @Override
  public List<Order> getUsersOrder(Long userId) {

    return orderRepository.findByCustomerId(userId);
  }

  @Override
  public List<Order> getRestaurantsOrderByStatus(Long restaurantId,
                                                 ORDER_STATUS orderStatus) {
    List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
    if (orderStatus != null) {
      orders = orders.stream().filter(order ->
                                         order.getOrderStatus().equals(
                                            orderStatus)).collect(Collectors.toList());
    }
    return orders;
  }

  @Override
  public List<Order> getRestaurantsOrder(Long restaurantId) {
    List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
    return orders;
  }

  @Override
  public Order findOrderById(Long id) {
    return orderRepository.findById(id).orElseThrow(
       () -> new ResourceNotFoundException("Order not found", HttpStatus.NOT_FOUND));
  }
}
