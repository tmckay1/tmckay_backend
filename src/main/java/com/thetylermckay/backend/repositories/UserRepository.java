package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.User;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
