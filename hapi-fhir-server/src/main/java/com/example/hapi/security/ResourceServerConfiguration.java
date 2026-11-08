package com.example.hapi.security;

import com.example.hapi.hapi.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class ResourceServerConfiguration {

  String[] PUBLIC_URLS = {
      "/",
      "/live",
      "/error",
      "/actuator/**",
  };

  String[] SWAGGER_URLS = {
      "/fhir/",
      "/fhir/api-docs",
      "/fhir/swagger-ui/",
      "/fhir/swagger-ui/index.html",
      "/fhir/swagger-ui/index.css",
      "/fhir/swagger-ui/swagger-ui.css",
      "/fhir/swagger-ui/raccoon.png",
      "/fhir/swagger-ui/swagger-ui-bundle.js",
      "/fhir/swagger-ui/swagger-ui-standalone-preset.js"
  };

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http, ApplicationProperties applicationProperties) {
    return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
        authorizeRequests -> {
          boolean openApiEnabled = applicationProperties.getInterceptor().isOpenApiEnabled();
          authorizeRequests.requestMatchers(PUBLIC_URLS).permitAll();
          if (openApiEnabled) {
            authorizeRequests.requestMatchers(SWAGGER_URLS).permitAll();
          }
          authorizeRequests
              .requestMatchers("/hapi/**")
              .permitAll()
              .anyRequest()
              .denyAll();
        }).build();
  }
}
