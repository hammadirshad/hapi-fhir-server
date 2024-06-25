package com.example.hapi.controller;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@ConditionalOnClass(IResourceProvider.class)
public class HapiFhirController extends ServletWrappingController {

  public HapiFhirController(ServletRegistrationBean<RestfulServer> hapiServletRegistration) {

    setServletClass(RestfulServer.class);
    setServletName("simpleRestfulServer");

    setSupportedMethods(
        RequestMethod.GET.toString(),
        RequestMethod.PUT.toString(),
        RequestMethod.POST.toString(),
        RequestMethod.DELETE.toString());
  }

  @RequestMapping(value = {"/fhir/**"})
  public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    handleRequest(
        new HttpServletRequestWrapper(request) {
          @Override
          public String getServletPath() {
            return "/fhir";
          }
        },
        response);
  }
}
