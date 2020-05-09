package com.thetylermckay.backend.config;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfig {
  @Bean
  public Bugsnag bugsnag() {
    return new Bugsnag("686fb857401d55f948291e7cd74bc507");
  }
}
