package com.example.hapi.controller;

import com.example.hapi.hapi.config.ApplicationProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {

  private final ApplicationProperties applicationProperties;

  @RequestMapping
  public RedirectView localRedirect(HttpServletRequest request) {
    RedirectView redirectView = new RedirectView();
    String serverAddress = applicationProperties.getServer().getAddress();
    if (serverAddress == null || serverAddress.isBlank()) {
      serverAddress = ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .build()
          .toUriString();
    }
    if (!serverAddress.endsWith("/")) {
      serverAddress += "/";
    }
    redirectView.setUrl(serverAddress);
    return redirectView;
  }
}
