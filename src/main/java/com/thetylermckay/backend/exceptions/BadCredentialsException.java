package com.thetylermckay.backend.exceptions;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class BadCredentialsException extends OAuth2Exception {
  private static final long serialVersionUID = 3L;
  
  public BadCredentialsException() {
    super("Bad credentials");
  }
  
  public static OAuth2Exception create() {
    return OAuth2Exception.create(INVALID_GRANT, "Bad credentials");
  }
}
