package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.Role;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
  Optional<Role> findByName(String name);
}

