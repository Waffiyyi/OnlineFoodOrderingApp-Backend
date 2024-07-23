package com.waffiyyi.onlinefoodordering.DTOs;

import lombok.Data;

@Data
public class IngredientCategoryRequest {
    private String name;
    private Long restaurantId;
}
