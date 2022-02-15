package com.example.hapi.hapi.resource;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.validation.ValidationResult;
import com.example.hapi.hapi.service.FhirValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BundleResourceProvider implements IResourceProvider {

  private final FhirValidationService fhirValidationService;

  @Read(version = false)
  public IBaseResource read(@IdParam IdType theId) {
    Bundle bundle = new Bundle();
    bundle.setId(new IdType(UUID.randomUUID().toString()));
    return bundle;
  }

  @Create
  public MethodOutcome create(@ResourceParam Bundle bundle) {

    return new MethodOutcome().setId(new IdType(UUID.randomUUID().toString()));
  }

  @Update
  public MethodOutcome update(@IdParam IdType theId, @ResourceParam Bundle bundle) {

    return new MethodOutcome();
  }

  @Search(type = Bundle.class)
  public List<Bundle> search(
      @Description(shortDefinition = "Bundle identifier token")
          @OptionalParam(name = Bundle.SP_IDENTIFIER)
          TokenParam theIdentifier,
      @Description(shortDefinition = "Bundle date (date=gt2011-01-01&date=lt2011-02-01)")
          @OptionalParam(name = "date")
          DateRangeParam theRange,
      @Offset Integer theOffset,
      @Count Integer theCount,
      @Sort SortSpec theSort) {

    return new ArrayList<>();
  }

  @Delete
  public MethodOutcome delete(@IdParam IdType theId) {

    return new MethodOutcome();
  }

  @Validate
  public MethodOutcome validate(
      @ResourceParam Bundle bundle,
      @Validate.Mode ValidationModeEnum ValidationModel,
      @Validate.Profile String profile) {
    if (bundle != null) {
      ValidationResult validationResult = fhirValidationService.validateResource(bundle, false);
      if (!validationResult.isSuccessful()) {
        return new MethodOutcome(validationResult.toOperationOutcome());
      }
    }
    return new MethodOutcome();
  }

  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Bundle.class;
  }
}
