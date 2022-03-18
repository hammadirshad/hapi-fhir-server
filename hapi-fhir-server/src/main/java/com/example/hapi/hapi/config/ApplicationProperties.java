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

  private boolean etagSupportEnabled = true;
  private EncodingEnum defaultEncoding = EncodingEnum.JSON;
  private boolean defaultPrettyPrint = true;
  private boolean narrativeEnabled = true;

  private Interceptor interceptor = new Interceptor();
  private Server server = new Server();
  private Validation validation = new Validation();
  private Logger logger = new Logger();
  private Map<String, ProfileImplementations> profiles = new HashMap<>();

  @Data
  public static class Interceptor {
    private boolean responseHighlighterEnabled = true;
    private boolean openApiEnabled = false;
    private boolean authorizationEnabled = false;
    private boolean fhirPathFilterEnabled = false;
  }

  @Data
  public static class ProfileImplementations {

    private String path;
    private String url;
    private String name;
    private String version;
  }

  @Data
  public static class Validation {

    private boolean requestsEnabled = true;
    private boolean responsesEnabled = true;
  }

  @Data
  public static class Logger {

    private boolean loggingEnabled = false;
    private String name = "fhirtest.access";
    private String error_format = "ERROR - ${requestVerb} ${requestUrl}";
    private String format =
        "Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]";
    private boolean logExceptions = true;
  }

  @Data
  public static class Server {

    private String path = "/hapi";
    private String address = null;
  }
}
