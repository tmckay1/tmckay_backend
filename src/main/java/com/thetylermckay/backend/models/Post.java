package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.PostViews;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonView(PostViews.Index.class)
public class Post {

  @JsonView(PostViews.Show.class)
  @Getter @Setter private String contents;

  @Getter @Setter private ZonedDateTime createdAt;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter @Setter private Long id;

  @Getter @Setter private String imagePath;

  @Getter @Setter private Boolean isActive;

  @Getter @Setter private ZonedDateTime publishedAt;

  @Getter @Setter private String slug;

  @Getter @Setter private String summary;

  @Getter @Setter private String title;

  @Getter @Setter private ZonedDateTime updatedAt;
}
