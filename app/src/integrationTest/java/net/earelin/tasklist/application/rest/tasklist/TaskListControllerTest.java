package net.earelin.tasklist.application.rest.tasklist;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Disabled
  @Test
  @WithMockUser
  void create_task_list() {
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .auth()
            .with(httpBasic("user", "password"))
        .body(
            """
            {
              "name": "%s",
              "description": "%s"
            }
            """.formatted("My Task List", "This is my task list"))
    .when()
        .post("/users")
    .then()
        .statusCode(201);
  }
}
