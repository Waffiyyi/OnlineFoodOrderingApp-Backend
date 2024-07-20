package com.waffiyyi.onlinefoodordering.repository;

import com.waffiyyi.onlinefoodordering.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
