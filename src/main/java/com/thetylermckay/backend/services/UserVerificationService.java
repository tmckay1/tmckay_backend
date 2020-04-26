package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.exceptions.UserVerificationAttemptsExceededException;
import com.thetylermckay.backend.exceptions.UserVerificationException;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.models.UserVerification;
import com.thetylermckay.backend.repositories.UserVerificationRepository;
import java.util.ArrayList;
import java.util.List;
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
  private UserVerificationRepository repository;

  @Autowired
  private TokenService tokenService;
  
  @Autowired
  private UserService userService;
  
  /**
   * Create the following verifications for the given user.
   * @param user The user to modify the verifications for.
   * @param userVerification1 First user verification
   * @param userVerification2 Second user verification
   * @param userVerification3 Third user verification
   */
  public void createVerifications(User user, Map<String, String> userVerification1,
      Map<String, String> userVerification2, Map<String, String> userVerification3) {
    UserVerification verification1 = new UserVerification();
    UserVerification verification2 = new UserVerification();
    UserVerification verification3 = new UserVerification();
    List<UserVerification> userVerifications = new ArrayList<>();
    userVerifications.add(verification1);
    userVerifications.add(verification2);
    userVerifications.add(verification3);

    UserVerification verification = userVerifications.get(0);
    verification.setUser(user);
    verification.setVerificationAnswer(userVerification1.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification1.get("verificationQuestion"));
    verification = userVerifications.get(1);
    verification.setUser(user);
    verification.setVerificationAnswer(userVerification2.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification2.get("verificationQuestion"));
    verification = userVerifications.get(2);
    verification.setUser(user);
    verification.setVerificationAnswer(userVerification3.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification3.get("verificationQuestion"));
    
    repository.saveAll(userVerifications);
  }
  
  /**
   * Update the given verifications.
   * @param userVerification1 First user verification
   * @param userVerification2 Second user verification
   * @param userVerification3 Third user verification
   */
  public void updateVerifications(Map<String, String> userVerification1,
      Map<String, String> userVerification2, Map<String, String> userVerification3) {
    List<Long> ids = new ArrayList<>();
    ids.add(Long.parseLong(userVerification1.get("id")));
    ids.add(Long.parseLong(userVerification2.get("id")));
    ids.add(Long.parseLong(userVerification3.get("id")));

    List<UserVerification> userVerifications = (List<UserVerification>) repository.findAllById(ids);
    if (userVerifications.size() != 3) {
      throw new EntityNotFoundException();
    }
    
    UserVerification verification = userVerifications.get(0);
    verification.setVerificationAnswer(userVerification1.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification1.get("verificationQuestion"));
    verification = userVerifications.get(1);
    verification.setVerificationAnswer(userVerification2.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification2.get("verificationQuestion"));
    verification = userVerifications.get(2);
    verification.setVerificationAnswer(userVerification3.get("verificationAnswer"));
    verification.setVerificationQuestion(userVerification3.get("verificationQuestion"));
    
    repository.saveAll(userVerifications);
  }

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
