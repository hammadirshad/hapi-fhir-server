package com.example.hapi.hapi.service;

import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class FhirValidationService {

  private final FhirValidator fhirValidator;

  public ValidationResult validateResource(IBaseResource resource, boolean showIssues) {
    ValidationResult validationResult = fhirValidator.validateWithResult(resource);

    if (showIssues) {
      showIssues(validationResult);
    }

    return validationResult;
  }

  public ValidationResult validateResource(String resourceText, boolean showIssues) {
    ValidationResult validationResult = fhirValidator.validateWithResult(resourceText);

    if (showIssues) {
      showIssues(validationResult);
    }

    return validationResult;
  }

  protected void showIssues(ValidationResult validationResult) {
    validationResult.getMessages().stream()
        .sorted(Comparator.comparing(SingleValidationMessage::getSeverity))
        .forEach(
            next -> {
              String msg =
                  next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage();
              if (next.getSeverity() == ResultSeverityEnum.ERROR) {
                log.error(msg);
              } else if (next.getSeverity() == ResultSeverityEnum.WARNING) {
                log.warn(msg);
              } else {
                log.info(msg);
              }
            });
  }
}
