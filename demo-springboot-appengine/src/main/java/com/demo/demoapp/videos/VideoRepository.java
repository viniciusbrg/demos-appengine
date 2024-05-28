package com.demo.demoapp.videos;

import com.demo.demoapp.categories.Category;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Long> {
  Iterable<Video> findVideosByCategory(Category category);

  Iterable<Video> findByTitleIgnoreCaseContaining(String title);
}
