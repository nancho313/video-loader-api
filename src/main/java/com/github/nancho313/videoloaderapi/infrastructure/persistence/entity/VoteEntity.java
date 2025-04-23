package com.github.nancho313.videoloaderapi.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "vote_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteEntity {

  @Id
  private String id;
  private String userId;
  private String videoId;
  private String videoOwnerId;
  private LocalDateTime createdDate;
}
