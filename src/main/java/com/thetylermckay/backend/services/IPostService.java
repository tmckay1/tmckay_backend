package com.thetylermckay.backend.services;

import com.thetylermckay.backend.models.Post;
import java.util.List;

public interface IPostService {
  Post findPostBySlug(String slug);
  
  List<Post> findAllActivePosts(int pageNumber, int pageLength);
}
