package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.BadCredentialsException;
import com.thetylermckay.backend.exceptions.CannotModifySelfException;
import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.exceptions.UserUnverifiedException;
import com.thetylermckay.backend.mailers.ResetPasswordMailer;
import com.thetylermckay.backend.mailers.SignUpMailer;
import com.thetylermckay.backend.models.Privilege;
import com.thetylermckay.backend.models.Role;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements UserDetailsService {
  
  /**
   * Max number of times a user is allowed to attempt logging in.
   */
  public final int maxLoginAttempts = 8;
  
  /**
   * Max page size allowed.
   */
  public final int maxPageLength = 10;
  
  /**
   * The default password for newly created users.
   */
  public final String defaultPassword = "ILOVEANGE!";

  @Autowired
  private FailedLoginAttemptService failedLoginService;

  @Autowired
  private UserRepository repository;

  @Autowired
  private ResetPasswordMailer resetPasswordMailer;

  @Autowired
  private RoleService roleService;

  @Autowired
  private SignUpMailer signUpMailer;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserVerificationService verificationService;

  /**
   * Change the password for the given user and invalidate the token.
   * @param password The new password
   * @param token The token used to reset this password
   * @param passwordEncoder PasswordEncoder used to hash password
   */
  public void changePassword(String password, String token, PasswordEncoder passwordEncoder) {
    Token t = tokenService.verifyToken(token);
    User user = t.getUser();
    if (!user.getIsVerified()) {
      throw new UserUnverifiedException();
    }
    log.info("Changing user password for {}", user.getEmail());
    user.setPassword(passwordEncoder.encode(password));
    repository.save(user);
    tokenService.invalidateToken(t);
  }
  
  /**
   * Create the given user with the following attributes.
   * @param passwordEncoder PasswordEncoder used to hash password
   * @param firstName First name of user
   * @param lastName Last name of user
   * @param email Email of user
   * @param isActive Whether the user is active or not
   * @param roleIds The ids of the roles for this user
   * @param userVerification1 The verification attributes for the first verification
   * @param userVerification2 The verification attributes for the second verification
   * @param userVerification3 The verification attributes for the third verification
   * @param imagePath Name of the profile image to use if present
   */
  @Transactional
  public void createUser(PasswordEncoder passwordEncoder, String firstName, String lastName,
      String email, boolean isActive, List<Long> roleIds, Map<String, String> userVerification1,
      Map<String, String> userVerification2, Map<String, String> userVerification3,
      String imagePath) {
    List<Role> roles = roleService.findAllRolesByIds(roleIds);
    User u = new User();
    u.setFirstName(firstName);
    u.setLastName(lastName);
    u.setEmail(email);
    u.setIsActive(isActive);
    u.setRoles(roles);
    u.setFailedAttempts(0);
    u.setFailedVerificationAttempts(0);
    u.setIsVerified(false);
    u.setPassword(passwordEncoder.encode(defaultPassword));
    if (imagePath != null) {
      u.setImagePath(imagePath);
    }
    
    verificationService.createVerifications(u, userVerification1,
        userVerification2, userVerification3);

    repository.save(u);
  }
  
  /**
   * Delete the user with the given id.
   * @param id The id of the user to delete
   */
  public void deleteUser(Long id, HttpServletRequest request) {
    Optional<User> user = repository.findById(id);
    if (!user.isPresent()) {
      throw new EntityNotFoundException();
    }

    Optional<User> currentUser = getUserFromRequest(request);
    if (!currentUser.isPresent() || currentUser.get().getId() == id) {
      throw new CannotModifySelfException();
    }
    log.info("User {} deleting user {}", currentUser.get().getEmail(), user.get().getEmail());
    repository.delete(user.get());
  }

  public List<User> findAllUsers(int pageNumber, int pageLength) {
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength));
    return repository.findAll(page).toList();
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
   * Get the user object from the request.
   * @param request The current request
   * @return User if it exists
   */
  public Optional<User> getUserFromRequest(HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    String email = principal.getName();
    return repository.findByEmail(email);
  }
  
  /**
   * Get the user object from the request.
   * @param authentication The user authentication object
   * @return User if it exists
   */
  public Optional<User> getUserFromAuthentication(OAuth2Authentication  authentication) {
    String email = authentication.getName();
    return repository.findByEmail(email);
  }
  
  /**
   * When a successful login occurs, reset the failed attempts and set the IP address.
   * @param username The username of the user who logged in
   * @param ipAddress The client ip
   */
  public void loginSucceeded(String username, String ipAddress) {
    User user = repository.findByEmail(username).get();
    user.setFailedAttempts(0);
    user.setLastLoginIp(ipAddress);
    repository.save(user);
  }
  
  /**
   * Increment the failed login attempts when a user fails to login.
   * @param username The username of the user that failed to login
   */
  public void loginFailed(String username, String ipAddress, String userAgent) {
    User user = repository.findByEmail(username).get();
    user.setFailedAttempts(1 + user.getFailedAttempts());
    failedLoginService.logFailedAttempt(user, ipAddress, userAgent);
    repository.save(user);
  }
  
  /**
   * Send a link to the user to reset the user's password.
   * @param username The username of the user to reset the password for
   */
  public void resetPassword(String username) {
    Optional<User> user = repository.findByEmail(username);
    if (user.isPresent() && user.get().getIsVerified()) {
      log.info("resetting password for {}", username);
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
      log.info("signing user up {}", username);
      String token = tokenService.generateSignUpToken(user.get());
      signUpMailer.sendSignUpEmail(user.get(), token);
    }
  }
  
  /**
   * Get the total users in our system.
   * @return Total
   */
  public Long totalUsers() {
    return repository.count();
  }
  
  /**
   * Update the given user with the following attributes.
   * @param id User id
   * @param firstName First name of user
   * @param lastName Last name of user
   * @param email Email of user
   * @param isActive Whether the user is active or not
   * @param roleIds The ids of the roles for this user
   * @param userVerification1 The verification attributes for the first verification
   * @param userVerification2 The verification attributes for the second verification
   * @param userVerification3 The verification attributes for the third verification
   * @param imagePath Name of the profile image to use if present
   */
  @Transactional
  public void updateUser(Long id, String firstName, String lastName,
      String email, boolean isActive, List<Long> roleIds, Map<String, String> userVerification1,
      Map<String, String> userVerification2, Map<String, String> userVerification3,
      String imagePath) {
    Optional<User> user = repository.findById(id);
    if (!user.isPresent()) {
      throw new EntityNotFoundException();
    }

    List<Role> roles = roleService.findAllRolesByIds(roleIds);
    User u = user.get();
    u.setFirstName(firstName);
    u.setLastName(lastName);
    u.setEmail(email);
    u.setIsActive(isActive);
    u.setRoles(roles);
    if (imagePath != null) {
      u.setImagePath(imagePath);
    }

    verificationService.updateVerifications(userVerification1,
        userVerification2, userVerification3);

    repository.save(u);
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
