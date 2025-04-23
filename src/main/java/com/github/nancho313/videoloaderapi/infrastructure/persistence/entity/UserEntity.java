package com.github.nancho313.videoloaderapi.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "user_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  private String id;
  private String name;
  private String lastname;
  private String email;
  private String city;
  private String country;
  private String password;
}