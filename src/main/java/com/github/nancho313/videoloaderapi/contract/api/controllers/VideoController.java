package com.github.nancho313.videoloaderapi.contract.api.controllers;

import com.github.nancho313.videoloaderapi.business.service.VideoService;
import com.github.nancho313.videoloaderapi.contract.api.dto.DeleteVideoResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.GetVideoResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.UploadVideoResponse;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VideoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

  private static final String CREATED_VIDEO_SUCCESSFULLY = "The video was uploaded successfully.";

  private static final String DELETED_VIDEO_SUCCESSFULLY = "The video was deleted successfully";

  private final VideoService videoService;

  @PostMapping("/upload")
  public ResponseEntity<UploadVideoResponse> loadVideo(@RequestParam("video_file") MultipartFile videoFile, @RequestParam("title") String title, @RequestAttribute("userId") String userId) {

    var taskId = videoService.saveVideo(videoFile, title, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(new UploadVideoResponse(CREATED_VIDEO_SUCCESSFULLY, taskId));
  }

  @GetMapping
  public ResponseEntity<List<GetVideoResponse>> findAll(@RequestAttribute("userId") String userId) {

    var result = videoService.findAllVideosByUserId(userId).stream().map(this::mapToGetVideoResponse).toList();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetVideoResponse> findVideoById(@PathVariable String id, @RequestAttribute("userId") String userId) {

    var result = videoService.findVideoByIdAndUserId(userId, id);
    return ResponseEntity.ok(mapToGetVideoResponse(result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteVideoResponse> deleteVideoById(@PathVariable String id, @RequestAttribute("userId") String userId) {

    videoService.deleteVideoByIdAndUserId(userId, id);
    return ResponseEntity.ok(new DeleteVideoResponse(DELETED_VIDEO_SUCCESSFULLY, id));
  }

  private GetVideoResponse mapToGetVideoResponse(VideoEntity entity) {

    return new GetVideoResponse(entity.getId(), entity.getTitle(), entity.getStatus(), entity.getUploadedAt(), entity.getProcessedAt(), entity.getProcessedUrl());
  }
}
