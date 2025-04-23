package com.github.nancho313.videoloaderapi.infrastructure.persistence.dao;

import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VoteEntity;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.projection.VideoVotesProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteDao extends CrudRepository<VoteEntity, String> {

  @Query(nativeQuery = true, value = """
          select video_id, COUNT(id) as votes
          from vote_tbl vt
          where video_id in (?1)
          group by video_id""")
  List<VideoVotesProjection> getVotesFromVideos(List<String> videoIds);

  boolean existsByUserIdAndVideoId(String userId, String videoId);
}
