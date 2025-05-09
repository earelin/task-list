package net.earelin.tasklist;

import static io.restassured.RestAssured.given;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;

public class SetUp {
  public static final String EMAIL = "testuser@example.com";
  public static final String PASSWORD = "testpassword";

  private static final String NAME = "test";
  private static final String SURNAME = "user";
  private static final String USER_URL = "/users";

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;

    createUser();

    var authSchema = new BasicAuthScheme();
    authSchema.setUserName(EMAIL);
    authSchema.setPassword(PASSWORD);

    RestAssured.requestSpecification = new RequestSpecBuilder()
        .addHeader("Content-Type", "application/json")
        .setAuth(authSchema)
        .build();
  }

  @AfterAll
  public static void down() {
    deleteUser();
  }

  private static void createUser() {
    given()
        .header("Content-Type", "application/json")
        .body(
          """
          {
            "email": "%s",
            "name": "%s",
            "surname": "%s",
            "password": "%s"
          }
          """.formatted(EMAIL, NAME, SURNAME, PASSWORD))
        .post(USER_URL)
    .then()
        .statusCode(201);
  }

  private static void deleteUser() {
    given()
        .header("Content-Type", "application/json")
        .delete(USER_URL)
    .then()
        .statusCode(204);
  }
}
