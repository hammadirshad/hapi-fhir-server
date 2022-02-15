package com.example.hapi.hapi.config.interceptor;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleAuthorizationInterceptor extends AuthorizationInterceptor {

  @Override
  public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
    // Process this header
    String authHeader = theRequestDetails.getHeader("Authorization");

    RuleBuilder builder = new RuleBuilder();
    builder
        .allow()
        .metadata()
        .andThen()
        .allow()
        .read()
        .allResources()
        .withAnyId()
        .andThen()
        .allow()
        .write()
        .resourcesOfType(Bundle.class)
        .inCompartment("Bundle", new IdType("Bundle/123"));

    return builder.build();
  }
}
