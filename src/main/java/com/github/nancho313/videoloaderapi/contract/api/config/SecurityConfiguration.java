package com.github.nancho313.videoloaderapi.contract.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

  @Bean
  public WebSecurityCustomizer ignoringCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/api/auth/**", "/api/public/rankings");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, @Value("${app.secret-key}") String secretKey) throws Exception {
    http.authorizeHttpRequests((authorize) ->
                    authorize.requestMatchers(AntPathRequestMatcher.antMatcher("/api/videos/**"),
                                    AntPathRequestMatcher.antMatcher("/api/public/videos"),
                                    AntPathRequestMatcher.antMatcher("/api/public/videos/**/vote")
                            ).authenticated().anyRequest().permitAll())
            .addFilterBefore(new JwtFilter(secretKey), AuthorizationFilter.class)
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}