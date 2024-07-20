package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.model.User;

public interface UserService {
    User findUserByJWTToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
