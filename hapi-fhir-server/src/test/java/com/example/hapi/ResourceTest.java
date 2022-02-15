package com.example.hapi;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceTest extends ApiTestBase {

  @Test
  public void indexRedirect() throws Exception {

    mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
  }
}
