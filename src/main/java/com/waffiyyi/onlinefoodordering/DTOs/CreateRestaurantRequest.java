package com.waffiyyi.onlinefoodordering.DTOs;

import com.waffiyyi.onlinefoodordering.model.Address;
import com.waffiyyi.onlinefoodordering.model.ContactInformation;
import lombok.Data;

import java.util.List;

@Data
public class CreateRestaurantRequest {
   private Long id;
   private String restaurantName;
   private String description;
   private String cuisineType;
   private Address address;
   private ContactInformation contactInformation;
   private String openingHours;
   private List<String> images;
}
