package com.waffiyyi.onlinefoodordering.service;

import com.waffiyyi.onlinefoodordering.DTOs.ResetPassDTO;
import com.waffiyyi.onlinefoodordering.model.User;

import java.security.NoSuchAlgorithmException;

public interface UserService {
  User findUserByJWTToken(String jwt);

  User findUserByEmail(String email);

  void initiateResetRequest(String email, String resetType) throws
                                                            NoSuchAlgorithmException;

  String resetPassword(ResetPassDTO resetPassDTO) throws NoSuchAlgorithmException;
}
