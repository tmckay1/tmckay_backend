package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.repositories.PostRepository;
import com.thetylermckay.backend.views.PostViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/posts")
public class PostsController {

  @Autowired
  private PostRepository postRepository;

  @GetMapping(path = "/")
  @JsonView(PostViews.Index.class)
  public @ResponseBody Iterable<Post> index() {
    PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publishedAt"));
    return postRepository.findAll(page);
  }

  @GetMapping(path = "/{slug}")
  @JsonView(PostViews.Show.class)
  public @ResponseBody Post show(@PathVariable("slug") String slug) {
    return postRepository.findBySlug(slug).get();
  }
}