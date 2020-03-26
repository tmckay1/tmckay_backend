package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.repositories.PostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService implements IPostService {
  
  @Autowired
  private PostRepository postRepository;
  
  @Override
  public List<Post> findAllActivePosts(int pageNumber, int pageLength) {
    Sort sort = Sort.by(Sort.Direction.DESC, "publishedAt");
    PageRequest page = PageRequest.of(pageNumber, pageLength, sort);
    return postRepository.findAll(page).toList();
  }
  
  @Override
  public Post findPostBySlug(String slug) {
    return postRepository.findBySlug(slug).isPresent()
          ? postRepository.findBySlug(slug).get()
          : null;
  }
}
