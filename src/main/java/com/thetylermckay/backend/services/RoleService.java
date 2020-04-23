package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.Role;
import com.thetylermckay.backend.repositories.RoleRepository;
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
}
