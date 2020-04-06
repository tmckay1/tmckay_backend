package com.thetylermckay.backend.listeners;

import com.thetylermckay.backend.services.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener 
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  @Autowired
  private LoginAttemptService service;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    service.loginFailed(e);
  }
}
