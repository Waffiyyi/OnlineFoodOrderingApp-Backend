package com.waffiyyi.onlinefoodordering.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor
@Service
public class OTPUtility {
  private static final SecureRandom secureRandom = new SecureRandom();

  public static String getRandomCodeString() {
    var number = secureRandom.nextInt(1000, 9999);
    return String.format("%06d", number);
  }

  public static String generateHash(String secureData) throws NoSuchAlgorithmException {
    var digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(secureData.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encodedHash);
  }
}
