package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.FailedLoginAttempt;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.FailedLoginAttemptRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FailedLoginAttemptService {
  
  /**
   * Max page size allowed.
   */
  public final int maxPageLength = 10;

  @Autowired
  private FailedLoginAttemptRepository repository;

  /**
   * Find all failed attempts in desc order.
   * @param pageNumber Client page number
   * @param pageLength Size of Page
   * @return List of failed attempts
   */
  public List<FailedLoginAttempt> findAllFailedLoginAttempts(int pageNumber, int pageLength) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength), sort);
    return repository.findAll(page).toList();
  }

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
  
  /**
   * Get the total failed attempts in our system.
   * @return Total
   */
  public Long totalFailedLoginAttempts() {
    return repository.count();
  }
}
