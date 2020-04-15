package com.thetylermckay.backend.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

  @Getter @Setter private String host;

  @Getter @Setter private List<String> hosts;

  @Getter @Setter private String resourcePath;
}
