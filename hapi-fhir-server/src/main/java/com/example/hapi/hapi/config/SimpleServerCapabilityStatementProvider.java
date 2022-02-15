package com.example.hapi.hapi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.util.FhirTerser;
import com.example.hapi.hapi.config.interceptor.CapabilityStatementInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseConformance;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Deprecated
public class SimpleServerCapabilityStatementProvider extends ServerCapabilityStatementProvider {

  private final FhirContext fhirContext;
  private final CapabilityStatementInterceptor capabilityStatementInterceptor;

  public SimpleServerCapabilityStatementProvider(
      RestfulServer restfulServer,
      IValidationSupport validationSupport,
      CapabilityStatementInterceptor capabilityStatementInterceptor) {
    super(restfulServer);
    fhirContext = restfulServer.getFhirContext();
    this.capabilityStatementInterceptor = capabilityStatementInterceptor;
  }

  @Override
  public IBaseConformance getServerConformance(
      HttpServletRequest request, RequestDetails theRequestDetails) {
    IBaseConformance iBaseConformance = super.getServerConformance(request, theRequestDetails);
    capabilityStatementInterceptor.setCapabilityStatement(iBaseConformance);
    return iBaseConformance;
  }

  @Override
  protected void postProcessRestResource(
      FhirTerser theTerser, IBase theResource, String theResourceName) {
    super.postProcessRestResource(theTerser, theResource, theResourceName);

    /*theTerser.addElement(
        theResource,
        "versioning",
        CapabilityStatement.ResourceVersionPolicy.VERSIONEDUPDATE.toCode());

    theTerser.addElement(
        theResource,
        "conditionalDelete",
        CapabilityStatement.ConditionalDeleteStatus.SINGLE.toCode());*/

    /*if (theResourceName.equals("Bundle")) {
      ExtensionUtil.setExtension(
          fhirContext,
          theResource,
          ExtensionConstants.CONF_RESOURCE_COUNT,
          "decimal",
          Long.toString(10));
    }*/
  }
}
