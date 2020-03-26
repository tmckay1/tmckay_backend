package com.thetylermckay.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

  private String[] hosts;

  private String resourcePath;

  public String[] getHosts() {
    return hosts;
  }
  
  public void setHosts(String[] hosts) {
    this.hosts = hosts;
  }

  public String getResourcePath() {
    return this.resourcePath;
  }
  
  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
