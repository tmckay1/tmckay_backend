package com.thetylermckay.backend.config;

import com.thetylermckay.backend.exceptions.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId("api");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.cors().and().authorizeRequests()
        .antMatchers("/posts/**", "/images/**", "/oauth/token", "/api/signUp").permitAll()
        .antMatchers("/api/users/**").hasAuthority("ADMIN")
        .antMatchers("/api/**").authenticated()
        .anyRequest().authenticated()
        .and().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(new CustomAccessDeniedHandler());
  }
}