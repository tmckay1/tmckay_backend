package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.services.IPostService;
import com.thetylermckay.backend.views.PostViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/posts")
public class PostsController {

  @Autowired
  private IPostService postService;

  @GetMapping(path = "/")
  @JsonView(PostViews.Index.class)
  public @ResponseBody Iterable<Post> 
      index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
              @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    return postService.findAllActivePosts(pageNumber, pageSize);
  }

  @GetMapping(path = "/{slug}")
  @JsonView(PostViews.Show.class)
  public @ResponseBody Post show(@PathVariable("slug") String slug) {
    return postService.findPostBySlug(slug);
  }
}