package com.waffiyyi.onlinefoodordering.controller;

import com.waffiyyi.onlinefoodordering.DTOs.LoginRequestDTO;
import com.waffiyyi.onlinefoodordering.config.JwtProvider;
import com.waffiyyi.onlinefoodordering.enums.USER_ROLE;
import com.waffiyyi.onlinefoodordering.model.Cart;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.repository.CartRepository;
import com.waffiyyi.onlinefoodordering.repository.UserRepository;
import com.waffiyyi.onlinefoodordering.respose.AuthResponse;
import com.waffiyyi.onlinefoodordering.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist != null){
            throw new Exception("Email is already used with another account");
        }

        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt  = jwtProvider.generateToken(authentication);

        AuthResponse response = AuthResponse.builder()
                .jwt(jwt)
                .message("Register success")
                .role(savedUser.getRole())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequestDTO req){
        String username = req.getUsername();
        String password = req.getPassword();
        
        Authentication authentication = authenticate(username, password);

        String jwt  = jwtProvider.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        AuthResponse response = AuthResponse.builder()
                .jwt(jwt)
                .message("Login success")
               .role(USER_ROLE.valueOf(role))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid username....");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password....");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
