package com.example.hapi.hapi.config;

import ca.uhn.fhir.rest.api.EncodingEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "hapi.fhir")
@EnableConfigurationProperties
@Data
public class ApplicationProperties {

  private boolean response_highlighter_enabled = true;
  private boolean open_api_enabled = false;
  private boolean authorization_interceptor_enabled = false;
  private boolean etag_support_enabled = true;
  private EncodingEnum default_encoding = EncodingEnum.JSON;
  private boolean default_pretty_print = true;
  private boolean fhir_path_interceptor_enabled = false;
  private String server_address = null;
  private boolean narrative_enabled = true;
  private Validation validation = new Validation();
  private Logger logger = new Logger();
  private Map<String, ProfileImplementations> profiles = new HashMap<>();

  @Data
  public static class ProfileImplementations {

    private String path;
    private String url;
    private String name;
    private String version;
  }

  @Data
  public static class Validation {

    private boolean requests_enabled = true;
    private boolean responses_enabled = true;
  }

  @Data
  public static class Logger {

    private boolean logging_enabled = false;
    private String name = "fhirtest.access";
    private String error_format = "ERROR - ${requestVerb} ${requestUrl}";
    private String format =
        "Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]";
    private boolean log_exceptions = true;
  }
}
