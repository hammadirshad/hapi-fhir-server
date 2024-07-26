package com.example.hapi.common.config;

import com.example.hapi.hapi.config.ApplicationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class ServerAppConfig {

  @Bean
  public FHIRServerRequestObservationConvention extendedServerRequestObservationConvention() {
    return new FHIRServerRequestObservationConvention();
  }

  @Bean
  @ConditionalOnMissingBean(RestTemplateBuilder.class)
  public RestTemplateBuilder restTemplateBuilder() {
    return new RestTemplateBuilder();
  }

  @Bean
  @ConditionalOnMissingBean(RestTemplate.class)
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }
}
