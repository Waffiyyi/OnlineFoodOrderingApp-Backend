package com.waffiyyi.onlinefoodordering.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long Id;

  private String name;

  private String description;

  private Long price;

  @ManyToOne
  @JsonIgnore
  private Category foodCategory;

  @Column(length = 1000)
  @ElementCollection
  private List<String> images;

  private boolean isAvailable = true;
  @ManyToOne
  private Restaurant restaurant;

  private boolean isVegetarian;

  private boolean isSeasonal;

  @ManyToMany
  private List<IngredientsItem> ingredientItems = new ArrayList<>();

  private Date creationDate;
}
