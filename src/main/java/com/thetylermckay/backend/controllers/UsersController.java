package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.exceptions.FileUploadException;
import com.thetylermckay.backend.helpers.ImageStreamer;
import com.thetylermckay.backend.models.User;
import com.thetylermckay.backend.services.RoleService;
import com.thetylermckay.backend.services.UserService;
import com.thetylermckay.backend.views.RoleViews;
import com.thetylermckay.backend.views.UserViews;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@Validated
class UsersController {
  
  @Autowired
  private ImageStreamer streamer;

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

  @RequestMapping(value = "/{id}", params = "profile_image", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String firstName,
      @RequestParam(required = true) String lastName,
      @RequestParam(required = true) String email,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) List<Long> roleIds,
      @RequestParam("profile_image") MultipartFile profileImage) {
    String imageName = null;
    if (!profileImage.isEmpty()) {
      try {
        imageName = "/" + streamer.uploadImage(profileImage);
      } catch (IOException e) {
        throw new FileUploadException();
      }
    }
    
    service.updateUser(id, firstName, lastName, email, isActive, roleIds, imageName);
  }

  @RequestMapping(value = "/{id}", params = "!profile_image", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String firstName,
      @RequestParam(required = true) String lastName,
      @RequestParam(required = true) String email,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) List<Long> roleIds) {
    service.updateUser(id, firstName, lastName, email, isActive, roleIds, null);
  }
}
