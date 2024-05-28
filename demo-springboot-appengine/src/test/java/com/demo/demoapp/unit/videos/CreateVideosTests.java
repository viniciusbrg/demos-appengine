package com.demo.demoapp.unit.videos;

import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoDTO;
import com.demo.demoapp.videos.VideoRepository;
import com.demo.demoapp.videos.VideosController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CreateVideosTests {
  @MockBean
  VideoRepository videoRepository;

  @Autowired
  VideosController videosController;

  final Video expectedVideo = new Video(1L, "Title", "description", "http://test.com");

  @Test
  @DisplayName("creating new video with correct information")
  public void createWithCorrectInformationTest() {

    Mockito.when(videoRepository.save(Mockito.any())).thenReturn(expectedVideo);

    final VideoDTO video = new VideoDTO(null, "Title", "description", "http://test.com");
    final Video createdVideo = videosController.create(video).getBody();

    Mockito.verify(videoRepository, Mockito.times(1)).save(Mockito.any());
    Assertions.assertEquals(expectedVideo, createdVideo);
  }

}
