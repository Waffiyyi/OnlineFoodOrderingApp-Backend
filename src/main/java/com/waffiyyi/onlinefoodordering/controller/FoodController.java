package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.model.Food;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.FoodService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {
  private final FoodService foodService;

  @GetMapping("/search-food")
  public ResponseEntity<List<Food>> searchFood(@RequestParam String keyword) {

    List<Food> foods = foodService.searchFood(keyword);
    return new ResponseEntity<>(foods, HttpStatus.OK);
  }

  @GetMapping("/get-foods")
  public ResponseEntity<List<Food>> getRestaurantFood(@RequestParam boolean vegetarian,
                                                      @RequestParam boolean seasonal,
                                                      @RequestParam boolean nonVeg,
                                                      @RequestParam(required = false)
                                                      String foodCategory,
                                                      @RequestParam Long restaurantId) {
    List<Food> foods = foodService.getRestaurantFood(restaurantId, vegetarian, nonVeg,
        seasonal, foodCategory);
    return new ResponseEntity<>(foods, HttpStatus.OK);
  }
}
