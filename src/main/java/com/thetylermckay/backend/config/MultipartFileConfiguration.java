package com.thetylermckay.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

//@Configuration
public class MultipartFileConfiguration {

  /**
   * Define resolver for multi form data.
   * @return Resolver
   */
  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(10000000);
    return multipartResolver;
  }
}
