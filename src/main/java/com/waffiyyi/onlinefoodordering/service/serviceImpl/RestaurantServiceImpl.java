package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.DTOs.RestaurantDTO;
import com.waffiyyi.onlinefoodordering.model.Address;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.repository.AddressRepository;
import com.waffiyyi.onlinefoodordering.repository.RestaurantRepository;
import com.waffiyyi.onlinefoodordering.repository.UserRepository;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address = addressRepository.save(req.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updateRestaurant) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if(updateRestaurant.getAddress()!= null){
            restaurant.setAddress(updateRestaurant.getAddress());
        }

        if(updateRestaurant.getContactInformation() != null){
            restaurant.setContactInformation(updateRestaurant.getContactInformation());
        }

        if(updateRestaurant.getCuisineType() != null){
            restaurant.setCuisineType(updateRestaurant.getCuisineType());
        }

        if(updateRestaurant.getDescription() != null){
            restaurant.setDescription(updateRestaurant.getDescription());
        }

        if(updateRestaurant.getImages() != null){
            restaurant.setImages(updateRestaurant.getImages());
        }

        if(updateRestaurant.getName() != null){
            restaurant.setName(updateRestaurant.getName());
        }

        if(updateRestaurant.getOpeningHours() != null){
            restaurant.setOpeningHours(updateRestaurant.getOpeningHours());
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
       Restaurant restaurant = findRestaurantById(restaurantId);

       restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        return restaurantRepository.findById(id).orElseThrow(()-> new Exception("Restaurant not found with id "+ id));
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if(restaurant == null){
            throw new Exception("Restaurant not found with id" + userId);
        }
        return restaurant;
    }

    @Override
    public RestaurantDTO addFavourites(Long restaurantId, User user) throws Exception {
          Restaurant restaurant = findRestaurantById(restaurantId);

          RestaurantDTO restaurantDTO = new RestaurantDTO();
          restaurantDTO.setDescription(restaurant.getDescription());
          restaurantDTO.setImages(restaurant.getImages());
          restaurantDTO.setTittle(restaurant.getName());
          restaurantDTO.setId(restaurant.getId());


        return null;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        return null;
    }
}
