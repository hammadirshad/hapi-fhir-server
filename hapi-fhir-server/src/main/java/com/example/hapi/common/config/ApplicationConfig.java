package com.example.hapi.common.config;

import com.example.hapi.hapi.config.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class ApplicationConfig {}
