package com.thetylermckay.backend.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtProperties jwtProperties;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private CustomTokenEnhancer tokenEnhancer;

  @Autowired
  private UserDetailsService userService;

  @Autowired
  TokenStore tokenStore;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient(jwtProperties.getClientId())
        .secret(passwordEncoder.encode(jwtProperties.getClientSecret()))
        .accessTokenValiditySeconds(jwtProperties.getAccessTokenValiditySeconds())
        .refreshTokenValiditySeconds(jwtProperties.getRefreshTokenValiditySeconds())
        .authorizedGrantTypes(jwtProperties.getAuthorizedGrantTypes())
        .scopes("read", "write")
        .resourceIds("api");
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(List.of(tokenEnhancer, accessTokenConverter()));
    endpoints
        .tokenStore(tokenStore)
        .reuseRefreshTokens(false)
        .tokenEnhancer(tokenEnhancerChain)
        .accessTokenConverter(accessTokenConverter())
        .userDetailsService(userService)
        .authenticationManager(authenticationManager);
  }
  
  /**
   * Override the default to store refresh tokens and refresh them.
   * @return Token service
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore);
    defaultTokenServices.setSupportRefreshToken(true);
    defaultTokenServices.setTokenEnhancer(accessTokenConverter());
    return defaultTokenServices;
  }

  @Bean
  JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    return converter;
  }
}