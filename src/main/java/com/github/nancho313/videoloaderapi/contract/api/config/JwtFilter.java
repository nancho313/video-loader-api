package com.github.nancho313.videoloaderapi.contract.api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private static final List<String> OMITTED_PATHS = List.of("/auth", "/rankings", "/actuator");

  private final String secretKey;

  public JwtFilter(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    if(OMITTED_PATHS.stream().anyMatch(omittedPath -> request.getRequestURI().contains(omittedPath))){
      filterChain.doFilter(request, response);
      return;
    }

    try {

      var authorizationValue = Optional.ofNullable(request.getHeader("Authorization")).orElseThrow(() -> new AuthorizationDeniedException("Access denied."));
      var token = getTokenFromHeader(authorizationValue);
      var decodedJwt = validateJWT(token);
      var userId = decodedJwt.getSubject();
      request.setAttribute("userId", userId);
      filterChain.doFilter(request, response);

    } catch (JWTVerificationException e) {

      throw new AuthorizationDeniedException("Access denied");
    }
  }

  private DecodedJWT validateJWT(String token) {

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    JWTVerifier verifier = JWT.require(algorithm).withIssuer("nancho313").build();
    return verifier.verify(token);
  }

  private String getTokenFromHeader(String authorization) {
    return authorization.replace("Bearer ", "");
  }
}
