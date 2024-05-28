package com.demo.demoapp.videos;

import com.demo.demoapp.categories.Category;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

public class VideoDTO {
  @Positive
  private Long id;

  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotBlank
  @URL
  private String url;

  @Positive
  private Long categoryId = 1L;

  public VideoDTO() {
  }

  public VideoDTO(Long id, String title, String description, String url) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.url = url;
  }

  public VideoDTO(Long id, String title, String description, String url, Long categoryId) {
    this(id, title, description, url);
    this.categoryId = categoryId;
  }

  public Video toModel(Category category) {
    assert categoryId > 0;
    return new Video(id, title, description, url, category);
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public Long getId() {
    return id;
  }
}
