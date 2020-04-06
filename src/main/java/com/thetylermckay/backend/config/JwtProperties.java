package com.thetylermckay.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  @Getter @Setter private String clientId;

  @Getter @Setter private String clientSecret;

  @Getter @Setter private String jwtSigningKey;

  @Getter @Setter private int accessTokenValiditySeconds;

  @Getter @Setter private String[] authorizedGrantTypes;

  @Getter @Setter private int refreshTokenValiditySeconds;
}
