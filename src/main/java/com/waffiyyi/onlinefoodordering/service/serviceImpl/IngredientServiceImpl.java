package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.IngredientCategory;
import com.waffiyyi.onlinefoodordering.model.IngredientsItem;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.repository.IngredientCategoryRepository;
import com.waffiyyi.onlinefoodordering.repository.IngredientItemRepository;
import com.waffiyyi.onlinefoodordering.service.IngredientService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientItemRepository ingredientItemRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final RestaurantService restaurantService;
    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory category = new IngredientCategory();
        category.setRestaurant(restaurant);
        category.setName(name);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) {
        return ingredientCategoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Ingredient Category not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) {
        restaurantService.findRestaurantById(id);
        return ingredientCategoryRepository.findByRestaurantId(id);
    }

    @Override
    @Transactional
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(categoryId);

        IngredientsItem ingredientsItem = new IngredientsItem();
        ingredientsItem.setName(ingredientName);
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setCategory(category);
        IngredientsItem savedIngredientItem = ingredientItemRepository.save(ingredientsItem);
        category.getIngredientsItem().add(savedIngredientItem);
        return savedIngredientItem;
    }

    @Override
    public List<IngredientsItem> findRestaurantIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) {
        IngredientsItem ingredientsItem = ingredientItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Ingredient item not found", HttpStatus.NOT_FOUND));
        ingredientsItem.setInStock(!ingredientsItem.isInStock());
        return ingredientItemRepository.save(ingredientsItem);
    }
}
