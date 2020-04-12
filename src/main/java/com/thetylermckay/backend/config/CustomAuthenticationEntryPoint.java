package com.thetylermckay.backend.config;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetylermckay.backend.exceptions.ApiException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private HttpMessageConverter<String> messageConverter;

  @Autowired
  private ObjectMapper mapper;

  public CustomAuthenticationEntryPoint() {
    this.messageConverter = new StringHttpMessageConverter();
  }

  @Override
  public void commence(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
    System.out.println("UNAUTHORIZED");
    System.out.println("UNAUTHORIZED");
    System.out.println("UNAUTHORIZED");
    System.out.println("UNAUTHORIZED");
    System.out.println("UNAUTHORIZED");
    System.out.println("UNAUTHORIZED");
    ApiException apiError = new ApiException(UNAUTHORIZED);
    apiError.setMessage(e.getMessage());
    apiError.setDebugMessage(e.getMessage());

    ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
    outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

    messageConverter.write(mapper.writeValueAsString(apiError),
        MediaType.APPLICATION_JSON, outputMessage);
  }
}
