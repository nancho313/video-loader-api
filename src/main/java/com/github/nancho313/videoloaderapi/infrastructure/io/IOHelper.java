package com.github.nancho313.videoloaderapi.infrastructure.io;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class IOHelper {

  public void validateResolutionAndDuration(byte[] video) {

    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(video);
         FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(byteArrayInputStream)) {

      grabber.start();

      int width = grabber.getImageWidth();
      long durationMicro = grabber.getLengthInTime();
      double durationInSeconds = durationMicro / 1_000_000.0;

      grabber.stop();

      if (durationInSeconds < 20 || durationInSeconds > 60) {

        throw new IllegalArgumentException("Invalid video. The duration of the video must be between 20 and 60 seconds.");
      }

      if (width < 1080) {

        throw new IllegalArgumentException("Invalid video. The resolution of the video is invalid. Must be 1080p.");
      }

    } catch (IllegalArgumentException e) {

      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Something went wrong when validating the video.");
    }
  }
}
