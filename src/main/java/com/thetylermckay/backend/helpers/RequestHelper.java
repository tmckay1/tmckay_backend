package com.thetylermckay.backend.helpers;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

  /**
   * Return the IP Address for the given request.
   * @param request Request to get the IP from
   * @return IP Address
   */
  public static String getClientIP(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }
}
