package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
   private final UserService userService;

   @GetMapping("/profile")
   public ResponseEntity<Map<String, Object>> findUserByJwt(
     @RequestHeader("Authorization") String jwt) {
      return new ResponseEntity<>(userService.getUserDetails(jwt), HttpStatus.OK);
   }
}
