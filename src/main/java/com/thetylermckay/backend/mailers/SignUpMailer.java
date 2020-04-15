package com.thetylermckay.backend.mailers;

import com.thetylermckay.backend.config.ServerProperties;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignUpMailer {

  @Autowired
  private ServerProperties serverProperties;

  @Autowired
  private EmailService emailService;

  /**
   * Send a link to reset a user's password.
   * @param user Recipient
   * @param token Reset password token
   */
  public void sendSignUpEmail(User user, String token) {
    String host = serverProperties.getHost();
    String signUpLink = host + "/verify/" + token;
    String subject = "Finish signing up for " + host;
    String body = signUpMessage(user.name(), host, signUpLink);
    try {
      emailService.sendHtmlMessage(user.getEmail(), subject, body);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private String signUpMessage(String name, String host, String signUpLink) {
    return new StringBuilder()
        .append("<p>Hi ").append(name).append(",</p>")
        .append("<p>To verify your account and gain access to ").append(host)
            .append(", please click on the following link: ")
            .append("<a href='").append(signUpLink).append("'>").append(signUpLink).append("</a>")
        .append("</p>")
        .append("<p>Enjoy</p>")
        .toString();
  }
}
