package com.waffiyyi.onlinefoodordering.DTOs;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long cartItemId;
    private int quantity;
}
