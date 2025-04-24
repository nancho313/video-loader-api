package com.github.nancho313.videoloaderapi.business.service;

import com.github.nancho313.videoloaderapi.infrastructure.io.IOHelper;
import com.github.nancho313.videoloaderapi.infrastructure.messaging.dto.VideoToProcessMessage;
import com.github.nancho313.videoloaderapi.infrastructure.messaging.publisher.NotifyVideoToProcessPublisher;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.dao.VideoDao;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VideoEntity;
import com.github.nancho313.videoloaderapi.infrastructure.s3.S3Integrator;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class VideoService {

  private final IOHelper ioHelper;

  private final VideoDao videoDao;

  private final NotifyVideoToProcessPublisher notifyVideoToProcessPublisher;

  private final S3Integrator s3Integrator;

  public VideoService(IOHelper ioHelper,
                      VideoDao videoDao,
                      NotifyVideoToProcessPublisher notifyVideoToProcessPublisher,
                      S3Integrator s3Integrator) {
    this.ioHelper = ioHelper;
    this.videoDao = videoDao;
    this.notifyVideoToProcessPublisher = notifyVideoToProcessPublisher;
    this.s3Integrator = s3Integrator;
  }

  @SneakyThrows
  public String saveVideo(MultipartFile videoFile, String title, String userId) {

    if (!videoFile.getOriginalFilename().endsWith(".mp4")) {

      throw new IllegalArgumentException("Invalid file. MP4 files are only allowed.");
    }

    ioHelper.validateResolutionAndDuration(videoFile.getBytes());

    var taskId = UUID.randomUUID().toString();
    var originalUrl = s3Integrator.storeVideo(videoFile.getBytes(), title);
    var newVideo = new VideoEntity(UUID.randomUUID().toString(), userId, title, title, originalUrl, null, "PENDING", LocalDateTime.now(), null);
    videoDao.save(newVideo);
    notifyVideoToProcessPublisher.sentVideoToProcess(new VideoToProcessMessage(newVideo.getId(), taskId));
    return taskId;
  }

  public List<VideoEntity> findAllVideosByUserId(String userId) {

    return videoDao.findByUserId(userId);
  }

  public VideoEntity findVideoByIdAndUserId(String userId, String videoId) {

    var video = videoDao.findById(videoId).orElseThrow( () -> new NoSuchElementException("The video with the given id does not exist."));

    if(!video.getUserId().equals(userId)) {

      throw new IllegalArgumentException("The video belongs to another user.");
    }

    return video;
  }

  public void deleteVideoByIdAndUserId(String userId, String videoId) {

    var video = videoDao.findById(videoId).orElseThrow( () -> new NoSuchElementException("The video with the given id does not exist."));

    if(!video.getUserId().equals(userId)) {

      throw new IllegalArgumentException("The video belongs to another user.");
    }

    if(!video.getStatus().equals("PENDING")) {

      throw new IllegalArgumentException("The video is not in PENDING status. Cannot be deleted.");
    }

    videoDao.deleteById(videoId);
  }

}
