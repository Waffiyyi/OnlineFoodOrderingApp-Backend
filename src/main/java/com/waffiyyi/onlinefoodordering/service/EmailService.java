package com.waffiyyi.onlinefoodordering.service;


import com.waffiyyi.onlinefoodordering.DTOs.EmailDetails;

public interface EmailService {
  void sendEmail(EmailDetails emailDTO);

  void sendEmailInternal(EmailDetails emailDTO);


}
