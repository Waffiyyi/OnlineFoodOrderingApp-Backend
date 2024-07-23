package com.waffiyyi.onlinefoodordering.DTOs;

import com.waffiyyi.onlinefoodordering.model.Category;
import com.waffiyyi.onlinefoodordering.model.IngredientsItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {
    private String name;
    private String description;
    private Long price;
    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;

    private List<IngredientsItem> ingredientItems;
}
