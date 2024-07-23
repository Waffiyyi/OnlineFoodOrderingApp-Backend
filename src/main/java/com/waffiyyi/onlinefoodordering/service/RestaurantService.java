package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.DTOs.RestaurantDTO;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;

import java.util.List;

public interface RestaurantService {
     Restaurant createRestaurant(CreateRestaurantRequest req, User user);
     Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updateRestaurant);

     void deleteRestaurant(Long restaurantId);

     List<Restaurant> getAllRestaurant();

     List<Restaurant> searchRestaurant(String keyword);

     Restaurant findRestaurantById(Long id);

     Restaurant getRestaurantByUserId(Long userId);

     RestaurantDTO addFavourites(Long restaurantId, User user);

     Restaurant updateRestaurantStatus(Long id);





}
