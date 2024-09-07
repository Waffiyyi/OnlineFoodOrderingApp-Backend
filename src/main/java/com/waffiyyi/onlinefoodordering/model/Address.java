package com.waffiyyi.onlinefoodordering.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long Id;

  private String streetAddress;

  private String city;

  private String stateProvince;

  private String postalCode;

  private String country;

  private String place;
}
