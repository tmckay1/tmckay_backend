package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.Token;
import com.thetylermckay.backend.models.User;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
  Optional<Token> findByToken(String token);

  @Transactional
  @Modifying
  @Query("UPDATE Token t SET t.isActive = false WHERE t.user = :user")
  void deactivateTokens(@Param("user") User user);
}
