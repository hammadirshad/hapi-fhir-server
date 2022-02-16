package com.example.hapi.hapi.service;

import ca.uhn.fhir.util.ClasspathUtil;
import com.example.hapi.hapi.config.ApplicationProperties;
import com.example.hapi.hapi.config.ApplicationProperties.ProfileImplementations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationPackageInitializer {

  private final FhirValidationService fhirValidationService;
  private final RestTemplate restTemplate;
  private final ApplicationProperties applicationProperties;

  @EventListener(ApplicationReadyEvent.class)
  public void run() {
    try {
      getAllExamples().forEach(example -> fhirValidationService.validateResource(example, true));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private List<String> getAllExamples() throws IOException {
    List<String> examples = new ArrayList<>();
    for (ProfileImplementations profileImplementations :
        applicationProperties.getProfiles().values()) {
      NpmPackage pkg;
      if (profileImplementations.getPath() != null) {
        pkg = getClasspathPackage(profileImplementations);
      } else {
        pkg = downloadSimplifierPackage(profileImplementations.getUrl());
      }

      if (pkg != null) {
        if (pkg.getFolders().containsKey("examples")) {
          NpmPackage.NpmPackageFolder packageFolder = pkg.getFolders().get("examples");
          for (String nextFile : packageFolder.listFiles()) {
            String example =
                new String(packageFolder.getContent().get(nextFile), StandardCharsets.UTF_8);
            if (!example.isBlank()) {
              examples.add(example);
            }
          }
        }
      }
    }
    return examples;
  }

  private NpmPackage getClasspathPackage(ProfileImplementations profileImplementations)
      throws IOException {
    return NpmPackage.fromPackage(
        ClasspathUtil.loadResourceAsStream(profileImplementations.getPath()));
  }

  public NpmPackage downloadSimplifierPackage(String url) throws IOException {
    ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(url, Resource.class);
    if (responseEntity.hasBody() && responseEntity.getBody() != null) {
      InputStream inputStream = responseEntity.getBody().getInputStream();
      return NpmPackage.fromPackage(inputStream);
    }
    return null;
  }
}
