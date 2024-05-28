package com.demo.demoapp.unit.videos;

import com.demo.demoapp.videos.Video;
import com.demo.demoapp.videos.VideoRepository;
import com.demo.demoapp.videos.VideosController;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ListVideoTests {
  @MockBean
  VideoRepository videoRepository;

  @Autowired
  VideosController videosController;

  String generateUrl(long index) {
    return "http://website" + index + ".com";
  }

  Iterable<Video> generateVideos() {
    return LongStream
        .range(1, 10)
        .mapToObj(value -> new Video
            (value, "Title" + value, "Description" + value, generateUrl(value)))
        .collect(Collectors.toList());
  }

  @Test
  public void listVideosTest() {
    Iterable<Video> generatedVideos = generateVideos();

    Mockito.when(videoRepository.findAll()).thenReturn(generatedVideos);

    final Iterable<Video> result = videosController.getAll(Optional.empty());

    Mockito.verify(videoRepository, Mockito.times(1)).findAll();
    Assertions.assertEquals(generatedVideos, result);
  }
}
