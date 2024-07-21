package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.DTOs.RestaurantDTO;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestParam String keyword
    ){
        List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Restaurant>> getAllRestaurant(
    ){
        List<Restaurant> restaurant = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Restaurant> getRestaurantById(
            @RequestParam Long restaurantId
    ) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @PutMapping("/add-favourite")
    public ResponseEntity<RestaurantDTO> addToFavourite(
            @RequestHeader("Authorization") String jwt,
            @RequestParam Long restaurantId
    ) throws Exception {
        User user = userService.findUserByJWTToken(jwt);
        RestaurantDTO restaurant = restaurantService.addFavourites(restaurantId, user);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
