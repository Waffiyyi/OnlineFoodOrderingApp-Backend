package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.CreateRestaurantRequest;
import com.waffiyyi.onlinefoodordering.DTOs.RestaurantDTO;
import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.Address;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.repository.AddressRepository;
import com.waffiyyi.onlinefoodordering.repository.RestaurantRepository;
import com.waffiyyi.onlinefoodordering.repository.UserRepository;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    restaurant.setRestaurantName(req.getRestaurantName());
    restaurant.setOpeningHours(req.getOpeningHours());
    restaurant.setRegistrationDate(LocalDateTime.now());
    restaurant.setOpen(true);
    restaurant.setOwner(user);
    return restaurantRepository.save(restaurant);
  }

  @Override
  public Restaurant updateRestaurant(Long restaurantId,
                                     CreateRestaurantRequest updateRestaurant) {
    Restaurant restaurant = findRestaurantById(restaurantId);

    if (updateRestaurant.getAddress() != null) {
      restaurant.setAddress(updateRestaurant.getAddress());
    }

    if (updateRestaurant.getContactInformation() != null) {
      restaurant.setContactInformation(updateRestaurant.getContactInformation());
    }

    if (updateRestaurant.getCuisineType() != null) {
      restaurant.setCuisineType(updateRestaurant.getCuisineType());
    }

    if (updateRestaurant.getDescription() != null) {
      restaurant.setDescription(updateRestaurant.getDescription());
    }

    if (updateRestaurant.getImages() != null) {
      restaurant.setImages(updateRestaurant.getImages());
    }

    if (updateRestaurant.getRestaurantName() != null) {
      restaurant.setRestaurantName(updateRestaurant.getRestaurantName());
    }

    if (updateRestaurant.getOpeningHours() != null) {
      restaurant.setOpeningHours(updateRestaurant.getOpeningHours());
    }

    return restaurantRepository.save(restaurant);
  }

  @Override
  public void deleteRestaurant(Long restaurantId) {
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
  public Restaurant findRestaurantById(Long id) {
    return restaurantRepository.findById(id).orElseThrow(
       () -> new ResourceNotFoundException("Restaurant not found with id " + id,
                                           HttpStatus.NOT_FOUND));
  }

  @Override
  public Restaurant getRestaurantByUserId(Long userId) {
    Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
    if (restaurant == null) {
      throw new BadRequestException(
         "User with ID" + userId + " does not have a restaurant", HttpStatus.BAD_REQUEST);
    }
    return restaurant;
  }

  @Override
  public RestaurantDTO addFavourites(Long restaurantId, User user) {
    Restaurant restaurant = findRestaurantById(restaurantId);

    RestaurantDTO restaurantDTO = new RestaurantDTO();
    restaurantDTO.setDescription(restaurant.getDescription());
    restaurantDTO.setImages(restaurant.getImages());
    restaurantDTO.setRestaurantName(restaurant.getRestaurantName());
    restaurantDTO.setId(restaurant.getId());
    restaurantDTO.setOpen(restaurant.isOpen());

    log.info(restaurant.isOpen() + "tetetettetetettete");

    boolean isFavorited = false;

    List<RestaurantDTO> favorites = user.getFavourites();
    for (RestaurantDTO favorite : favorites) {
      if (favorite.getId().equals(restaurantId)) {
        isFavorited = true;
        break;
      }
    }

    if (isFavorited) {
      favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
    } else {
      favorites.add(restaurantDTO);
    }

    userRepository.save(user);

    return restaurantDTO;
  }

  @Override
  public Restaurant updateRestaurantStatus(Long id) {
    Restaurant restaurant = findRestaurantById(id);
    restaurant.setOpen(!restaurant.isOpen());
    return restaurantRepository.save(restaurant);
  }
}
