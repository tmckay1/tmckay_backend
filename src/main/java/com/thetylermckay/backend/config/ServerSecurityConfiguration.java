package com.thetylermckay.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class ServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private ServerProperties serverProperties;

  @Autowired
  private UserDetailsService userDetailsService;
  
  public ServerSecurityConfiguration(
      @Qualifier("userService") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if (serverProperties.isProd()) {
      http.requiresChannel()
          .anyRequest()
          .requiresSecure();
    }
  }

  /**
   * Setup and configure the authentication provider.
   * @return The authentication provider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * Override the CORS configuration class to allow requests from
   * the local environment.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Bean
  FilterRegistrationBean simpleCorsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(serverProperties.getHosts());
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return bean;
  }
}