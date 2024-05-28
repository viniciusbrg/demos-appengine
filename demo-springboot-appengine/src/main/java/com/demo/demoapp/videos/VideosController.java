package com.demo.demoapp.videos;

import com.demo.demoapp.categories.CategoriesRepository;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class VideosController {

  private final VideoRepository videoRepository;
  private final CategoriesRepository categoriesRepository;

  public VideosController(VideoRepository videoRepository,
                          CategoriesRepository categoriesRepository) {
    this.videoRepository = videoRepository;
    this.categoriesRepository = categoriesRepository;
  }

  @GetMapping("videos")
  public Iterable<Video> getAll(@RequestParam Optional<String> search) {
    if (search.isEmpty()) {
      return videoRepository.findAll();
    }

    return videoRepository.findByTitleIgnoreCaseContaining(search.get().toLowerCase());
  }

  @GetMapping("videos/{id}")
  public ResponseEntity<Video> getById(@PathVariable final Long id) {
    var video = videoRepository.findById(id);

    return video.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("videos")
  public ResponseEntity<Video> create(@RequestBody @Valid final VideoDTO video) {
    var category = categoriesRepository.findById(video.getCategoryId());

      if (category.isEmpty()) {
          return ResponseEntity.badRequest().build();
      }

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(videoRepository.save(video.toModel(category.get())));
  }

  @PutMapping("videos/{id}")
  @Transactional
  public ResponseEntity<Video> update(@PathVariable final Long id,
                                      @RequestBody @Valid final VideoDTO video) {
    var maybeVideo = videoRepository.findById(id);
    var maybeCategory = categoriesRepository.findById(video.getCategoryId());

      if (maybeVideo.isEmpty() || maybeCategory.isEmpty()) {
          return ResponseEntity.notFound().build();
      }

    var foundVideo = maybeVideo.get();
    var foundCategory = maybeCategory.get();

      if (foundVideo.getId() != video.getId()) {
          return ResponseEntity.badRequest().build();
      }

    return ResponseEntity.ok(videoRepository.save(video.toModel(foundCategory)));
  }

  @DeleteMapping("videos/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final Long id) {
    videoRepository.findById(id).ifPresent(e -> videoRepository.deleteById(id));
  }
}
