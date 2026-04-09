package com.example.hapi.hapi.config;

import static org.apache.commons.lang3.StringUtils.defaultString;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import com.example.hapi.hapi.config.ApplicationProperties.ProfileImplementations;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FhirValidationConfig {

  private final ApplicationProperties applicationProperties;
  private final RestClient restClient;

  @Bean
  public IValidationSupport validationSupport(FhirContext fhirContext)
      throws IOException {
    ValidationSupportChain validationSupportChain = new ValidationSupportChain();
    validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(fhirContext));
    validationSupportChain.addValidationSupport(getNpmPackageValidationSupport(
        fhirContext, applicationProperties));
    validationSupportChain.addValidationSupport(
        new CommonCodeSystemsTerminologyService(fhirContext));
    validationSupportChain.addValidationSupport(
        new InMemoryTerminologyServerValidationSupport(fhirContext));
    validationSupportChain.addValidationSupport(
        new SnapshotGeneratingValidationSupport(fhirContext));

    return validationSupportChain;
  }


  @Bean
  public FhirInstanceValidator fhirInstanceValidator(IValidationSupport validationSupport) {
    FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(validationSupport);
    fhirInstanceValidator.setAnyExtensionsAllowed(true);
    fhirInstanceValidator.setErrorForUnknownProfiles(false);
    fhirInstanceValidator.setNoTerminologyChecks(false);
    fhirInstanceValidator.setAssumeValidRestReferences(true);

    BestPracticeWarningLevel level = BestPracticeWarningLevel.Ignore;
    fhirInstanceValidator.setBestPracticeWarningLevel(level);
    return fhirInstanceValidator;
  }

  @Bean
  public FhirValidator fhirValidator(FhirInstanceValidator fhirInstanceValidator, FhirContext ctx) {
    FhirValidator validator = ctx.newValidator();
    validator.registerValidatorModule(fhirInstanceValidator);
    return validator;
  }


  private NpmPackageValidationSupport getNpmPackageValidationSupport(FhirContext fhirContext,
      ApplicationProperties applicationProperties) throws IOException {

    RemoteNpmPackageValidationSupport npmPackageValidationSupport =
        new RemoteNpmPackageValidationSupport(fhirContext, restClient);
    for (ProfileImplementations profileImplementations :
        applicationProperties.getProfiles().values()) {
      log.info(
          "Loading Npm package: "
              + profileImplementations.getName()
              + "-"
              + profileImplementations.getVersion());
      if (profileImplementations.getPath() != null) {
        npmPackageValidationSupport.loadPackageFromClasspath(profileImplementations.getPath());
      } else if (profileImplementations.getUrl() != null) {
        npmPackageValidationSupport.loadPackageFromSimplifier(profileImplementations.getUrl());
      }
    }

    List<IBaseResource> resourceList = npmPackageValidationSupport.fetchAllConformanceResources();
    if (resourceList != null) {
      for (IBaseResource baseResource : resourceList) {

        String url = defaultString(
            fhirContext.newTerser().getSinglePrimitiveValueOrNull(baseResource, "url"));

        if (!url.contains("http://hl7.org/fhir")) {
          log.info("Npm package resource " + baseResource);
        }
      }
    }
    return npmPackageValidationSupport;
  }

}