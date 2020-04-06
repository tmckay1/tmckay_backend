package com.thetylermckay.backend.listeners;

import com.thetylermckay.backend.services.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener 
    implements ApplicationListener<AuthenticationSuccessEvent> {

  @Autowired
  private LoginAttemptService service;

  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent e) {
    service.loginSucceeded(e);
  }
}
