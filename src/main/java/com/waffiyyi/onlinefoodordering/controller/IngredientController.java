package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.IngredientCategoryRequest;
import com.waffiyyi.onlinefoodordering.DTOs.IngredientRequest;
import com.waffiyyi.onlinefoodordering.model.IngredientCategory;
import com.waffiyyi.onlinefoodordering.model.IngredientsItem;
import com.waffiyyi.onlinefoodordering.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;
    @PostMapping("/create-category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientCategoryRequest req){

        IngredientCategory item = ingredientService.createIngredientCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PostMapping("/create-item")
    public ResponseEntity<IngredientsItem> createIngredientItem(@RequestBody IngredientRequest req){
        IngredientsItem item = ingredientService.createIngredientItem(req.getRestaurantId(), req.getName(), req.getCategoryId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping("/update-stock")
    public ResponseEntity<IngredientsItem> updateIngredientStock(@RequestParam Long ingredientId){
        IngredientsItem item = ingredientService.updateStock(ingredientId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredient(@RequestParam Long ingredientId){
        List<IngredientsItem> items = ingredientService.findRestaurantIngredients(ingredientId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/restaurant-category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(@RequestParam Long restaurantId){
        List<IngredientCategory> items = ingredientService.findIngredientCategoryByRestaurantId(restaurantId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
