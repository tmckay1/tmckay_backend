package com.thetylermckay.backend.config;

import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.services.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

@Configuration
public class CustomTokenEnhancer implements TokenEnhancer {
  
  @Autowired
  private UserService service;

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
      OAuth2Authentication authentication) {
    Optional<User> user = service.getUserFromAuthentication(authentication);
    if (user.isPresent()) {
      Map<String, Object> additionalInfo = new HashMap<>();
      additionalInfo.put("image_path", user.get().getImagePath());
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    }
 
    return accessToken;
  }
}
