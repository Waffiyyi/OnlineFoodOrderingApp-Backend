package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.Category;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import com.waffiyyi.onlinefoodordering.repository.CategoryRepository;
import com.waffiyyi.onlinefoodordering.service.CategoryService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
  private final RestaurantService restaurantService;
  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(String name, Long userId) {
    Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
    if (name == null || name.isEmpty()) {
      throw new BadRequestException("Category name cannot be null",
                                    HttpStatus.BAD_REQUEST);
    }
    log.info("name->>>>>>>" + name);


    Category category = new Category();
    category.setName(name);
    category.setRestaurant(restaurant);
    return categoryRepository.save(category);
  }

  @Override
  public List<Category> findCategoryByRestaurantId(Long id) {
    Restaurant restaurant = restaurantService.findRestaurantById(id);
    return categoryRepository.findByRestaurantId(restaurant.getId());
  }

  @Override
  public Category findCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(
       () -> new ResourceNotFoundException("Category not found", HttpStatus.NOT_FOUND));
  }
}
