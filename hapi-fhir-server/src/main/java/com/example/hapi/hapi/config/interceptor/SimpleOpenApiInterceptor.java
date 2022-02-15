package com.example.hapi.hapi.config.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Slf4j
public class SimpleOpenApiInterceptor extends OpenApiInterceptor {

  @Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED)
  @Override
  public boolean serveSwaggerUi(
      HttpServletRequest theRequest,
      HttpServletResponse theResponse,
      ServletRequestDetails theRequestDetails)
      throws IOException {

    String requestPath = theRequest.getPathInfo();
    if (requestPath != null && requestPath.equals("/api-docs")) {

      try {
        Method m =
            getClass()
                .getSuperclass()
                .getDeclaredMethod("generateOpenApi", ServletRequestDetails.class);
        m.setAccessible(true);
        OpenAPI openApi = (OpenAPI) m.invoke(this, theRequestDetails);

        openApi
            .getPaths()
            .forEach(
                (key, pathValue) -> {
                  if (key.startsWith("/Bundle")) {
                    if (pathValue.getGet() != null) {
                      pathValue
                          .getGet()
                          .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                    }
                    if (pathValue.getPost() != null) {
                      pathValue
                          .getPost()
                          .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                    }
                    if (pathValue.getPut() != null) {
                      pathValue
                          .getPut()
                          .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                    }
                    if (pathValue.getDelete() != null) {
                      pathValue
                          .getDelete()
                          .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
                    }
                  }
                });

        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(Type.HTTP);
        securityScheme.setScheme("bearer");
        securityScheme.setBearerFormat("JWT");
        securityScheme.setIn(In.HEADER);
        openApi.getComponents().addSecuritySchemes("bearerAuth", securityScheme);

        String response = Yaml.pretty(openApi);
        theResponse.getWriter().flush();
        theResponse.getWriter().write(response);
        theResponse.getWriter().close();
        return false;
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return false;
    } else {
      return super.serveSwaggerUi(theRequest, theResponse, theRequestDetails);
    }
  }
}
