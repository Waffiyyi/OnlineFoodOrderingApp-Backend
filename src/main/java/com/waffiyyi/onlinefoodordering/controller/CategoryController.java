package com.waffiyyi.onlinefoodordering.controller;


import com.waffiyyi.onlinefoodordering.model.Category;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.CategoryService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private final UserService userService;

  @PostMapping("/admin/category/create")
  public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                 @RequestHeader("Authorization")
                                                 String jwt) {

    User user = userService.findUserByJWTToken(jwt);

    Category createCategory = categoryService.createCategory(category.getName(),
        user.getId());

    return new ResponseEntity<>(createCategory, HttpStatus.CREATED);

  }

  @GetMapping("/category/restaurant")
  public ResponseEntity<List<Category>> getRestaurantCategory(
      @RequestHeader("Authorization") String jwt) {

    User user = userService.findUserByJWTToken(jwt);

    List<Category> categories = categoryService.findCategoryByRestaurantId(user.getId());

    return new ResponseEntity<>(categories, HttpStatus.OK);

  }
}
