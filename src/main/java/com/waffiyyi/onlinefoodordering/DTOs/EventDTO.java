package com.waffiyyi.onlinefoodordering.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDTO {
  private Long id;
  private Long restaurantId;
  private String image;
  private String eventName;
  private String location;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
}
