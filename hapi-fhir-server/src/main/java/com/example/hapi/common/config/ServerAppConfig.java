package com.example.hapi.common.config;

import com.example.hapi.hapi.config.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class ServerAppConfig {

  @Bean
  public FHIRServerRequestObservationConvention extendedServerRequestObservationConvention() {
    return new FHIRServerRequestObservationConvention();
  }

  @Bean
  @ConditionalOnMissingBean(RestClient.class)
  public RestClient restClient(RestClient.Builder builder) {
    return builder.build();
  }
}
