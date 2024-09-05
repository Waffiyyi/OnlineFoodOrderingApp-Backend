package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.EventDTO;

import java.util.List;

public interface EventService {
  EventDTO createEvent(EventDTO createEventDTO, Long restaurantId);

  List<EventDTO> getAllRestaurantEvent(Long restaurantId);

  List<EventDTO> getAllEvent();

  void deleteEvent(Long eventId);
}
