package com.waffiyyi.onlinefoodordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String image;
  private String eventName;
  private String location;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  @ManyToOne
  @JsonIgnore
  private Restaurant restaurant;

}
