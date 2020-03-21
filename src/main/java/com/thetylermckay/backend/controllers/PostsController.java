package com.thetylermckay.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import com.thetylermckay.backend.models.Post;
import com.thetylermckay.backend.repositories.PostRepository;

@Controller
@RequestMapping(path="/posts")
public class PostsController {
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping(path="/")
	public @ResponseBody Iterable<Post> index() {
		return postRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publishedAt")));
	}

	@GetMapping(path="/{slug}")
	public @ResponseBody Post show(@PathVariable("slug") String slug) {
		return postRepository.findBySlug(slug).get();
	}
}