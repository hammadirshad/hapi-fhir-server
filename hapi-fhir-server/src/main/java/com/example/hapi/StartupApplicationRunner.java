package com.example.hapi;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
// @Service
@Profile("default")
@Slf4j
public class StartupApplicationRunner implements ApplicationRunner {

  @Override
  @SneakyThrows
  public void run(ApplicationArguments args) {}
}
