package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.services.PrivilegeService;
import com.thetylermckay.backend.services.RoleService;
import com.thetylermckay.backend.views.RoleViews;
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

@RestController
@RequestMapping("/api/roles")
@Validated
class RolesController {

  @Autowired
  private PrivilegeService privilegeService;

  @Autowired
  private RoleService service;

  @GetMapping(path = "/autocomplete")
  @JsonView(RoleViews.Index.class)
  public @ResponseBody Map<String, Object> autocomplete() {
    Map<String, Object> map = new HashMap<>();
    map.put("privileges", privilegeService.findAllPrivileges());
    return map;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public void create(@RequestParam(required = true) String name,
      @RequestParam(required = true) String description,
      @RequestParam(required = true) List<Long> privilegeIds) {
    service.createRole(name, description, privilegeIds);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public void delete(@PathVariable Long id) {
    service.deleteRole(id);
  }

  @GetMapping(path = "/")
  @JsonView(RoleViews.Index.class)
  public @ResponseBody Map<String, Object>
  index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
          @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllRoles(pageNumber, pageSize));
    map.put("totalItems", service.totalRoles());
    return map;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String name,
      @RequestParam(required = true) String description,
      @RequestParam(required = true) List<Long> privilegeIds) {
    service.updateRole(id, name, description, privilegeIds);
  }
}
