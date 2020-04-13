package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetylermckay.backend.models.UserVerification;
import com.thetylermckay.backend.services.TokenService;
import com.thetylermckay.backend.services.UserService;
import com.thetylermckay.backend.services.UserVerificationService;
import com.thetylermckay.backend.views.UserVerificationViews;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

  @Autowired
  private UserService service;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserVerificationService verificationService;

  @Autowired
  private TokenStore tokenStore;

  /**
   * Change the password for the given user.
   * @param password User's password
   * @param token Reset password token for this user
   */
  @PostMapping(path = "/change_password")
  @ResponseBody
  public void changePassword(@RequestParam String password, @RequestParam String token) {
    service.changePassword(password, token);
  }

  /**
   * Send an email to reset the user's password.
   * @param email User's email
   */
  @PostMapping(path = "/reset_password")
  @ResponseBody
  public void resetPassword(@RequestParam String email) {
    service.resetPassword(email);
  }

  /**
   * Remove an access token as valid so we can no longer log in. Note, this also
   * invalidates the refresh token
   * @param request The current http request object
   */
  @DeleteMapping(path = "/revoke_token")
  @ResponseBody
  public void revokeToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
      String tokenValue = authHeader.replace("Bearer", "").trim();
      OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
      tokenStore.removeAccessToken(accessToken);
    }
  }

  /**
   * Sign up the user.
   * @param email User's email
   */
  @PostMapping(path = "/sign_up")
  @ResponseBody
  public void signUp(@RequestParam String email) {
    service.signUp(email);
  }

  /**
   * Verify the questions have been answered correctly.
   * @param answers The answers to the questions in the verification form
   * @return Token to reset password
   */
  @SuppressWarnings("unchecked")
  @PostMapping(path = "/verify")
  @ResponseBody
  public String verify(@RequestParam String answers, @RequestParam String token)
      throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return verificationService.verifyUser(mapper.readValue(answers, Map.class), token);
  }

  /**
   * Get the verification questions for the user with the given token.
   * @param token Token of the user to get questions for.
   */
  @PostMapping(path = "/verification_questions")
  @JsonView(UserVerificationViews.Index.class)
  public @ResponseBody Iterable<UserVerification>
    verificationQuestions(@RequestParam String token) {
    return tokenService.getVerificationQuestions(token);
  }
}
