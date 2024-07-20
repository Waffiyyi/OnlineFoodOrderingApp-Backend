package com.waffiyyi.onlinefoodordering.respose;

import com.waffiyyi.onlinefoodordering.enums.USER_ROLE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String jwt;
    private String message;

    private USER_ROLE role;
}
