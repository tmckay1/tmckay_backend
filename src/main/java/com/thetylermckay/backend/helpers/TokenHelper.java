package com.thetylermckay.backend.helpers;

import com.thetylermckay.backend.enums.TokenType;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {

  private final int tokenLength = 32;

  public String generateToken() {
    return RandomStringUtils.randomAlphanumeric(tokenLength);
  }

  /**
   * The number of seconds this type of token is valid for.
   * @param type The type of token
   * @return Number of seconds
   */
  public long exirationSeconds(TokenType type) {
    switch (type) {
      case SIGN_UP:
        return 259200; // 3 days
      case RESET_PASSWORD:
        return 86400;  // 1 day
      default:
        return 86400;  // 1 day
    }
  }
}
