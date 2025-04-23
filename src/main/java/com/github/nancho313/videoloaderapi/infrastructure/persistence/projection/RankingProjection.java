package com.github.nancho313.videoloaderapi.infrastructure.persistence.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingProjection {

  private String name;
  private String email;
  private String id;
  private String city;
  private String country;
  private Long votes;

}
