package com.demo.demoapp.unit.videos;

import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoRepository;
import com.demo.demoapp.videos.VideosController;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class DeleteVideosTest {
  @MockBean
  VideoRepository videoRepository;

  @Autowired
  VideosController videosController;

  private final long videoId = 1;
  private final Video video = new Video(videoId, "Title", "description", "http://test.com");

  @Test
  public void deleteExistingVideo() {
    Mockito.when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

    videosController.delete(videoId);

    Mockito.verify(videoRepository, Mockito.times(1)).findById(videoId);
    Mockito.verify(videoRepository, Mockito.times(1)).deleteById(videoId);
  }

  @Test
  public void deleteVideoNotFound() {
    Mockito.when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

    videosController.delete(videoId);

    Mockito.verify(videoRepository, Mockito.times(1)).findById(videoId);
    Mockito.verify(videoRepository, Mockito.never()).deleteById(videoId);
  }
}
