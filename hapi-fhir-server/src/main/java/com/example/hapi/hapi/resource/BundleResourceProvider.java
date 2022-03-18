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
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BundleResourceProvider implements IResourceProvider {



  @Read(version = false)
  public IBaseResource read(@IdParam IdType theId) {
    Bundle bundle = new Bundle();
    bundle.setId(theId);
    bundle.setIdentifier(new Identifier().setSystem("https://hapifhir.io").setValue(UUID.randomUUID().toString()));
    return bundle;
  }


  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Bundle.class;
  }
}
