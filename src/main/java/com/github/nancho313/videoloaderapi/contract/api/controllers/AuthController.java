package com.github.nancho313.videoloaderapi.contract.api.controllers;

import com.github.nancho313.videoloaderapi.business.service.AuthService;
import com.github.nancho313.videoloaderapi.contract.api.dto.SignInRequest;
import com.github.nancho313.videoloaderapi.contract.api.dto.SignInResponse;
import com.github.nancho313.videoloaderapi.contract.api.dto.SignUpRequest;
import com.github.nancho313.videoloaderapi.contract.api.dto.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signUp")
  public ResponseEntity<SuccessfulResponse> signUp(@Valid @RequestBody SignUpRequest request) {

    authService.signUp(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessfulResponse("User created successfully."));
  }

  @PostMapping("/signIn")
  public ResponseEntity<SignInResponse> signUp(@Valid @RequestBody SignInRequest request) {

    var jwt = authService.signIn(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(new SignInResponse(jwt, "Bearer", 3600));
  }
}
