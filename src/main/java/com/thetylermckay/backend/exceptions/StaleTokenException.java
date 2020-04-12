package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "Token is no longer valid or does not exist")
public class StaleTokenException extends RuntimeException {
  private static final long serialVersionUID = 4L;
}
