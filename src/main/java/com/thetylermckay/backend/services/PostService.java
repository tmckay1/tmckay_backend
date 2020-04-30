package com.thetylermckay.backend.services;

import com.thetylermckay.backend.exceptions.EntityNotFoundException;
import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.repositories.PostRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  
  /**
   * Max page size allowed.
   */
  public final int maxPageLength = 10;
  
  @Autowired
  private PostRepository repository;
  
  /**
   * Create the given user with the following attributes.
   * @param title Post title
   * @param summary A short description of the post
   * @param slug The post slug
   * @param contents The contents of the post
   * @param isActive Whether the post is active or not
   * @param imagePath Name of the post image to use if present
   */
  public void createPost(String title, String summary, String slug,
      String contents, boolean isActive, String imagePath) {
    Post p = new Post();
    p.setTitle(title);
    p.setSummary(summary);
    p.setSlug(slug);
    p.setContents(contents);
    if (isActive && p.getPublishedAt() == null) {
      p.setPublishedAt(ZonedDateTime.now());
    }
    if (imagePath != null) {
      p.setImagePath(imagePath);
    }
    p.setIsActive(isActive);

    repository.save(p);
  }
  
  /**
   * Delete the post with the given id.
   * @param id The id of the post to delete
   */
  public void deletePost(Long id) {
    Optional<Post> post = repository.findById(id);
    if (!post.isPresent()) {
      throw new EntityNotFoundException();
    }

    repository.delete(post.get());
  }
  
  /**
   * Find all posts that are active.
   * @param pageNumber Client page number
   * @param pageLength Size of Page
   * @return List of posts
   */
  public List<Post> findAllActivePosts(int pageNumber, int pageLength) {
    Sort sort = Sort.by(Sort.Direction.DESC, "publishedAt");
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength), sort);
    return repository.findAllActive(page).toList();
  }
  
  /**
   * Find all posts.
   * @param pageNumber Client page number
   * @param pageLength Size of Page
   * @return List of posts
   */
  public List<Post> findAllPosts(int pageNumber, int pageLength) {
    PageRequest page = PageRequest.of(pageNumber, Math.min(pageLength, maxPageLength));
    return repository.findAll(page).toList();
  }
  
  /**
   * Find a post with the given slug.
   * @param slug The post slug
   * @return Post
   */
  public Post findPostBySlug(String slug) {
    return repository.findBySlug(slug).isPresent()
          ? repository.findBySlug(slug).get()
          : null;
  }
  
  public Long totalPosts() {
    return repository.count();
  }
  
  /**
   * Create the given user with the following attributes.
   * @param id Post id
   * @param title Post title
   * @param summary A short description of the post
   * @param slug The post slug
   * @param contents The contents of the post
   * @param isActive Whether the post is active or not
   * @param imagePath Name of the post image to use if present
   */
  public void updatePost(Long id, String title, String summary, String slug,
      String contents, boolean isActive, String imagePath) {
    Optional<Post> post = repository.findById(id);
    if (!post.isPresent()) {
      throw new EntityNotFoundException();
    }

    Post p = post.get();
    p.setTitle(title);
    p.setSummary(summary);
    p.setSlug(slug);
    p.setContents(contents);
    if (isActive && p.getPublishedAt() == null) {
      p.setPublishedAt(ZonedDateTime.now());
    }
    if (imagePath != null) {
      p.setImagePath(imagePath);
    }
    p.setIsActive(isActive);
    p.setUpdatedAt(ZonedDateTime.now());

    repository.save(p);
  }
}
