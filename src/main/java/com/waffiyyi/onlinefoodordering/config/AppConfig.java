package com.waffiyyi.onlinefoodordering.config;

import com.waffiyyi.onlinefoodordering.exception.AuthenticationExceptionHandler;
import com.waffiyyi.onlinefoodordering.exception.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class AppConfig {
   private final JwtTokenValidator jwtTokenValidator;
   private final AuthenticationExceptionHandler authenticationExceptionHandler;
   private final SecurityException securityException;

   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.sessionManagement(management -> management.sessionCreationPolicy(
        SessionCreationPolicy.STATELESS)).authorizeHttpRequests(
        Authorize -> Authorize
          .requestMatchers(HttpMethod.POST,
                           "/auth/**", "/stripe/crave-courier/webhook").permitAll()
          .requestMatchers(
            "/api/admin/**").hasAnyRole("RESTAURANT_OWNER",
                                        "ADMIN").requestMatchers(
            "/api/**").authenticated().anyRequest().authenticated()).exceptionHandling(
        request -> {
           request.authenticationEntryPoint(authenticationExceptionHandler);
           request.accessDeniedHandler(securityException);
        }).addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class).csrf(
        csrf -> csrf.disable()).cors(
        cors -> cors.configurationSource(corsConfigurationSource()));
      return http.build();
   }

   private CorsConfigurationSource corsConfigurationSource() {
      return new CorsConfigurationSource() {
         @Override
         public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(
              Arrays.asList("http://localhost:5173", "http://localhost:3000",
                            "https://crave-courier.vercel.app"));
            cfg.setAllowedMethods(Collections.singletonList("*"));
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            cfg.setExposedHeaders(Arrays.asList("Authorization"));
            cfg.setMaxAge(3600L);
            return cfg;
         }
      };
   }

   @Bean
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
