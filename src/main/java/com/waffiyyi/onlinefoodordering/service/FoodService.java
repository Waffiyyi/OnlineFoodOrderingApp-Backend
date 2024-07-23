package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.CreateFoodRequest;
import com.waffiyyi.onlinefoodordering.model.Category;
import com.waffiyyi.onlinefoodordering.model.Food;
import com.waffiyyi.onlinefoodordering.model.Restaurant;

import java.util.List;

public interface FoodService {
     Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

     void deleteFood(Long foodId);

    List<Food> getRestaurantFood(Long restaurantId, boolean isVegetarian, boolean isNonVeg, boolean isSeasonal, String foodCategory);

    List<Food> searchFood(String keyword);

    Food findFoodById(Long foodId);

    Food updateAvailability(Long foodId);



}
