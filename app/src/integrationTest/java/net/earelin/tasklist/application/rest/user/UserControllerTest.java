package net.earelin.tasklist.application.rest.user;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

  private static final String EMAIL = "john.smith@example.com";
  private static final String PASSWORD = "secret";
  private static final String NAME = "John";
  private static final String SURNAME = "Smith";

//  @MockitoBean
//  private UserRepository userRepository;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Test
  void create_user() {
    given()
        .body(
          """
          {
            "email": "%s",
            "name": "%s",
            "surname": "%s",
            "password": "%s"
          }
          """.formatted(EMAIL, NAME, SURNAME, PASSWORD))
    .when()
        .post("/users")
    .then()
        .statusCode(201);
  }
}
