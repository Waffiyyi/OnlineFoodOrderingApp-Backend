package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.model.User;

public interface UserService {
    User findUserByJWTToken(String jwt);

    User findUserByEmail(String email);
}
