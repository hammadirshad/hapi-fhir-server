package com.example.hapi;

import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.hapi.hapi.config.ApplicationProperties;
import com.example.hapi.hapi.config.SimpleRestfulServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

// Server is accessible at eg. http://localhost:8080/hapi/metadata
@SpringBootApplication
public class ServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  @Bean
  public ServletRegistrationBean<RestfulServer> hapiServletRegistration(
      ApplicationProperties applicationProperties, SimpleRestfulServer simpleRestfulServer) {
    ServletRegistrationBean<RestfulServer> servletRegistrationBean =
        new ServletRegistrationBean<>();
    servletRegistrationBean.setServlet(simpleRestfulServer);
    servletRegistrationBean.addUrlMappings(applicationProperties.getServer().getPath() + "/*");
    servletRegistrationBean.setLoadOnStartup(1);
    return servletRegistrationBean;
  }

  @Bean
  public ConfigurableServletWebServerFactory webServerFactory() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers(
        connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
    return factory;
  }
}
