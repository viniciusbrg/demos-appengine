package com.demo.demoapp.unit.videos;

import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoDTO;
import com.demo.demoapp.videos.VideoRepository;
import com.demo.demoapp.videos.VideosController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CreateVideoInvalidInformationTest {
  @MockBean
  VideoRepository videoRepository;

  @Autowired
  VideosController videosController;

  final Video expectedVideo = new Video(1L, "Title", "description", "http://test.com");

  void makeAssertions(final Video video) {
    var videoDto =
        new VideoDTO(video.getId(), video.getTitle(), video.getDescription(), video.getUrl());
    Assertions.assertThrows(RuntimeException.class, () -> videosController.create(videoDto));
    Mockito.verify(videoRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  public void createWithBlankTitleTest() {
    final Video video = new Video(null, "    ", "description", "http://test.com");
    makeAssertions(video);
  }

  @Test
  public void createWithEmptyTitleTest() {
    final Video video = new Video(null, "", "description", "http://test.com");
    makeAssertions(video);
  }


  @Test
  public void createWithEmptyDescriptionTest() {
    final Video video = new Video(null, "Title", "    ", "http://test.com");
    makeAssertions(video);
  }


  @Test
  public void createWithBlankDescriptionTest() {
    final Video video = new Video(null, "Title", "", "http://test.com");
    makeAssertions(video);
  }

  @Test
  public void createWithBlankUrlTest() {
    final Video video = new Video(null, "Title", "description", "    ");
    makeAssertions(video);
  }

  @Test
  public void createWithEmptyUrlTest() {
    final Video video = new Video(null, "Title", "description", "");
    makeAssertions(video);
  }

  @Test
  public void createWithNotUrlTest() {
    final Video video = new Video(null, "Title", "description", "really not a url");
    makeAssertions(video);
  }

  @Test
  public void createVideoWithMultipleErrors() {
    final Video video = new Video(null, "", "        ", "http://fake.com");
    makeAssertions(video);
  }
}
