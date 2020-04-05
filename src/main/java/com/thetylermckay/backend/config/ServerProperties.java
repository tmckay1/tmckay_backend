package com.thetylermckay.backend.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

  private List<String> hosts;

  private String resourcePath;

  public List<String> getHosts() {
    return hosts;
  }
  
  public void setHosts(List<String> hosts) {
    this.hosts = hosts;
  }

  public String getResourcePath() {
    return this.resourcePath;
  }
  
  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }
}
