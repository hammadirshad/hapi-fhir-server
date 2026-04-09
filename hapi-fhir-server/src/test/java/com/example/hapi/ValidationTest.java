package com.example.hapi;

import com.example.hapi.hapi.service.FhirValidationService;
import com.example.hapi.hapi.service.ValidationPackageExampleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidationTest extends ApiTestBase {

  @Autowired
  private ValidationPackageExampleService validationPackageExampleService;
  @Autowired
  private FhirValidationService fhirValidationService;

  @Test
  public void validateResources() throws Exception {

    validationPackageExampleService.getAllExamples(false)
        .forEach(example -> Assertions.assertThatCode(() ->
            fhirValidationService.validateResource(example, true)
        ).doesNotThrowAnyException());
  }
}
