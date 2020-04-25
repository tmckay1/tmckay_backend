package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "Error, cannot modify your own user")
public class CannotModifySelfException extends RuntimeException {
  private static final long serialVersionUID = 10L;
}
