package com.waffiyyi.onlinefoodordering.service.serviceImpl;

import com.waffiyyi.onlinefoodordering.DTOs.EmailDetails;
import com.waffiyyi.onlinefoodordering.DTOs.ResetPassDTO;
import com.waffiyyi.onlinefoodordering.config.JwtProvider;
import com.waffiyyi.onlinefoodordering.exception.BadRequestException;
import com.waffiyyi.onlinefoodordering.exception.UserNotFoundException;
import com.waffiyyi.onlinefoodordering.model.ResetRequest;
import com.waffiyyi.onlinefoodordering.model.User;
import com.waffiyyi.onlinefoodordering.repository.ResetPasswordRepository;
import com.waffiyyi.onlinefoodordering.repository.RestaurantRepository;
import com.waffiyyi.onlinefoodordering.repository.UserRepository;
import com.waffiyyi.onlinefoodordering.service.EmailService;
import com.waffiyyi.onlinefoodordering.service.RestaurantService;
import com.waffiyyi.onlinefoodordering.service.UserService;
import com.waffiyyi.onlinefoodordering.utils.MailMessages;
import com.waffiyyi.onlinefoodordering.utils.OTPUtility;
import com.waffiyyi.onlinefoodordering.validations.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
   private final JwtProvider jwtProvider;
   private final EmailService emailService;
   private final ResetPasswordRepository resetPasswordRepo;
   private final PasswordEncoder passwordEncoder;
   private final RestaurantRepository restaurantRepository;

   @Override
   public User findUserByJWTToken(String jwt) {
      String email = jwtProvider.getEmailFromJwtToken(jwt);
      User user = findUserByEmail(email);
      return user;
   }

   @Override
   public Map<String, Object> getUserDetails(String jwt) {
      User user = findUserByJWTToken(jwt);
      boolean hasRestaurant = restaurantRepository.findByOwnerId(user.getId()) != null;
      Map<String, Object> response = new HashMap<>();
      response.put("user", user);
      response.put("hasRestaurant", hasRestaurant);
      return response;
   }

   @Override
   public User findUserByEmail(String email) {
      User user = userRepository.findByEmail(email);

      if (user == null) {
         throw new UserNotFoundException("User not found", HttpStatus.NOT_FOUND);
      }
      return user;
   }

   @Override
   @Transactional
   public void initiateResetRequest(String email, String resetType) throws
                                                                    NoSuchAlgorithmException {
      User user = findUserByEmail(email);

      ResetRequest request = new ResetRequest();
      final String code = OTPUtility.getRandomCodeString();
      String hash = OTPUtility.generateHash(code);
      LocalDateTime resetTokenExpiry = LocalDateTime.now().plusMinutes(10);
      request.setResetToken(hash);
      request.setResetTokenExpiry(resetTokenExpiry);
      request.setUser(user);
      resetPasswordRepo.save(request);

      String resetMessage = MailMessages.resetPasswordEmail(code);
      EmailDetails emailDTO = EmailDetails.builder()
                                          .subject("Reset Request")
                                          .recipient(user.getEmail())
                                          .messageBody(resetMessage)
                                          .build();
      try {
         emailService.sendEmail(emailDTO);
      } catch (Exception e) {
         log.error("Failed to send email", e);
         throw new BadRequestException("Request failed, try again",
                                       HttpStatus.BAD_REQUEST);
      }
   }

   public String resetPassword(ResetPassDTO resetPassDTO) throws
                                                          NoSuchAlgorithmException {
      String hash = OTPUtility.generateHash(resetPassDTO.getResetToken());

      Optional<ResetRequest> resetRequestOptional = resetPasswordRepo.findByResetToken(
        hash);
      if (resetRequestOptional.isPresent()) {
         ResetRequest resetRequest = resetRequestOptional.get();
         if (resetRequest.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token expired", HttpStatus.BAD_REQUEST);
         }

         User user = findUserByEmail(resetRequest.getUser().getEmail());


         if (!resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match",
                                          HttpStatus.BAD_REQUEST);
         }
         if (!PasswordValidator.isValid(resetPassDTO.getNewPassword())) {
            throw new BadRequestException(
              "Invalid password format, password must include at " +
                "least one uppercase letter, one lowercase letter, and one digit ",
              HttpStatus.BAD_REQUEST);
         }
         user.setPassword(passwordEncoder.encode(resetPassDTO.getNewPassword()));
         userRepository.save(user);

         resetRequest.setResetToken(null);
         resetRequest.setResetTokenExpiry(null);
         resetPasswordRepo.save(resetRequest);

         return "Your password successfully updated.";

      } else {
         throw new BadRequestException("Invalid OTP", HttpStatus.BAD_REQUEST);
      }
   }


}
