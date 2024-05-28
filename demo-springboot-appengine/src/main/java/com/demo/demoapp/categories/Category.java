package com.demo.demoapp.categories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String title;

  @NotNull
  @Pattern(regexp = "[0123456789ABCDEFabcdef]{6}")
  private String color;

  public Category() {
  }

  public Category(Long id, String title, String color) {
    this.id = id;
    this.title = title;
    this.color = color;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getColor() {
    return color;
  }
}
