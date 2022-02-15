package com.example.hapi.hapi.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Slf4j
public class RemoteNpmPackageValidationSupport extends NpmPackageValidationSupport {

  private final RestTemplate restTemplate;

  /** Constructor */
  public RemoteNpmPackageValidationSupport(
      @Nonnull FhirContext theFhirContext, RestTemplate restTemplate) {
    super(theFhirContext);
    this.restTemplate = restTemplate;
  }

  /**
   * Load an NPM package using a from simplifier specification,
   *
   * <p>e.g. <code>https://packages.simplifier.net/packageName/packageVersion</code>.
   *
   * @throws InternalErrorException If the classpath file can't be found
   */
  public void loadPackageFromSimplifier(String url) throws IOException {
    NpmPackage pkg = downloadSimplifierPackage(url);
    if (pkg != null) {
      if (pkg.getFolders().containsKey("package")) {
        NpmPackage.NpmPackageFolder packageFolder = pkg.getFolders().get("package");
        for (String nextFile : packageFolder.listFiles()) {
          if (nextFile.toLowerCase(Locale.US).endsWith(".json")) {
            String input =
                new String(packageFolder.getContent().get(nextFile), StandardCharsets.UTF_8);
            IBaseResource resource = getFhirContext().newJsonParser().parseResource(input);
            super.addResource(resource);
          }
        }
      }
    }
  }

  private NpmPackage downloadSimplifierPackage(String url) throws IOException {
    ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(url, Resource.class);
    if (responseEntity.hasBody() && responseEntity.getBody() != null) {
      InputStream inputStream = responseEntity.getBody().getInputStream();
      return NpmPackage.fromPackage(inputStream);
    }
    return null;
  }
}
