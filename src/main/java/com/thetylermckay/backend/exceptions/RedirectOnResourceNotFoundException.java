package com.thetylermckay.backend.exceptions;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@ControllerAdvice
public class RedirectOnResourceNotFoundException {

  /**
   * Redirect to index page when we don't find a resource, so the frontend
   * can handle it.
   * @param ex No handler found exception
   * @param req Request
   * @param redirectAttributes attributes to add to redirect response
   * @return
   */
  // @ExceptionHandler(value = NoHandlerFoundException.class)
  public Object handleStaticResourceNotFound(final NoHandlerFoundException ex,
      HttpServletRequest req, RedirectAttributes redirectAttributes) {
    if (req.getRequestURI().startsWith("/api")) {
      return this.getApiResourceNotFoundBody(ex, req);
    }
    return "redirect:/index.html";
  }

  private ResponseEntity<String> getApiResourceNotFoundBody(
      NoHandlerFoundException ex, HttpServletRequest req) {
    return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
  }
}
