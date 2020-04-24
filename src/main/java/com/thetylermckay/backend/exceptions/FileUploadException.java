package com.thetylermckay.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "failed to write to directory")
public class FileUploadException extends RuntimeException {
  private static final long serialVersionUID = 9L;
}
