package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.model.IngredientCategory;
import com.waffiyyi.onlinefoodordering.model.IngredientsItem;

import java.util.List;

public interface IngredientService {
    IngredientCategory createIngredientCategory(String name, Long restaurantId);

    IngredientCategory findIngredientCategoryById(Long id);

    List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id);

    IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId);

    List<IngredientsItem> findRestaurantIngredients(Long restaurantId);

    IngredientsItem updateStock(Long id);
}
