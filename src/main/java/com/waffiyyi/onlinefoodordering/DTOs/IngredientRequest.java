package com.waffiyyi.onlinefoodordering.DTOs;

import lombok.Data;

@Data
public class IngredientRequest {
    private String name;
    private Long categoryId;
    private Long restaurantId;
}
