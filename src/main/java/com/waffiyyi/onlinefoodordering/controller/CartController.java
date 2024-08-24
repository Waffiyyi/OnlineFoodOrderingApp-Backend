package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.AddCartItemRequest;
import com.waffiyyi.onlinefoodordering.DTOs.UpdateCartItemRequest;
import com.waffiyyi.onlinefoodordering.model.Cart;
import com.waffiyyi.onlinefoodordering.model.CartItem;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.CartService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;
  private final UserService userService;

  @PutMapping("/cart/add")
  public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest req,
                                                @RequestHeader("Authorization")
                                                String jwt) {
    CartItem cartItem = cartService.addItemToCart(req, jwt);
    return new ResponseEntity<>(cartItem, HttpStatus.OK);
  }

  @PutMapping("/cart-item/update")
  public ResponseEntity<CartItem> updateCartItem(@RequestBody UpdateCartItemRequest req) {
    CartItem cartItem = cartService.updateCartItemQuantity(req.getCartItemId(),
        req.getQuantity());
    return new ResponseEntity<>(cartItem, HttpStatus.OK);
  }

  @DeleteMapping("/cart-item/remove")
  public ResponseEntity<Cart> removeCartItem(@RequestParam Long cartItemId,
                                             @RequestHeader("Authorization") String jwt) {
    Cart cart = cartService.removeItemFromCart(cartItemId, jwt);
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @PutMapping("/cart/clear")
  public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) {
    Cart cart = cartService.clearCart(jwt);
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

  @GetMapping("/cart/user")
  public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) {
    User user = userService.findUserByJWTToken(jwt);
    Cart cart = cartService.findCartByUserId(user.getId());
    return new ResponseEntity<>(cart, HttpStatus.OK);
  }

}
