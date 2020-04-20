package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.config.JwtProperties;
import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.repositories.TokenRepository;
import com.thetylermckay.backend.repositories.UserRepository;
import com.thetylermckay.backend.services.UserService;
import com.thetylermckay.backend.views.PostViews;
import com.thetylermckay.backend.views.UserViews;

import java.util.HashSet;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
class UsersController {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService service;

  @GetMapping(path = "/")
  @JsonView(UserViews.Index.class)
  public @ResponseBody Iterable<User> 
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    return service.findAllUsers(pageNumber, pageSize);
  }

  //   @GetMapping("/search")
  //   Page<User> search(@RequestParam String email, Pageable pageable,
  //       OAuth2Authentication authentication) {
  //       String auth = (String) authentication.getUserAuthentication().getPrincipal();
  //       String role = authentication.getAuthorities().iterator().next().getAuthority();
  //       if (role.equals(User.Role.USER.name())) {
  //           return repository.findAllByEmailContainsAndEmail(email, auth, pageable);
  //       }
  //       return repository.findByEmailContains(email, pageable);
  //   }

  //   @GetMapping("/findByEmail")
  //   @PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
  //   User findByEmail(@RequestParam String email, OAuth2Authentication authentication) {
  //       return repository.findByEmail(email).orElseThrow(() ->
  //           new EntityNotFoundException(User.class, "email", email));
  //   }
  //
  //   @GetMapping("/{id}")
  //   @PostAuthorize("!hasAuthority('USER') ||
  //       (returnObject != null && returnObject.email == authentication.principal)")
  //   User one(@PathVariable Long id) {
  //       return repository.findById(id).orElseThrow(() ->
  //           new EntityNotFoundException(User.class, "id", id.toString()));
  //   }

  //   @PutMapping("/{id}")
  //   @PreAuthorize("!hasAuthority('USER') ||
  //       (authentication.principal == @userRepository.findById(#id).orElse(
  //        new net.reliqs.gleeometer.users.User()).email)")
  //   void update(@PathVariable Long id, @Valid @RequestBody User res) {
  //       User u = repository.findById(id).orElseThrow(() ->
  //           new EntityNotFoundException(User.class, "id", id.toString()));
  //       res.setPassword(u.getPassword());
  //       res.setGlee(u.getGlee());
  //       repository.save(res);
  //   }

  @PostMapping
  @PreAuthorize("!hasAuthority('USER')")
  User create(@Valid @RequestBody User res) {
    return repository.save(res);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("!hasAuthority('USER')")
  void delete(@PathVariable Long id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @PutMapping("/{id}/changePassword")
  @PreAuthorize("!hasAuthority('USER') || (#oldPassword != null && !#oldPassword.isEmpty() &&"
      + "authentication.principal == @userRepository.findById(#id).orElse("
      + "new net.reliqs.gleeometer.users.User()).email)")
  void changePassword(@PathVariable Long id,
      @RequestParam(required = false) String oldPassword,
      @Valid @Size(min = 3) @RequestParam String newPassword) {
    User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    if (oldPassword == null || oldPassword.isEmpty()
        || passwordEncoder.matches(oldPassword, user.getPassword())) {
      user.setPassword(passwordEncoder.encode(newPassword));
      repository.save(user);
    } else {
      throw new ConstraintViolationException("old password doesn't match", new HashSet<>());
    }
  }
}
