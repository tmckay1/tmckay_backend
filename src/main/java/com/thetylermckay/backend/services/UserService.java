package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.UserRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
  
  /**
   * Max number of times a user is allowed to attempt logging in.
   */
  public final int maxLoginAttempts = 8;

  @Autowired
  private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByEmail(username).orElseThrow(() ->
        new RuntimeException("User not found: " + username));
    boolean isEnabled = user.getIsActive();
    boolean isAccountNotExpired = user.getIsVerified();
    boolean isPasswordNotExpired = true;
    boolean isAccountNotLocked = user.getFailedAttempts() < maxLoginAttempts;
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), isEnabled, isAccountNotExpired,
        isPasswordNotExpired, isAccountNotLocked, Arrays.asList(authority));
  }
  
  /**
   * When a successful login occurs, reset the failed attempts and set the IP address.
   * @param username The username of the user who logged in
   * @param e The successful authentication event
   */
  public void loginSucceeded(String username, AuthenticationSuccessEvent e) {
    User user = repository.findByEmail(username).get();
    user.setFailedAttempts(0);
    this.setLoginIp(user, e);
    repository.save(user);
  }
  
  /**
   * Increment the failed attempts when a user fails to login.
   * @param username The username of the user that failed to login
   */
  public void loginFailed(String username) {
    User user = repository.findByEmail(username).get();
    user.setFailedAttempts(1 + user.getFailedAttempts());
    repository.save(user);
  }

  /**
   * Set the last successful login ip for this user if we can get the IP.
   * @param user The user to update the login ip for
   * @param e The authentication event that has more info about the login
   */
  private void setLoginIp(User user, AuthenticationSuccessEvent e) {
    if (e.getAuthentication().getDetails() instanceof WebAuthenticationDetails) {
      WebAuthenticationDetails details = (WebAuthenticationDetails)
          e.getAuthentication().getDetails();
      user.setLastLoginIp(details.getRemoteAddress());
    }
  }
}
