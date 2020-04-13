package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "The user verification did not succeed")
public class UserVerificationException extends RuntimeException {
  private static final long serialVersionUID = 5L;
}
