package com.example.hapi.hapi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.narrative2.NullNarrativeGenerator;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.interceptor.*;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import com.example.hapi.hapi.config.interceptor.CapabilityStatementInterceptor;
import com.example.hapi.hapi.config.interceptor.SimpleAuthorizationInterceptor;
import com.example.hapi.hapi.config.interceptor.SimpleOpenApiInterceptor;
import com.example.hapi.hapi.resource.BundleResourceProvider;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleRestfulServer extends RestfulServer {

  private final FhirContext fhirContext;
  private final ApplicationProperties applicationProperties;
  private final BundleResourceProvider bundleResourceProvider;
  private final FhirInstanceValidator fhirInstanceValidator;
  private final SimpleAuthorizationInterceptor simpleAuthorizationInterceptor;

  @Override
  protected void initialize() {
    setFhirContext(fhirContext);
    registerProvider(bundleResourceProvider);

    /* ResponseHighlighter */
    if (applicationProperties.getInterceptor().isResponseHighlighterEnabled()) {
      registerInterceptor(new ResponseHighlighterInterceptor());
    }
    /* Open_api */
    if (applicationProperties.getInterceptor().isOpenApiEnabled()) {
      registerInterceptor(new SimpleOpenApiInterceptor());
    }

    /* Authorization */
    if (applicationProperties.getInterceptor().isAuthorizationEnabled()) {
      registerInterceptor(simpleAuthorizationInterceptor);
    }

    /*FhirPathFilter */
    if (applicationProperties.getInterceptor().isFhirPathFilterEnabled()) {
      registerInterceptor(new FhirPathFilterInterceptor());
    }

    /* ETag */
    setETagSupport(
        applicationProperties.isEtagSupportEnabled()
            ? ETagSupportEnum.ENABLED
            : ETagSupportEnum.DISABLED);

    setDefaultPrettyPrint(applicationProperties.isDefaultPrettyPrint());

    setDefaultResponseEncoding(applicationProperties.getDefaultEncoding());

    /*Validation*/
    if (applicationProperties.getValidation().isRequestsEnabled()) {
      RequestValidatingInterceptor requestInterceptor = new RequestValidatingInterceptor();
      requestInterceptor.addValidatorModule(fhirInstanceValidator);
      requestInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
      requestInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
      requestInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
      // requestInterceptor.setResponseHeaderValueNoIssues("No issues detected");
      registerInterceptor(requestInterceptor);
    }

    if (applicationProperties.getValidation().isResponsesEnabled()) {
      ResponseValidatingInterceptor responseInterceptor = new ResponseValidatingInterceptor();
      responseInterceptor.addValidatorModule(fhirInstanceValidator);
      responseInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
      responseInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
      responseInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
      // responseInterceptor.setResponseHeaderValueNoIssues("No issues detected");
      registerInterceptor(responseInterceptor);
    }

    if (applicationProperties.getLogger().isLoggingEnabled()) {
      LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
      loggingInterceptor.setLoggerName(applicationProperties.getLogger().getName());
      loggingInterceptor.setMessageFormat(applicationProperties.getLogger().getFormat());
      loggingInterceptor.setErrorMessageFormat(applicationProperties.getLogger().getError_format());
      loggingInterceptor.setLogExceptions(applicationProperties.getLogger().isLogExceptions());
      this.registerInterceptor(loggingInterceptor);
    }

    String serverAddress = applicationProperties.getServer().getAddress();
    if (!Strings.isNullOrEmpty(serverAddress)) {
      setServerAddressStrategy(new HardcodedServerAddressStrategy(serverAddress));
    } else {
      setServerAddressStrategy(new IncomingRequestAddressStrategy());
    }

    /*
     * This server tries to dynamically generate narratives
     */
    INarrativeGenerator theNarrativeGenerator =
        applicationProperties.isNarrativeEnabled()
            ? new DefaultThymeleafNarrativeGenerator()
            : new NullNarrativeGenerator();
    fhirContext.setNarrativeGenerator(theNarrativeGenerator);

    setPagingProvider(new FifoMemoryPagingProvider(100));

    CapabilityStatementInterceptor capabilityStatementInterceptor =
        new CapabilityStatementInterceptor(applicationProperties);
    registerInterceptor(capabilityStatementInterceptor);

    /* setServerConformanceProvider(
    new SimpleServerCapabilityStatementProvider(this, validationSupport,capabilityStatementInterceptor));*/

  }
}
