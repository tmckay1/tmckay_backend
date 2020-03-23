package com.thetylermckay.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TmckayBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(TmckayBackendApplication.class, args);
  }

  /**
   * Override the CORS configuration class to allow requests from
   * the local environment.
   * @return WebMvcConfigurer The CORS configuration object
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        // allow cors for our local frontend server
        registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://10.0.0.14:3000");
      }
    };
  }
}
