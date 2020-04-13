package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.UserVerificationAttemptsExceededException;
import com.thetylermckay.backend.exceptions.UserVerificationException;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.models.UserVerification;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationService {
  
  /**
   * Max number of times a user is allowed to attempt to verify.
   */
  public final int maxVerificationAttempts = 8;
  
  @Autowired
  private TokenService tokenService;
  
  @Autowired
  private UserService userService;

  /**
   * Verify the user answered the questions correctly and return a reset password
   * token the user can use to reset his or her password.
   * @param questionIdsToAnswers The responses from the user
   * @param token The token the user used to verify the account
   * @return A reset password token
   */
  public String verifyUser(Map<String, String> questionIdsToAnswers, String token) {
    Token t = tokenService.verifyToken(token);
    User user = t.getUser();
    if (user.getFailedVerificationAttempts() >= maxVerificationAttempts) {
      tokenService.invalidateToken(t);
      throw new UserVerificationAttemptsExceededException();
    }
    
    // verify the user
    if (!verify(user.getUserVerifications(), questionIdsToAnswers)) {
      userService.verificationFailed(user);
      throw new UserVerificationException();
    }
    userService.verifyUser(user);

    return tokenService.generateResetPasswordToken(user);
  }
  
  private boolean verify(Set<UserVerification> verifications,
      Map<String, String> questionIdsToAnswers) {
    // Ensure the total number of answers is correct
    if (verifications.size() != questionIdsToAnswers.size()) {
      return false;
    }
    
    for (UserVerification verification : verifications) {
      // Ensure we have this question answered
      String verificationId = verification.getId().toString();
      if (!questionIdsToAnswers.containsKey(verificationId)) {
        return false;
      }
      
      String actualAnswer = verification.getVerificationAnswer().toLowerCase().trim();
      String userAnswer = questionIdsToAnswers.get(verificationId).toLowerCase().trim();
      if (!actualAnswer.equals(userAnswer)) {
        return false;
      }
    }
    
    return true;
  }
}
