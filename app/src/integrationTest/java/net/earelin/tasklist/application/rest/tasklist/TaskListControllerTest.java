package net.earelin.tasklist.application.rest.tasklist;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskListControllerTest {
  @Test
  void create_task_list() {
    given()
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
