package com.thetylermckay.backend.repositories;

import com.thetylermckay.backend.models.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
  Optional<Post> findBySlug(String slug);

  @Query("SELECT p FROM Post p WHERE is_active = 1 ORDER BY published_at DESC")
  Page<Post> findAll(Pageable pageRequest);
}
