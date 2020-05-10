package com.thetylermckay.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.exceptions.FileUploadException;
import com.thetylermckay.backend.helpers.ImageStreamer;
import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.services.PostService;
import com.thetylermckay.backend.views.PostViews;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostsController {
  
  @Autowired
  private ImageStreamer streamer;

  @Autowired
  private PostService service;

  /**
   * Get the active posts.
   * @param pageNumber The client page number
   * @param pageSize The client page size
   * @return The result
   */
  @GetMapping(path = "/view_posts/")
  @JsonView(PostViews.Index.class)
  public @ResponseBody Map<String, Object>  
      allActive(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
              @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllActivePosts(pageNumber, pageSize));
    map.put("totalItems", service.totalActivePosts());
    return map;
  }

  /**
   * Create the following post.
   * @param title Post title
   * @param summary Post summary
   * @param slug Post slug
   * @param contents Post contents
   * @param isActive Post is active or not
   * @param image Post image
   */
  @RequestMapping(value = "/api/posts/", params = "image", method = RequestMethod.POST)
  @ResponseBody
  public void create(@RequestParam(required = true) String title,
      @RequestParam(required = true) String summary,
      @RequestParam(required = true) String slug,
      @RequestParam(required = true) String contents,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) MultipartFile image) {
    String imageName = null;
    try {
      imageName = "/" + streamer.uploadImage(image);
    } catch (IOException e) {
      throw new FileUploadException();
    }

    service.createPost(title, summary, slug, contents, isActive, imageName);
  }

  @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public void delete(@PathVariable Long id) {
    service.deletePost(id);
  }

  /**
   * Get a list of all posts, active or not.
   * @param pageNumber Number page client is on
   * @param pageSize Size of page for the client
   * @return Remote table format
   */
  @GetMapping(path = "/api/posts/")
  @JsonView(PostViews.Index.class)
  public @ResponseBody Map<String, Object> 
      index(@RequestParam(required = true, defaultValue = "0") Integer pageNumber, 
              @RequestParam(required = true, defaultValue = "10") Integer pageSize) {
    Map<String, Object> map = new HashMap<>();
    map.put("items", service.findAllPosts(pageNumber, pageSize));
    map.put("totalItems", service.totalPosts());
    return map;
  }

  @GetMapping(path = "/view_posts/{slug}")
  @JsonView(PostViews.Show.class)
  public @ResponseBody Post show(@PathVariable("slug") String slug) {
    return service.findPostBySlug(slug);
  }

  /**
   * Update the following post.
   * @param id Post id
   * @param title Post title
   * @param summary Post summary
   * @param slug Post slug
   * @param contents Post contents
   * @param isActive Post is active or not
   * @param image Post image
   */
  @RequestMapping(value = "/api/posts/{id}", params = "image", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String title,
      @RequestParam(required = true) String summary,
      @RequestParam(required = true) String slug,
      @RequestParam(required = true) String contents,
      @RequestParam(required = true) Boolean isActive,
      @RequestParam(required = true) MultipartFile image) {
    String imageName = null;
    try {
      imageName = "/" + streamer.uploadImage(image);
    } catch (IOException e) {
      throw new FileUploadException();
    }

    service.updatePost(id, title, summary, slug, contents, isActive, imageName);
  }

  /**
   * Update the following post.
   * @param id Post id
   * @param title Post title
   * @param summary Post summary
   * @param slug Post slug
   * @param contents Post contents
   * @param isActive Post is active or not
   */
  @RequestMapping(value = "/api/posts/{id}", params = "!image", method = RequestMethod.PUT)
  @ResponseBody
  public void update(@PathVariable Long id,
      @RequestParam(required = true) String title,
      @RequestParam(required = true) String summary,
      @RequestParam(required = true) String slug,
      @RequestParam(required = true) String contents,
      @RequestParam(required = true) Boolean isActive) {
    service.updatePost(id, title, summary, slug, contents, isActive, null);
  }
}