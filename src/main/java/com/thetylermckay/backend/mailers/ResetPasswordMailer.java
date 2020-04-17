package com.thetylermckay.backend.mailers;

import com.thetylermckay.backend.config.ServerProperties;
import com.thetylermckay.backend.exceptions.FailedMailerException;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordMailer {

  @Autowired
  private ServerProperties serverProperties;

  @Autowired
  private EmailService emailService;

  /**
   * Send a link to reset a user's password.
   * @param user Recipient
   * @param token Reset password token
   */
  public void sendResetPasswordEmail(User user, String token) {
    String host = serverProperties.getHost();
    String signUpLink = host + "/verify/" + token;
    String subject = "Reset your password for " + host;
    String body = resetPasswordMessage(user.name(), host, signUpLink);
    try {
      emailService.sendHtmlMessage(user.getEmail(), subject, body);
    } catch (Exception e) {
      throw new FailedMailerException();
    }
  }
  
  private String resetPasswordMessage(String name, String host, String resetPasswordLink) {
    return new StringBuilder()
        .append("<p>Hi ").append(name).append(",</p>")
        .append("<p>A password reset has been requested. ")
            .append("To reset your password for ").append(host)
            .append(", please click on the following link: ")
            .append("<a href='").append(resetPasswordLink).append("'>").append(resetPasswordLink).append("</a>")
        .append("</p>")
        .append("<p>Enjoy</p>")
        .toString();
  }
}
