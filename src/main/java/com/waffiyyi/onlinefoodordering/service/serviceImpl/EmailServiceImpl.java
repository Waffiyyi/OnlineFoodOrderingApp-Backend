package com.waffiyyi.onlinefoodordering.service.serviceImpl;


import com.waffiyyi.onlinefoodordering.DTOs.EmailDetails;
import com.waffiyyi.onlinefoodordering.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  private final RetryTemplate retryTemplate;

  private String senderName = "Crave Courier";

  @Value("${spring.mail.username}")
  private String senderEmail;

  @Override
  public void sendEmail(EmailDetails emailDetails) {
    retryTemplate.execute(context -> {
      sendEmailInternal(emailDetails);
      return null;
    });
  }

  @Override
  public void sendEmailInternal(EmailDetails emailDTO) {
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      var mailMessage = new MimeMessageHelper(message);
      mailMessage.setFrom(senderEmail, senderName);
      mailMessage.setTo(emailDTO.getRecipient());
      mailMessage.setSubject(emailDTO.getSubject());
      mailMessage.setText(emailDTO.getMessageBody(), true);
      javaMailSender.send(message);

    } catch (MessagingException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }


}
