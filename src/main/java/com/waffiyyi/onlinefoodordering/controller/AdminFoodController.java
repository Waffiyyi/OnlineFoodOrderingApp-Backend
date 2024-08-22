package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.CreateFoodRequest;
import com.waffiyyi.onlinefoodordering.model.Food;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.FoodService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJWTToken(jwt);

        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getCategory(), restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFood(@RequestParam Long foodId) {
      foodService.deleteFood(foodId);
        return new ResponseEntity<>("Successfully deleted Food", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update-availability")
    public ResponseEntity<Food> updateFoodAvailability(@RequestParam Long foodId) {
        Food food = foodService.updateAvailability(foodId);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
}
