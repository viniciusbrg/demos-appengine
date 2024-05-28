package com.demo.demoapp.videos;

import com.demo.demoapp.categories.Category;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "videos")
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotBlank
  @URL
  private String url;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  public Video() {
  }

  public Video(Long id, String title, String description, String url) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.url = url;
  }

  public Video(Long id, String title, String description, String url, Category category) {
    this(id, title, description, url);
    this.category = category;
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

  public Long getId() {
    return id;
  }

  public Category getCategory() {
    return category;
  }
}
