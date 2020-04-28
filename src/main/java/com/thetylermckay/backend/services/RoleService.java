package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.CannotDeleteRoleInUseException;
import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.models.Privilege;
import com.thetylermckay.backend.models.Role;
import com.thetylermckay.backend.repositories.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  
  /**
   * Max page size allowed.
   */
  public final int maxPageLength = 10;

  @Autowired
  private PrivilegeService privilegeService;

  @Autowired
  private RoleRepository repository;
  
  /**
   * Create the given role with the following attributes.
   * @param name Name of the role
   * @param description Descriptive text
   * @param privilegeIds The ids of the privileges for this role
   */
  @Transactional
  public void createRole(String name, String description, List<Long> privilegeIds) {
    List<Privilege> privileges = privilegeService.findAllPrivilegesByIds(privilegeIds);
    Role r = new Role();
    r.setName(name);
    r.setDescription(description);
    r.setPrivileges(privileges);
    repository.save(r);
  }
  
  /**
   * Delete the role with the given id.
   * @param id The id of the user to delete
   */
  public void deleteRole(Long id) {
    Optional<Role> role = repository.findById(id);
    if (!role.isPresent()) {
      throw new EntityNotFoundException();
    }

    if (role.get().getUsers().size() != 0) {
      throw new CannotDeleteRoleInUseException();
    }
    repository.delete(role.get());
  }

  public List<Role> findAllRoles() {
    Pageable wholePage = Pageable.unpaged();
    return repository.findAll(wholePage).toList();
  }

  public List<Role> findAllRoles(int pageNumber, int pageLength) {
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength));
    return repository.findAll(page).toList();
  }

  /**
   * Get a list of roles with the given ids.
   * @param ids Ids of the roles to retrieve
   * @return List of roles
   */
  public List<Role> findAllRolesByIds(List<Long> ids) {
    Iterable<Role> roles = repository.findAllById(ids);
    List<Role> result = new ArrayList<Role>();
    for (Role role : roles) {
      result.add(role);
    }
    return result;
  }
  
  public Long totalRoles() {
    return repository.count();
  }

  /**
   * Create the given role with the following attributes.
   * @param id Role id
   * @param name Name of the role
   * @param description Descriptive text
   * @param privilegeIds The ids of the privileges for this role
   */
  @Transactional
  public void updateRole(Long id, String name, String description, List<Long> privilegeIds) {
    Optional<Role> role = repository.findById(id);
    if (!role.isPresent()) {
      throw new EntityNotFoundException();
    }

    List<Privilege> privileges = privilegeService.findAllPrivilegesByIds(privilegeIds);
    Role r =  role.get();
    r.setName(name);
    r.setDescription(description);
    r.setPrivileges(privileges);

    repository.save(r);
  }
}
