package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.services.FailedLoginAttemptService;
import com.thetylermckay.backend.views.FailedLoginAttemptViews;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/failed_login_attempts")
@Validated
class FailedLoginAttemptsController {

  @Autowired
  private FailedLoginAttemptService service;

  @GetMapping(path = "/")
  @JsonView(FailedLoginAttemptViews.Index.class)
  public @ResponseBody Map<String, Object>
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllFailedLoginAttempts(pageNumber, pageSize));
    map.put("totalItems", service.totalFailedLoginAttempts());
    return map;
  }
}
