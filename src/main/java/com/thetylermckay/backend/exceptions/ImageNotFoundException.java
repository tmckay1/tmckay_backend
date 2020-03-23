package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "image not found")
public class ImageNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
