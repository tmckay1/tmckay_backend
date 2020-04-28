package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
    reason = "Error, cannot delete a role that belongs to at least one user")
public class CannotDeleteRoleInUseException extends RuntimeException {
  private static final long serialVersionUID = 11L;
}
