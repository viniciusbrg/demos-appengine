package com.demo.demoapp.categories;

import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoRepository;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class CategoriesController {

  final CategoriesRepository categoriesRepository;
  final VideoRepository videoRepository;

  public CategoriesController(CategoriesRepository categoriesRepository,
                              VideoRepository videoRepository) {
    this.categoriesRepository = categoriesRepository;
    this.videoRepository = videoRepository;
  }

  @GetMapping("categories")
  public Iterable<Category> getAll() {
    return categoriesRepository.findAll();
  }

  @GetMapping("categories/{id}")
  public ResponseEntity<Category> getById(@PathVariable final Long id) {
    var maybeCategory = categoriesRepository.findById(id);

    return maybeCategory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("categories")
  @ResponseStatus(HttpStatus.CREATED)
  public Category create(@RequestBody @Valid final Category newCategory) {
    return categoriesRepository.save(newCategory);
  }

  @Transactional
  @PutMapping("categories/{id}")
  public ResponseEntity<Category> update(@PathVariable final Long id,
                                         @RequestBody @Valid final Category categoryData) {
    var maybeCategory = categoriesRepository.findById(id);

    if (maybeCategory.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var category = maybeCategory.get();
    if (categoryData.getId() != category.getId()) {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(categoriesRepository.save(categoryData));
  }

  @Transactional
  @DeleteMapping("categories/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final Long id) {
    categoriesRepository.findById(id).ifPresent(category -> categoriesRepository.deleteById(id));
  }

  @GetMapping("categories/{id}/videos")
  public ResponseEntity<Iterable<Video>> getVideosByCategory(@PathVariable final Long id) {
    var maybeCategory = categoriesRepository.findById(id);

    if (maybeCategory.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var category = maybeCategory.get();

    return ResponseEntity.ok(videoRepository.findVideosByCategory(category));
  }
}
