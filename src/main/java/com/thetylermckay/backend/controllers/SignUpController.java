package com.thetylermckay.backend.controllers;

import com.thetylermckay.backend.enums.Role;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signUp")
public class SignUpController {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Sign in the user and return a token.
   * @param email User's email
   * @param password User's password
   * @return
   */
  @PostMapping
  public User signUp(@RequestParam String email, @RequestParam String password,
      @RequestParam String firstName, @RequestParam String lastName) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setFirstName(firstName);
    user.setLastName(lastName);
    return repository.save(user);
  }

//  @PostMapping("/validateEmail")
//  Boolean emailExists(@RequestParam String email) {
//    return repository.existsByEmail(email);
//  }
}
