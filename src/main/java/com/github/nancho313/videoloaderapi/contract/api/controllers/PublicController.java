package com.github.nancho313.videoloaderapi.contract.api.controllers;

import com.github.nancho313.videoloaderapi.business.service.PublicService;
import com.github.nancho313.videoloaderapi.contract.api.dto.GetPublicVideoResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.GetRankingResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.VoteVideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

  private final PublicService publicService;

  @GetMapping("/videos")
  public ResponseEntity<List<GetPublicVideoResponse>> getAllProcessedVideos() {

    return ResponseEntity.ok(publicService.getAllProcessedVideos());
  }

  @PostMapping("/videos/{id}/vote")
  public ResponseEntity<VoteVideoResponse> voteVideo(@PathVariable String id, @RequestAttribute("userId") String userId) {

    publicService.voteVideo(userId, id);
    return ResponseEntity.ok(new VoteVideoResponse("Vote registered successfully."));
  }

  @GetMapping("/rankings")
  public ResponseEntity<List<GetRankingResponse>> getRanking(@RequestParam Map<String, String> queryParams) {

    return ResponseEntity.ok(publicService.getRanking(queryParams));
  }
}
