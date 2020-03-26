package com.thetylermckay.backend.repositories;

import static org.junit.Assert.assertEquals;

import com.thetylermckay.backend.models.Post;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class PostRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PostRepository postRepository;

  @Test
  public void shouldFindBySlug() {
    Post post = new Post();
    post.setTitle("My Post");
    post.setSlug("my-slug");
    entityManager.persist(post);
    entityManager.flush();
    
    Post target = postRepository.findBySlug(post.getSlug()).get();
    assertEquals(target.getTitle(), post.getTitle());
  }

  @Test
  public void shouldFindAllActive() {
    Post post1 = new Post();
    post1.setIsActive(true);
    post1.setTitle("My Active Post");
    entityManager.persist(post1);
    entityManager.flush();

    Post post2 = new Post();
    post2.setIsActive(false);
    post2.setTitle("My Inactive Post");
    entityManager.persist(post2);
    entityManager.flush();
    
    PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publishedAt"));
    Page<Post> posts = postRepository.findAll(page);
    assertEquals(posts.getTotalPages(), 1);
    assertEquals(posts.get().findFirst().get().getTitle(), post1.getTitle());
  }
}
