package com.example.hapi.hapi.config.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import com.example.hapi.hapi.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.ContactDetail;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;

@Interceptor
@RequiredArgsConstructor
public class CapabilityStatementInterceptor {

  private final ApplicationProperties applicationProperties;

  public void setCapabilityStatement(IBaseConformance iBaseConformance) {
    StringBuilder version = new StringBuilder();
    applicationProperties
        .getProfiles()
        .forEach(
            (key, profileImplementation) -> {
              version
                  .append(profileImplementation.getName())
                  .append("-")
                  .append(profileImplementation.getVersion());
            });

    CapabilityStatement capabilityStatement = (CapabilityStatement) iBaseConformance;
    capabilityStatement.setName("Fhir server");
    capabilityStatement.setPublisher("Fhir server");
    capabilityStatement.setTitle("Fhir server");
    capabilityStatement.setVersion(version.toString());
    capabilityStatement.setDescription("Hapi fhir server");

    capabilityStatement.addContact(
        new ContactDetail()
            .setName("HAPI")
            .setTelecom(
                List.of(
                    new ContactPoint()
                        .setSystem(ContactPointSystem.EMAIL)
                        .setUse(ContactPointUse.WORK)
                        .setValue("abc@example.com"))));

    capabilityStatement.setStatus(Enumerations.PublicationStatus.DRAFT);
  }

  @Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
  public void customize(IBaseConformance theCapabilityStatement) {
    setCapabilityStatement(theCapabilityStatement);
  }
}
