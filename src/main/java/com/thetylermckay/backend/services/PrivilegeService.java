package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.models.Privilege;
import com.thetylermckay.backend.repositories.PrivilegeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {
  
  /**
   * Max page size allowed.
   */
  public final int maxPageLength = 10;
  
  @Autowired
  private PrivilegeRepository repository;
  
  /**
   * Create the given privilege with the following attributes.
   * @param name Name of the privilege
   * @param description Descriptive text
   */
  @Transactional
  public void createPrivilege(String name, String description) {
    Privilege p = new Privilege();
    p.setName(name.toUpperCase());
    p.setDescription(description);
    repository.save(p);
  }
  
  /**
   * Delete the role with the given id.
   * @param id The id of the user to delete
   */
  public void deletePrivilege(Long id) {
    Optional<Privilege> privilege = repository.findById(id);
    if (!privilege.isPresent()) {
      throw new EntityNotFoundException();
    }
    repository.delete(privilege.get());
  }

  public List<Privilege> findAllPrivileges() {
    Pageable wholePage = Pageable.unpaged();
    return repository.findAll(wholePage).toList();
  }

  public List<Privilege> findAllPrivileges(int pageNumber, int pageLength) {
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength));
    return repository.findAll(page).toList();
  }

  /**
   * Get a list of privileges with the given ids.
   * @param ids Ids of the privileges to retrieve
   * @return List of privileges
   */
  public List<Privilege> findAllPrivilegesByIds(List<Long> ids) {
    Iterable<Privilege> privileges = repository.findAllById(ids);
    List<Privilege> result = new ArrayList<Privilege>();
    for (Privilege privilege : privileges) {
      result.add(privilege);
    }
    return result;
  }
  
  public Long totalPrivileges() {
    return repository.count();
  }

  /**
   * Create the given privilege with the following attributes.
   * @param id Privilege id
   * @param name Name of the privilege
   * @param description Descriptive text
   */
  @Transactional
  public void updatePrivilege(Long id, String name, String description) {
    Optional<Privilege> privilege = repository.findById(id);
    if (!privilege.isPresent()) {
      throw new EntityNotFoundException();
    }

    Privilege p =  privilege.get();
    p.setName(name.toUpperCase());
    p.setDescription(description);

    repository.save(p);
  }
}
