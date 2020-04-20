package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Page<User> findAll(Pageable pageRequest);
}
