package com.github.nancho313.videoloaderapi.business.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.nancho313.videoloaderapi.business.exception.InvalidCredentialsException;
import com.github.nancho313.videoloaderapi.contract.api.dto.SignInRequest;
import com.github.nancho313.videoloaderapi.contract.api.dto.SignUpRequest;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.dao.UserDao;
import com.github.nancho313.videoloaderapi.infrastructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private final UserDao userDao;

  private final Algorithm algorithm;

  public AuthService(UserDao userDao, @Value("${app.secret-key}") String secretKey) {

    this.userDao = userDao;
    this.algorithm = Algorithm.HMAC256(secretKey);
  }

  public void signUp(SignUpRequest signUpRequest) {

    if (!signUpRequest.password1().equals(signUpRequest.password2())) {

      throw new IllegalArgumentException("The passwords are not the same.");
    }

    if (userDao.existsByEmail(signUpRequest.email())) {

      throw new IllegalArgumentException("The email is already registered.");
    }

    var newUser = new UserEntity(UUID.randomUUID().toString(),
            signUpRequest.name(),
            signUpRequest.lastname(),
            signUpRequest.email(),
            signUpRequest.city(),
            signUpRequest.country(),
            passwordEncoder.encode(signUpRequest.password1()));

    userDao.save(newUser);
  }

  public String signIn(SignInRequest signInRequest) {

    var userOpt = userDao.findByEmail(signInRequest.email());

    if(userOpt.isEmpty() || !passwordEncoder.matches(signInRequest.password(), userOpt.get().getPassword())) {

      throw new InvalidCredentialsException("Invalid credentials");
    }

    return JWT.create()
            .withIssuer("nancho313")
            .withSubject(userOpt.get().getId())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(3600))
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm);
  }
}
