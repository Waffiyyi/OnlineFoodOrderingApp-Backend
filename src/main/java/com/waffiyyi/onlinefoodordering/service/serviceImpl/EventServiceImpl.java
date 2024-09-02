package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.EventDTO;
import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.Event;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.repository.EventRepository;
import com.waffiyyi.onlinefoodordering.repository.RestaurantRepository;
import com.waffiyyi.onlinefoodordering.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
  private final EventRepository eventRepository;
  private final RestaurantRepository restaurantRepository;

  @Override
  public EventDTO createEvent(EventDTO createEventDTO) {
    if (createEventDTO.getEventName() == null || createEventDTO.getLocation() == null
           || createEventDTO.getStartDateTime() == null || createEventDTO.getEndDateTime() == null) {
      throw new BadRequestException("One or more required field is null",
                                    HttpStatus.BAD_REQUEST);
    }
    if (createEventDTO.getStartDateTime().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("Start date time cannot be in the past",
                                    HttpStatus.BAD_REQUEST);
    }

    restaurantRepository.findById(createEventDTO.getRestaurantId()).orElseThrow(
       () -> new ResourceNotFoundException("Restaurant not found",
                                           HttpStatus.BAD_REQUEST));

    Event event = Event.builder()
                     .eventName(createEventDTO.getEventName())
                     .location(createEventDTO.getLocation())
                     .image(createEventDTO.getImage())
                     .startDateTime(createEventDTO.getStartDateTime())
                     .endDateTime(createEventDTO.getEndDateTime())
                     .build();
    Event savedEvent = eventRepository.save(event);
    return convertToEventResponse(savedEvent);
  }

  @Override
  public List<EventDTO> getAllRestaurantEvent(Long restaurantId) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
       () -> new ResourceNotFoundException("Restaurant not found",
                                           HttpStatus.BAD_REQUEST));
    List<Event> validEvents = eventRepository.findAllByRestaurant(restaurant);
    List<EventDTO> events = new ArrayList<>();
    for (Event event : validEvents) {
      events.add(convertToEventResponse(event));
    }
    return events;
  }

  @Override
  public List<EventDTO> getAllEvent() {
    List<Event> validEvents = eventRepository.findAll();
    List<EventDTO> events = new ArrayList<>();
    for (Event event : validEvents) {
      events.add(convertToEventResponse(event));
    }
    return events;
  }

  @Override
  public void deleteEvent(Long eventId) {
    Event validEvent =
       eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException(
          "Event Not found", HttpStatus.NOT_FOUND));
    eventRepository.delete(validEvent);
  }

  private EventDTO convertToEventResponse(Event event) {
    return EventDTO.builder()
              .id(event.getId())
              .eventName(event.getEventName())
              .image(event.getImage())
              .location(event.getLocation())
              .startDateTime(event.getStartDateTime())
              .endDateTime(event.getEndDateTime())
              .build();
  }
}
