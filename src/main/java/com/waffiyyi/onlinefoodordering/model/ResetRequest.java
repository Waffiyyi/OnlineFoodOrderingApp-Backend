package com.waffiyyi.onlinefoodordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
public class ResetRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String resetToken;
  private LocalDateTime resetTokenExpiry;
  @ManyToOne
  @JsonIgnore
  private User user;
}