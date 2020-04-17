package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.BadCredentialsException;
import com.thetylermckay.backend.exceptions.UserUnverifiedException;
import com.thetylermckay.backend.mailers.ResetPasswordMailer;
import com.thetylermckay.backend.mailers.SignUpMailer;
import com.thetylermckay.backend.models.Privilege;
import com.thetylermckay.backend.models.Role;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class UserService implements UserDetailsService {
  
  /**
   * Max number of times a user is allowed to attempt logging in.
   */
  public final int maxLoginAttempts = 8;

  @Autowired
  private UserRepository repository;

  @Autowired
  private ResetPasswordMailer resetPasswordMailer;

  @Autowired
  private SignUpMailer signUpMailer;

  @Autowired
  private TokenService tokenService;

  /**
   * Change the password for the given user and invalidate the token.
   * @param password The new password
   * @param token The token used to reset this password
   */
  public void changePassword(String password, String token, PasswordEncoder passwordEncoder) {
    Token t = tokenService.verifyToken(token);
    User user = t.getUser();
    if (!user.getIsVerified()) {
      throw new UserUnverifiedException();
    }
    user.setPassword(passwordEncoder.encode(password));
    repository.save(user);
    tokenService.invalidateToken(t);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByEmail(username).orElseThrow(() ->
        BadCredentialsException.create());
    boolean isEnabled = user.getIsActive();
    boolean isAccountNotExpired = user.getIsVerified();
    boolean isPasswordNotExpired = true;
    boolean isAccountNotLocked = user.getFailedAttempts() < maxLoginAttempts;
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), isEnabled, isAccountNotExpired,
        isPasswordNotExpired, isAccountNotLocked, getAuthorities(user.getRoles()));
  }
  
  private Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Role> roles) {
    return getGrantedAuthorities(getPrivileges(roles));
  }

  private List<String> getPrivileges(Collection<Role> roles) {
    List<String> privileges = new ArrayList<>();
    List<Privilege> collection = new ArrayList<>();
    for (Role role : roles) {
      collection.addAll(role.getPrivileges());
    }
    for (Privilege item : collection) {
      privileges.add(item.getName());
    }
    return privileges;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
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
   * Increment the failed login attempts when a user fails to login.
   * @param username The username of the user that failed to login
   */
  public void loginFailed(String username) {
    User user = repository.findByEmail(username).get();
    user.setFailedAttempts(1 + user.getFailedAttempts());
    repository.save(user);
  }
  
  /**
   * Send a link to the user to reset the user's password.
   * @param username The username of the user to reset the password for
   */
  public void resetPassword(String username) {
    Optional<User> user = repository.findByEmail(username);
    if (user.isPresent() && user.get().getIsVerified()) {
      String token = tokenService.generateResetPasswordToken(user.get());
      resetPasswordMailer.sendResetPasswordEmail(user.get(), token);
    }
  }
  
  /**
   * Sign Up the given user if it is a valid user.
   * @param username The username of the user to signup
   */
  public void signUp(String username) {
    Optional<User> user = repository.findByEmail(username);
    if (user.isPresent() && !user.get().getIsVerified()) {
      String token = tokenService.generateSignUpToken(user.get());
      signUpMailer.sendSignUpEmail(user.get(), token);
    }
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
  
  /**
   * Increment the failed verification attempts when a user fails to verify.
   * @param user The user that failed to verify
   */
  public void verificationFailed(User user) {
    user.setFailedVerificationAttempts(1 + user.getFailedVerificationAttempts());
    repository.save(user);
  }
  
  /**
   * Mark the user verified, and reset his or her verification attempts.
   * @param user User to verify
   */
  public void verifyUser(User user) {
    user.setFailedVerificationAttempts(0);
    user.setIsVerified(true);
    repository.save(user);
  }
}
