package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.Privilege;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Long> {
  Optional<Privilege> findByName(String name);
}

