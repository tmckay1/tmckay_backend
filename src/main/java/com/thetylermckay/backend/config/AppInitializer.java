package com.thetylermckay.backend.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class AppInitializer implements WebApplicationInitializer {

  @Autowired
  private ServerProperties serverProperties;

  private int maxUploadSize = 5 * 1024 * 1024; 
  
  @Bean
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Override
  public void onStartup(ServletContext sc) throws ServletException {
    ServletRegistration.Dynamic appServlet = sc.addServlet("mvc",
        new DispatcherServlet(new GenericWebApplicationContext()));

    appServlet.setLoadOnStartup(1);
     
    String tmp = String.format("%s/tmp", serverProperties.getResourcePath());
    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(tmp, 
        maxUploadSize, maxUploadSize * 2, maxUploadSize / 2);
     
    appServlet.setMultipartConfig(multipartConfigElement);
  }
}