package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long userId);

    List<Category> findCategoryByRestaurantId(Long id);

    Category findCategoryById(Long id);
}
