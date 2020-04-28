package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.services.PrivilegeService;
import com.thetylermckay.backend.views.RoleViews;
import java.util.HashMap;
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

@RestController
@RequestMapping("/api/privileges")
@Validated
class PrivilegesController {

  @Autowired
  private PrivilegeService service;

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public void create(@RequestParam(required = true) String name,
      @RequestParam(required = true) String description) {
    service.createPrivilege(name, description);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public void delete(@PathVariable Long id) {
    service.deletePrivilege(id);
  }

  @GetMapping(path = "/")
  @JsonView(RoleViews.Index.class)
  public @ResponseBody Map<String, Object>
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllPrivileges(pageNumber, pageSize));
    map.put("totalItems", service.totalPrivileges());
    return map;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String name,
      @RequestParam(required = true) String description) {
    service.updatePrivilege(id, name, description);
  }
}
