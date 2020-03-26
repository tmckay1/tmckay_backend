package com.thetylermckay.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

  @Autowired
  private ServerProperties serverProperties;

  /**
   * Override the CORS configuration class to allow requests from
   * the local environment.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // allow cors for our local frontend server
    registry.addMapping("/**")
        .allowedOrigins(serverProperties.getHosts());
  }
}