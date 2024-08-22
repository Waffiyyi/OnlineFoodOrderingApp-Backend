package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.CreateFoodRequest;
import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.*;
import com.waffiyyi.onlinefoodordering.repository.*;
import com.waffiyyi.onlinefoodordering.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final IngredientItemRepository ingredientItemRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
        Food food = new Food();
//        if(restaurant == null){
//            throw new BadRequestException("Please input food category", HttpStatus.BAD_REQUEST);
//        }
//        if(category == null){
//            throw new BadRequestException("Please input food category", HttpStatus.BAD_REQUEST);
//        }
      Category category1 = categoryRepository.findById(category.getId()).orElseThrow(()-> new ResourceNotFoundException("Category not found", HttpStatus.NOT_FOUND));

        food.setFoodCategory(category1);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setCreationDate(new Date());

        List<IngredientsItem> ingredientItems = req.getIngredientItems();

        if (ingredientItems == null || ingredientItems.isEmpty()) {
            throw new BadRequestException("Please add the food's ingredient items", HttpStatus.BAD_REQUEST);
        }

        for (IngredientsItem ingredientItem : ingredientItems) {
            if (ingredientItem.getCategory() == null || !ingredientCategoryRepository.existsById(ingredientItem.getCategory().getId())) {
                throw new BadRequestException("Ingredient category with id" + ingredientItem.getCategory().getId() +" does not exist", HttpStatus.BAD_REQUEST);
            }
            if (ingredientItem.getId() == null || !ingredientItemRepository.existsById(ingredientItem.getId())) {
                throw new BadRequestException("Ingredient item with ID " + ingredientItem.getId() + " does not exist", HttpStatus.BAD_REQUEST);
            }
        }
        food.setIngredientItems(req.getIngredientItems());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) {
         Food food = findFoodById(foodId);
         food.setRestaurant(null);
         foodRepository.save(food);
    }

    @Override
    public List<Food> getRestaurantFood(Long restaurantId,
                                        boolean isVegetarian,
                                        boolean isNonVeg,
                                        boolean isSeasonal,
                                        String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        if(isVegetarian){
            foods= filterByVegetarian(foods, isVegetarian);

        }    
        
        if(isNonVeg){
            foods=filterByNonVeg(foods, isNonVeg);
        }
        
        if(isSeasonal){
            foods=filterBySeasonal(foods, isSeasonal);
        }

        if(foodCategory !=null && !foodCategory.equals("") ){
            foods = filterByCategory(foods,foodCategory);
        }
        
        return foods;
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if(food.getFoodCategory()!=null){
                return food.getFoodCategory().getName().equals(foodCategory);
            }
             return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food -> food.isSeasonal()== isSeasonal).collect(Collectors.toList());
    }

    private List<Food> filterByNonVeg(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> food.isVegetarian()== false).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream().filter(food -> food.isVegetarian()== isVegetarian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(()-> new ResourceNotFoundException("Food not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Food updateAvailability(Long foodId) {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
