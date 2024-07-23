package com.waffiyyi.onlinefoodordering.DTOs;

import com.waffiyyi.onlinefoodordering.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
