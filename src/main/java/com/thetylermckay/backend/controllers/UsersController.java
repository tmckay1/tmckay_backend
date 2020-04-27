package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.exceptions.FileUploadException;
import com.thetylermckay.backend.helpers.ImageStreamer;
import com.thetylermckay.backend.services.RoleService;
import com.thetylermckay.backend.services.UserService;
import com.thetylermckay.backend.views.RoleViews;
import com.thetylermckay.backend.views.UserVerificationViews;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private ImageStreamer streamer;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService service;

  @GetMapping(path = "/autocomplete")
  @JsonView(RoleViews.Index.class)
  public @ResponseBody Map<String, Object> autocomplete() {
    Map<String, Object> map = new HashMap<>();
    map.put("roles", roleService.findAllRoles());
    return map;
  }

  @RequestMapping(value = "/", params = "profile_image", method = RequestMethod.POST)
  @ResponseBody
  public void create(@RequestParam(required = true) String firstName,
      @RequestParam(required = true) String lastName,
      @RequestParam(required = true) String email,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) List<Long> roleIds,
      @RequestParam(required = true) Map<String, String> userAttributes,
      @RequestParam("profile_image") MultipartFile profileImage) {
    String imageName = null;
    try {
      imageName = "/" + streamer.uploadImage(profileImage);
    } catch (IOException e) {
      throw new FileUploadException();
    }

    Map<String, String> userVerification1 = new HashMap<>();
    userVerification1.put("id",
        userAttributes.get("userVerification1[id]"));
    userVerification1.put("verificationAnswer",
        userAttributes.get("userVerification1[verificationAnswer]"));
    userVerification1.put("verificationQuestion",
        userAttributes.get("userVerification1[verificationQuestion]"));

    Map<String, String> userVerification2 = new HashMap<>();
    userVerification2.put("id",
        userAttributes.get("userVerification2[id]"));
    userVerification2.put("verificationAnswer",
        userAttributes.get("userVerification2[verificationAnswer]"));
    userVerification2.put("verificationQuestion",
        userAttributes.get("userVerification2[verificationQuestion]"));

    Map<String, String> userVerification3 = new HashMap<>();
    userVerification3.put("id",
        userAttributes.get("userVerification3[id]"));
    userVerification3.put("verificationAnswer",
        userAttributes.get("userVerification3[verificationAnswer]"));
    userVerification3.put("verificationQuestion",
        userAttributes.get("userVerification3[verificationQuestion]"));

    service.createUser(passwordEncoder, firstName, lastName, email, isActive,
        roleIds, userVerification1, userVerification2, userVerification3, imageName);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public void delete(@PathVariable Long id, HttpServletRequest request) {
    service.deleteUser(id, request);
  }

  @GetMapping(path = "/")
  @JsonView(UserVerificationViews.Index.class)
  public @ResponseBody Map<String, Object>
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllUsers(pageNumber, pageSize));
    map.put("totalItems", service.totalUsers());
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
      @RequestParam(required = true) Map<String, String> userAttributes,
      @RequestParam("profile_image") MultipartFile profileImage) {
    String imageName = null;
    if (!profileImage.isEmpty()) {
      try {
        imageName = "/" + streamer.uploadImage(profileImage);
      } catch (IOException e) {
        throw new FileUploadException();
      }
    }

    Map<String, String> userVerification1 = new HashMap<>();
    userVerification1.put("id",
        userAttributes.get("userVerification1[id]"));
    userVerification1.put("verificationAnswer",
        userAttributes.get("userVerification1[verificationAnswer]"));
    userVerification1.put("verificationQuestion",
        userAttributes.get("userVerification1[verificationQuestion]"));

    Map<String, String> userVerification2 = new HashMap<>();
    userVerification2.put("id",
        userAttributes.get("userVerification2[id]"));
    userVerification2.put("verificationAnswer",
        userAttributes.get("userVerification2[verificationAnswer]"));
    userVerification2.put("verificationQuestion",
        userAttributes.get("userVerification2[verificationQuestion]"));

    Map<String, String> userVerification3 = new HashMap<>();
    userVerification3.put("id",
        userAttributes.get("userVerification3[id]"));
    userVerification3.put("verificationAnswer",
        userAttributes.get("userVerification3[verificationAnswer]"));
    userVerification3.put("verificationQuestion",
        userAttributes.get("userVerification3[verificationQuestion]"));
    
    service.updateUser(id, firstName, lastName, email, isActive,
        roleIds, userVerification1, userVerification2, userVerification3, imageName);
  }

  @RequestMapping(value = "/{id}", params = "!profile_image", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String firstName,
      @RequestParam(required = true) String lastName,
      @RequestParam(required = true) String email,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) List<Long> roleIds,
      @RequestParam(required = true) Map<String, String> userAttributes
  ) {
    Map<String, String> userVerification1 = new HashMap<>();
    userVerification1.put("id",
        userAttributes.get("userVerification1[id]"));
    userVerification1.put("verificationAnswer",
        userAttributes.get("userVerification1[verificationAnswer]"));
    userVerification1.put("verificationQuestion",
        userAttributes.get("userVerification1[verificationQuestion]"));

    Map<String, String> userVerification2 = new HashMap<>();
    userVerification2.put("id",
        userAttributes.get("userVerification2[id]"));
    userVerification2.put("verificationAnswer",
        userAttributes.get("userVerification2[verificationAnswer]"));
    userVerification2.put("verificationQuestion",
        userAttributes.get("userVerification2[verificationQuestion]"));

    Map<String, String> userVerification3 = new HashMap<>();
    userVerification3.put("id",
        userAttributes.get("userVerification3[id]"));
    userVerification3.put("verificationAnswer",
        userAttributes.get("userVerification3[verificationAnswer]"));
    userVerification3.put("verificationQuestion",
        userAttributes.get("userVerification3[verificationQuestion]"));

    service.updateUser(id, firstName, lastName, email, isActive,
        roleIds, userVerification1, userVerification2, userVerification3, null);
  }
}
