package com.thetylermckay.backend.services;

import java.io.File;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender emailSender;

  /**
   * Send a simple message with no html.
   * @param to Email recipient
   * @param subject email subject
   * @param body Email body
   */
  public void sendSimpleMessage(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject(subject);
    message.setText(body);
    message.setTo(to);
    emailSender.send(message);
  }

  /**
   * Send a simple message with html.
   * @param to Email recipient
   * @param subject email subject
   * @param body Email body
   */
  public void sendHtmlMessage(String to, String subject, String body) throws Exception {       
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body, true);

    emailSender.send(message);
  }

  /**
   * Send email with an attachment.
   * @param to The recipient
   * @param subject Email subject
   * @param body Email body
   * @param pathToAttachment File path to the attachment
   * @param attachmentName Name of the attachment
   * @throws Exception File processing exception
   */
  public void sendMessageWithAttachment(String to, String subject, String body,
      String pathToAttachment, String attachmentName) throws Exception {       
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body);

    FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
    helper.addAttachment(attachmentName, file);

    emailSender.send(message);
  }

}