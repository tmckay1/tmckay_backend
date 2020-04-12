package com.thetylermckay.backend.services;

import com.thetylermckay.backend.enums.TokenType;
import com.thetylermckay.backend.exceptions.StaleTokenException;
import com.thetylermckay.backend.helpers.TokenHelper;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.models.UserVerification;
import com.thetylermckay.backend.repositories.TokenRepository;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Autowired
  private TokenHelper tokenHelper;
  
  @Autowired
  private TokenRepository repository;
  
  /**
   * Generate a token for resetting a password.
   * @return The token
   */
  public String generateResetPasswordToken(User user) {
    repository.deactivateTokens(user);
    return generateNewToken(user, TokenType.RESET_PASSWORD);
  }
  
  /**
   * Generate a token for signing up.
   * @return The token
   */
  public String generateSignUpToken(User user) {
    repository.deactivateTokens(user);
    return generateNewToken(user, TokenType.SIGN_UP);
  }
  
  /**
   * Generate a token for the given user and type.
   * @param user The user to generate the token for
   * @param type The type of token to generate
   * @return The token
   */
  private String generateNewToken(User user, TokenType type) {
    String token = tokenHelper.generateToken();
    while (repository.findByToken(token).isPresent()) {
      token = tokenHelper.generateToken();
    }
    long expirationSeconds = tokenHelper.exirationSeconds(type);

    Token t = new Token();
    t.setToken(token);
    t.setActive(true);
    t.setUser(user);
    t.setExpiresAt(ZonedDateTime.now().plusSeconds(expirationSeconds));
    repository.save(t);

    return token;
  }
  
  /**
   * Get the verification questions for a user with the given token.
   * @param token Token the user is getting questions for.
   * @return Questions
   */
  public Iterable<UserVerification> getVerificationQuestions(String token) {
    Optional<Token> t = repository.findByToken(token);
    if (!t.isPresent() || !t.get().isActive()
        || t.get().getExpiresAt().compareTo(ZonedDateTime.now()) < 0) {
      throw new StaleTokenException();
    }
    
    return t.get().getUser().getUserVerifications();
  }
}
