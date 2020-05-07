package com.thetylermckay.backend.helpers;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

  /**
   * Return the IP Address for the given request.
   * @param request Request to get the IP from
   * @return IP Address
   */
  public static String getClientIP(HttpServletRequest request) {
    String remoteAddr = "";
    if (request != null) {
      remoteAddr = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddr == null || "".equals(remoteAddr)) {
        remoteAddr = request.getRemoteAddr();
      }
    }

    return remoteAddr;
  }
  
  /**
   * Return the User Agent for the given request.
   * @param request Request to get the user agent from
   * @return User Agent
   */
  public static String getClientUserAgent(HttpServletRequest request) {
    String remoteAddr = "";
    if (request != null) {
      remoteAddr = request.getHeader("User-Agent");
    }

    return remoteAddr;
  }
}
