package com.example.hapi.hapi.service;

import com.example.hapi.hapi.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationPackageInitializer {

  private final FhirValidationService fhirValidationService;
  private final ValidationPackageExampleService validationPackageExampleService;
  private final ApplicationProperties applicationProperties;

  @EventListener(ApplicationReadyEvent.class)
  public void run() {
    if (applicationProperties.isPackageInitializatorEnabled()) {
      try {
        validationPackageExampleService.getAllExamples(true).forEach(
            example -> fhirValidationService.validateResource(example, true));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }


}
