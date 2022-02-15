package com.example.hapi;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.kubernetes.commons.KubernetesCommonsAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration(exclude = {KubernetesCommonsAutoConfiguration.class})
public class ApiTestContext {}
