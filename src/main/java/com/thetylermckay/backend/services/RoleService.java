package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.Role;
import com.thetylermckay.backend.repositories.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  
  @Autowired
  private RoleRepository repostiory;

  public List<Role> findAllRoles() {
    Pageable wholePage = Pageable.unpaged();
    return repostiory.findAll(wholePage).toList();
  }

  /**
   * Get a list of roles with the given ids.
   * @param ids Ids of the roles to retrieve
   * @return List of roles
   */
  public List<Role> findAllRolesByIds(List<Long> ids) {
    Iterable<Role> roles = repostiory.findAllById(ids);
    List<Role> result = new ArrayList<Role>();
    for (Role role : roles) {
      result.add(role);
    }
    return result;
  }
}
