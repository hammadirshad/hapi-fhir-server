package com.example.hapi.common.config;

import io.micrometer.common.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.observation.ServerHttpObservationDocumentation;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Slf4j
public class FHIRServerRequestObservationConvention extends DefaultServerRequestObservationConvention {

  private static final KeyValue URI_UNKNOWN = KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI, "UNKNOWN");
  private static final KeyValue URI_ROOT = KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI, "root");
  private static final KeyValue URI_NOT_FOUND = KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI, "NOT_FOUND");
  private static final KeyValue URI_REDIRECTION = KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI, "REDIRECTION");

  @Override
  protected KeyValue uri(ServerRequestObservationContext context) {
    if (context.getCarrier() != null) {
      String pattern = context.getPathPattern();
      if (pattern == null) {
        try {
          String requestPath = StringUtils.defaultString(context.getCarrier().getRequestURI());
          if (requestPath != null && !requestPath.isEmpty()) {
            pattern =
                requestPath.replaceAll(
                    "[a-fA-F0-9]{8}(?:-[a-fA-F0-9]{4}){4}[a-fA-F0-9]{8}", "{bundle_id}");
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      if (pattern != null) {
        if (pattern.isEmpty()) {
          return URI_ROOT;
        }
        return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.URI, pattern);
      }

      if (context.getResponse() != null) {
        HttpStatus status = HttpStatus.resolve(context.getResponse().getStatus());
        if (status != null) {
          if (status.is3xxRedirection()) {
            return URI_REDIRECTION;
          }
          if (status == HttpStatus.NOT_FOUND) {
            return URI_NOT_FOUND;
          }
        }
      }
    }
    return URI_UNKNOWN;
  }
}
