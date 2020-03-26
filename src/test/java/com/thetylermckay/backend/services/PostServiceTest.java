package com.thetylermckay.backend.services;

import static org.junit.Assert.assertEquals;

import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.repositories.PostRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PostServiceTest {

  @TestConfiguration
  static class PostServiceTestContextConfiguration {
    
    @Bean
    public IPostService postService() {
      return new PostService();
    }
  }
  
  @Autowired
  private IPostService postService;
  
  @MockBean
  private PostRepository postRepository;
  
  /**
   * Setup configuration before each test.
   */
  @Before
  public void setUp() {
    Post post = new Post();
    post.setSlug("slugo");
    post.setTitle("How to eat");
    Mockito.when(postRepository.findBySlug(post.getSlug())).thenReturn(Optional.of(post));

    Page<Post> posts = new PageImpl<>(Arrays.asList(post));
    PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publishedAt"));
    Mockito.when(postRepository.findAll(page)).thenReturn(posts);
  }
  
  @Test
  public void shouldFindPostBySlug() {
    String slug = "slugo";
    Post post = postService.findPostBySlug(slug);
    assertEquals(post.getTitle(), "How to eat");
    
    slug = "nonexistentslug";
    post = postService.findPostBySlug(slug);
    assertEquals(post, null);
  }
  
  @Test
  public void shouldFindAll() {
    List<Post> posts = postService.findAllActivePosts(0, 10);
    assertEquals(posts.size(), 1);
    assertEquals(posts.get(0).getTitle(), "How to eat");
  }
}
