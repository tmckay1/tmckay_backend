package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.FailedLoginAttempt;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.FailedLoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FailedLoginAttemptService {

  @Autowired
  private FailedLoginAttemptRepository repository;

  /**
   * Log a failed login attempt for a user.
   * @param user The user that failed to login
   * @param ipAddress The ip address of the client that failed the login
   */
  public void logFailedAttempt(User user, String ipAddress, String userAgent) {
    FailedLoginAttempt fla = new FailedLoginAttempt();
    fla.setUser(user);
    fla.setIpAddress(ipAddress);
    fla.setUserAgent(userAgent);
    repository.save(fla);
  }
}
