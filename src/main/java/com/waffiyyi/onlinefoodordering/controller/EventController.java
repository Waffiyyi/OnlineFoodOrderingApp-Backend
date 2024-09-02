package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.EventDTO;
import com.waffiyyi.onlinefoodordering.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;

  @PostMapping("/create-event")
  public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
    return new ResponseEntity<>(eventService.createEvent(eventDTO), HttpStatus.CREATED);
  }


  @GetMapping("/get-restaurant-events")
  public ResponseEntity<List<EventDTO>> getAllRestaurantEvent(
     @RequestParam Long restaurantId) {
    return new ResponseEntity<>(eventService.getAllRestaurantEvent(restaurantId),
                                HttpStatus.OK);
  }

  @GetMapping("/get-all-events")
  public ResponseEntity<List<EventDTO>> getAllEvent() {
    return new ResponseEntity<>(eventService.getAllEvent(),
                                HttpStatus.OK);
  }


  @PostMapping("/delete-event")
  public ResponseEntity<EventDTO> createEvent(@RequestParam Long eventId) {
    eventService.deleteEvent(eventId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
