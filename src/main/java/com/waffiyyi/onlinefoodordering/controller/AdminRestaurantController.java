package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurant")
@RequiredArgsConstructor
public class AdminRestaurantController {
  private final RestaurantService restaurantService;
  private final UserService userService;

  @PostMapping("/create")
  public ResponseEntity<Restaurant> createRestaurant(
      @RequestBody CreateRestaurantRequest req,
      @RequestHeader("Authorization") String jwt
  ) {

    User user = userService.findUserByJWTToken(jwt);

    Restaurant restaurant = restaurantService.createRestaurant(req, user);
    return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
  }

  @PutMapping("/update")
  public ResponseEntity<Restaurant> updateRestaurant(
      @RequestBody CreateRestaurantRequest req,
      @RequestParam Long restaurantId
  ) {
    Restaurant restaurant = restaurantService.updateRestaurant(restaurantId, req);
    return new ResponseEntity<>(restaurant, HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteRestaurant(
      @RequestParam Long restaurantId
  ) {

    restaurantService.deleteRestaurant(restaurantId);
    return new ResponseEntity<>("Successfully deleted restaurant", HttpStatus.OK);
  }

  @PutMapping("/update-status")
  public ResponseEntity<Restaurant> updateRestaurantStatus(
      @RequestParam Long restaurantId
  ) {
    Restaurant restaurant = restaurantService.updateRestaurantStatus(restaurantId);
    return new ResponseEntity<>(restaurant, HttpStatus.OK);
  }

  @GetMapping("/user")
  public ResponseEntity<Restaurant> findRestaurantByUserId(
      @RequestHeader("Authorization") String jwt
  ) {
    User user = userService.findUserByJWTToken(jwt);
    Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
    return new ResponseEntity<>(restaurant, HttpStatus.OK);
  }
}
