package com.github.nancho313.videoloaderapi.infrastructure.persistence.dao;

import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends CrudRepository<UserEntity, String> {

  boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);
}
