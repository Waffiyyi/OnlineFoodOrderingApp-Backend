package com.waffiyyi.onlinefoodordering.DTOs;

import lombok.Data;

@Data
public class ResetPassDTO {
  private String resetToken;
  private String newPassword;
  private String confirmPassword;
}
