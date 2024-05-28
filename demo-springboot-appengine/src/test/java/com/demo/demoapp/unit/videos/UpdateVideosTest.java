package com.demo.demoapp.unit.videos;

import com.demo.demoapp.categories.Category;
import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoDTO;
import com.demo.demoapp.videos.VideoRepository;
import com.demo.demoapp.videos.VideosController;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UpdateVideosTest {
  @MockBean
  VideoRepository videoRepository;

  @Autowired
  VideosController videosController;

  @Test
  public void updateVideoCorrectInformation() {
    final long videoId = 1L;
    var video = new VideoDTO(videoId, "Title", "description", "http://test.com");
    final Video videoFromDb = video.toModel(new Category(1L, "a", "b"));
    Mockito.when(videoRepository.save(Mockito.any())).thenReturn(videoFromDb);
    Mockito.when(videoRepository.findById(videoId)).thenReturn(Optional.of(videoFromDb));

    final ResponseEntity<Video> updateResponse = videosController.update(videoId, video);

    Mockito.verify(videoRepository, Mockito.times(1)).save(Mockito.any());
    Mockito.verify(videoRepository, Mockito.times(1)).findById(videoId);
    Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    Assertions.assertEquals(video.getTitle(), updateResponse.getBody().getTitle());
  }

  @Test
  public void updateVideoWithInvalidInformation() {
    final long videoId = 1L;
    final VideoDTO video = new VideoDTO(videoId, " ", "  ", "http://test.com");

    Assertions.assertThrows(RuntimeException.class, () -> videosController.update(videoId, video));

    Mockito.verify(videoRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  public void updateWithVideoNotExisting() {
    final long videoId = 1L;
    var video = new VideoDTO(videoId, "Title", "description", "http://test.com");
    Mockito.when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

    final ResponseEntity<Video> updateResponse = videosController.update(videoId, video);

    Mockito.verify(videoRepository, Mockito.never()).save(Mockito.any());
    Mockito.verify(videoRepository, Mockito.times(1)).findById(videoId);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
  }

  @Test
  public void updateChangingIdField() {
    final long videoId = 1L;

    // use a different id to update video
    var video = new VideoDTO(2L, "Title", "description", "http://test.com");
    Mockito.when(videoRepository.findById(videoId))
        .thenReturn(Optional.of(new Video(1L, "Title", "description", "http://test.com")));

    final ResponseEntity<Video> updateResponse = videosController.update(videoId, video);

    Mockito.verify(videoRepository, Mockito.never()).save(Mockito.any());
    Mockito.verify(videoRepository, Mockito.times(1)).findById(videoId);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
  }
}
