package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.AddCartItemRequest;
import com.waffiyyi.onlinefoodordering.exception.ResourceNotFoundException;
import com.waffiyyi.onlinefoodordering.model.Cart;
import com.waffiyyi.onlinefoodordering.model.CartItem;
import com.waffiyyi.onlinefoodordering.model.Food;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.repository.CartItemRepository;
import com.waffiyyi.onlinefoodordering.repository.CartRepository;
import com.waffiyyi.onlinefoodordering.service.CartService;
import com.waffiyyi.onlinefoodordering.service.FoodService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final FoodService foodService;
    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) {
        User user = userService.findUserByJWTToken(jwt);

        Food food = foodService.findFoodById(req.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem: cart.getItems()){
            if(cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity() + req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        cart.getItems().add(savedCartItem);




        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()-> new ResourceNotFoundException("Cart item not found", HttpStatus.NOT_FOUND));
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getFood().getPrice() * quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) {
        User user = userService.findUserByJWTToken(jwt);
        Cart cart = cartRepository.findByCustomerId(user.getId());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()-> new ResourceNotFoundException("Cart item not found", HttpStatus.NOT_FOUND));

        cart.getItems().remove(cartItem);
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) {
        Long total = 0L;

        for (CartItem cartItem: cart.getItems()){
            total+=cartItem.getFood().getPrice() + cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cart not found with id " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        Cart cart = cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateCartTotal(cart));
        return cart;
    }

    @Override
    public Cart clearCart(String jwt) {
        User user = userService.findUserByJWTToken(jwt);
        Cart cart = findCartByUserId(user.getId());
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
