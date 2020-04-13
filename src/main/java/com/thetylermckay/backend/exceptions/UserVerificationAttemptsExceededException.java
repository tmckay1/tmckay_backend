package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "Failed to verify too many times, token no longer valid")
public class UserVerificationAttemptsExceededException extends RuntimeException {
  private static final long serialVersionUID = 6L;
}
