package com.example.hapi;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = ApiTestContext.class)
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false, printOnlyOnFailure = false)
@DirtiesContext
@TestPropertySource(locations = "classpath:application-server-test.yml")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("server-test")
public class ApiTestBase {

  @Autowired
  protected MockMvc mockMvc;
}
