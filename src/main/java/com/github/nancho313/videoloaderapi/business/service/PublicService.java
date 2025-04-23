package com.github.nancho313.videoloaderapi.business.service;

import com.github.nancho313.videoloaderapi.contract.api.dto.GetPublicVideoResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.GetRankingResponse;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.customquery.VoteCustomQuery;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.dao.VideoDao;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.dao.VoteDao;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VideoEntity;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VoteEntity;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.projection.RankingProjection;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.projection.VideoVotesProjection;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicService {

  private final VoteCustomQuery voteCustomQuery;

  private final VideoDao videoDao;

  private final VoteDao voteDao;

  public PublicService(VoteCustomQuery voteCustomQuery, VideoDao videoDao, VoteDao voteDao) {
    this.voteCustomQuery = voteCustomQuery;
    this.videoDao = videoDao;
    this.voteDao = voteDao;
  }

  public List<GetPublicVideoResponse> getAllProcessedVideos() {

    var processedVideos = videoDao.findByStatus("PROCESSED");
    var videoIds = processedVideos.stream().map(VideoEntity::getId).toList();
    Map<String, VideoVotesProjection> votesFromVideos = voteDao.getVotesFromVideos(videoIds).stream().collect(Collectors.toMap(VideoVotesProjection::getVideoId, value -> value));
    var result = new ArrayList<GetPublicVideoResponse>();
    processedVideos.forEach(processedVideo -> {
      result.add(new GetPublicVideoResponse(processedVideo.getId(),
              processedVideo.getTitle(),
              processedVideo.getStatus(),
              processedVideo.getUploadedAt(),
              processedVideo.getProcessedAt(),
              processedVideo.getProcessedUrl(),
              Optional.ofNullable(votesFromVideos.get(processedVideo.getId())).map(VideoVotesProjection::getVotes).orElse(0)));
    });
    return result.stream().sorted(Comparator.comparing(GetPublicVideoResponse::votes).reversed()).toList();
  }

  public void voteVideo(String userId, String videoId) {

    if(voteDao.existsByUserIdAndVideoId(userId, videoId)) {

      throw new IllegalArgumentException("The user has already voted for the video.");
    }

    var video = videoDao.findById(videoId).orElseThrow(() -> new NoSuchElementException("The video to vote does not exist."));
    var newVote = new VoteEntity(UUID.randomUUID().toString(), userId, videoId, video.getUserId(), LocalDateTime.now());
    voteDao.save(newVote);
  }

  public List<GetRankingResponse> getRanking(Map<String, String> filters) {

    List<RankingProjection> ranking = voteCustomQuery.getRankingWithFilters(filters);
    var result = new ArrayList<GetRankingResponse>();
    for (int i = 0; i < ranking.size(); i++) {

      var value = ranking.get(i);
      result.add(new GetRankingResponse(i+1, value.getName(), value.getEmail(), value.getCity(), value.getCountry(), value.getVotes().intValue()));
    }
    return result;
  }
}
