package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.services.RoleService;
import com.thetylermckay.backend.services.UserService;
import com.thetylermckay.backend.views.RoleViews;
import com.thetylermckay.backend.views.UserViews;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
class UsersController {

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService service;

  @GetMapping(path = "/")
  @JsonView(UserViews.Index.class)
  public @ResponseBody Iterable<User> 
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    return service.findAllUsers(pageNumber, pageSize);
  }

  @GetMapping(path = "/autocomplete")
  @JsonView(RoleViews.Index.class)
  public @ResponseBody Map<String, Object> autocomplete() {
    Map<String, Object> map = new HashMap<>();
    map.put("roles", roleService.findAllRoles());
    return map;
  }

  @PutMapping(path = "/{id}")
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String firstName,
      @RequestParam(required = true) String lastName,
      @RequestParam(required = true) String email,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) String[] roleIds) {
    
  }
}
