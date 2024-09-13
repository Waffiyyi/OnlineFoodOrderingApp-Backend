package com.waffiyyi.onlinefoodordering.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@RequiredArgsConstructor

public class MailMessages {
  public static String resetPasswordEmail(String code) {
    return
       "<p>Hi User, </p>" +
          "<p>You have requested to reset your password, Please use " +
          "the code below to complete this process.</p>" +
          "<p><a href=\"#\"><h4>Code: " + code + "</h4></a></p>";
  }
}
