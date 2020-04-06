package com.thetylermckay.backend.services;

import com.thetylermckay.backend.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

  @Autowired
  private JwtProperties jwtProperties;

  @Autowired
  private UserService userService;

  /**
   * Log a successful login attempt.
   * @param e The successful login event
   */
  public void loginSucceeded(AuthenticationSuccessEvent e) {
    org.springframework.security.core.userdetails.User u =
        ((org.springframework.security.core.userdetails.User)
            e.getAuthentication().getPrincipal());
    String username = u.getUsername();

    // For some reason this event triggers twice, once with the actual
    // username and another time with the client id.
    // TODO: Investigate a permanent fix
    if (!username.equals(jwtProperties.getClientId())) {
      userService.loginSucceeded(username, e);
    }
  }

  /**
   * Log an unsuccessful login attempt.
   * @param e The unsuccessful login event
   */
  public void loginFailed(AuthenticationFailureBadCredentialsEvent e) {
    String username = (String) e.getAuthentication().getPrincipal();
    userService.loginFailed(username);
  }
}
