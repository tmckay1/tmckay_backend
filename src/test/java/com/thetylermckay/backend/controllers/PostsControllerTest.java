package com.thetylermckay.backend.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.services.PostService;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PostsController.class)
public class PostsControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PostService service;

  /**
   * Setup configuration before each test.
   */
  @Before
  public void setup() {
    Post post = new Post();
    post.setSlug("slugo");
    post.setTitle("How to eat");
    Mockito.when(service.findPostBySlug("slugo")).thenReturn(post);

    List<Post> posts = Arrays.asList(post);
    Mockito.when(service.findAllActivePosts(0, 10)).thenReturn(posts);
  }

  @Test
  public void shouldIndexPost() throws Exception {
    mvc.perform(get("/posts/")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].title", is("How to eat")));
  }

  @Test
  public void shouldShowPost() throws Exception {
    mvc.perform(get("/posts/slugo")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("How to eat")));
  }
}
