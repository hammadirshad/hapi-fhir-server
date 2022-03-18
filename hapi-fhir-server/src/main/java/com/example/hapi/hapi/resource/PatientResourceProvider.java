package com.example.hapi.hapi.resource;

import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.validation.ValidationResult;
import com.example.hapi.hapi.service.FhirValidationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientResourceProvider implements IResourceProvider {

  private final FhirValidationService fhirValidationService;

  @Read(version = false)
  public IBaseResource read(@IdParam IdType theId) {
    return getPatient(theId);
  }

  @Create
  public MethodOutcome create(@ResourceParam Patient patient) {
    return new MethodOutcome().setId(new IdType(UUID.randomUUID().toString()));
  }

  @Update
  public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient patient) {
    return new MethodOutcome();
  }

  @Search(type = Patient.class)
  public List<Patient> search(
      @Description(shortDefinition = "Patient identifier token")
      @OptionalParam(name = Patient.SP_IDENTIFIER)
          TokenParam theIdentifier,
      @Description(shortDefinition = "Patient date of birth (date=gt2011-01-01&date=lt2011-02-01)")
      @OptionalParam(name = "date")
          DateRangeParam theRange,
      @Offset Integer theOffset,
      @Count Integer theCount,
      @Sort SortSpec theSort) {

    return List.of(getPatient(new IdType(123)));
  }

  @Delete
  public MethodOutcome delete(@IdParam IdType theId) {
    return new MethodOutcome();
  }

  @Validate
  public MethodOutcome validate(
      @ResourceParam Patient patient,
      @Validate.Mode ValidationModeEnum ValidationModel,
      @Validate.Profile String profile) {
    if (patient != null) {
      ValidationResult validationResult = fhirValidationService.validateResource(patient, false);
      if (!validationResult.isSuccessful()) {
        return new MethodOutcome(validationResult.toOperationOutcome());
      }
    }
    return new MethodOutcome();
  }


  private Patient getPatient(IdType theId) {
    Patient patient = new Patient();
    patient.getMeta().addProfile("http://hl7.no/fhir/StructureDefinition/no-basis-Patient");
    patient.setId(theId);
    patient.setIdentifier(List.of(
        new Identifier().setSystem("urn:oid:2.16.578.1.12.4.1.4.1")
            .setValue(UUID.randomUUID().toString())));
    return patient;
  }


  @Override
  public Class<? extends IBaseResource> getResourceType() {
    return Patient.class;
  }
}
