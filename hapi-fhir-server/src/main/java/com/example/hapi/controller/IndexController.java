package com.example.hapi.controller;

import com.example.hapi.hapi.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {

  private final ApplicationProperties applicationProperties;

  @RequestMapping
  public RedirectView localRedirect() {
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(applicationProperties.getServer_address());
    return redirectView;
  }
}
