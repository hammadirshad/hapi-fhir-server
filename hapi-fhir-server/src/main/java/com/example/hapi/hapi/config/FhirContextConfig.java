package com.example.hapi.hapi.config;

import ca.uhn.fhir.context.FhirContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FhirContextConfig {

  @Bean
  public FhirContext fhirContext() {
    FhirContext fhirContext = FhirContext.forR4();
    // fhirContext.setParserErrorHandler(new StrictErrorHandler());
    // fhirContext.setDefaultTypeForProfile(PROFILE, Bundle.class);
    return fhirContext;
  }
}
