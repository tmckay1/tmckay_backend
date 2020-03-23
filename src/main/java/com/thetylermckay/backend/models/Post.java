package com.thetylermckay.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.thetylermckay.backend.views.PostViews;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonView(PostViews.Index.class)
public class Post {

  @JsonView(PostViews.Show.class)
  private String contents;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String imagePath;

  private Boolean isActive;

  private ZonedDateTime publishedAt;

  private String slug;

  private String summary;

  private String title;

  private ZonedDateTime updatedAt;

  public String getContents() {
    return contents;
  }

  public void setContents(String contents) {
    this.contents = contents;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public ZonedDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(ZonedDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
