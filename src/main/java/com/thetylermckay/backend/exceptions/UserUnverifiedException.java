package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "The user is not verified. You need to first verify the user before continuing.")
public class UserUnverifiedException extends RuntimeException {
  private static final long serialVersionUID = 7L;
}
