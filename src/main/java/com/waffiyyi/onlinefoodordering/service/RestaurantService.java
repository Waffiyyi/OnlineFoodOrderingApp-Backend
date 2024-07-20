package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.DTOs.RestaurantDTO;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;

import java.util.List;

public interface RestaurantService {
     Restaurant createRestaurant(CreateRestaurantRequest req, User user);
     Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updateRestaurant) throws Exception;

     void deleteRestaurant(Long restaurantId) throws Exception;

     List<Restaurant> getAllRestaurant();

     List<Restaurant> searchRestaurant(String keyword);

     Restaurant findRestaurantById(Long id) throws Exception;

     Restaurant getRestaurantByUserId(Long userId) throws Exception;

     RestaurantDTO addFavourites(Long restaurantId, User user) throws Exception;

     Restaurant updateRestaurantStatus(Long id) throws Exception;





}
