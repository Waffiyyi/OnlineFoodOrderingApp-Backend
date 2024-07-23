package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.AddCartItemRequest;
import com.waffiyyi.onlinefoodordering.model.Cart;
import com.waffiyyi.onlinefoodordering.model.CartItem;

public interface CartService {
    CartItem addItemToCart(AddCartItemRequest req, String jwt);

    CartItem updateCartItemQuantity(Long cartItemId, int quantity);

    Cart removeItemFromCart(Long cartItemId, String jwt);

    Long calculateCartTotal(Cart cart);

    Cart findCartById(Long id);

    Cart findCartByUserId(Long userId);

    Cart clearCart(String jwt);

}
