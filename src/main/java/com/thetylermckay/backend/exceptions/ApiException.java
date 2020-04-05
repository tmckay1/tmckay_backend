package com.thetylermckay.backend.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ApiException {

  private HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private String message;
  private String debugMessage;

  private ApiException() {
    timestamp = LocalDateTime.now();
  }

  public ApiException(HttpStatus status) {
    this();
    this.status = status;
  }

  /**
   * Initializer.
   * @param status Http status code
   * @param ex Error
   */
  public ApiException(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  /**
   * Initializer.
   * @param status Http status code
   * @param message Error message
   * @param ex Error
   */
  public ApiException(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }
  
  public String getDebugMessage() {
    return this.debugMessage;
  }
  
  public void setDebugMessage(String message) {
    this.debugMessage = message;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public HttpStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
