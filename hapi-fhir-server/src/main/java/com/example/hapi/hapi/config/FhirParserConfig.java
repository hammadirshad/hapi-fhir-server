package com.example.hapi.hapi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FhirParserConfig {

  @Primary
  @Bean
  public IParser xmlParser(FhirContext fhirContext) {
    IParser xmlParser = fhirContext.newXmlParser();
    xmlParser.setPrettyPrint(true);
    return xmlParser;
  }

  @Bean
  public IParser jsonParser(FhirContext fhirContext) {
    IParser jsonParser = fhirContext.newJsonParser();
    jsonParser.setPrettyPrint(true);
    return jsonParser;
  }
}
