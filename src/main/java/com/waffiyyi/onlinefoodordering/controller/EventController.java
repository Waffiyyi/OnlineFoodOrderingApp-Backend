package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.EventDTO;
import com.waffiyyi.onlinefoodordering.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor

public class EventController {
  private final EventService eventService;

  @GetMapping("/get-all")
  public ResponseEntity<List<EventDTO>> getAllEvent() {
    return new ResponseEntity<>(eventService.getAllEvent(),
                                HttpStatus.OK);
  }
}
