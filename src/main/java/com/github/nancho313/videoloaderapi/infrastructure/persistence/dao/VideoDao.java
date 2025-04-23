package com.github.nancho313.videoloaderapi.infrastructure.persistence.dao;

import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.VideoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoDao extends CrudRepository<VideoEntity, String> {

  List<VideoEntity> findByUserId(String userId);

  List<VideoEntity> findByStatus(String status);
}
