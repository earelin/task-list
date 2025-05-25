package net.earelin.tasklist.application.rest.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.earelin.tasklist.domain.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  private static final String EMAIL = "john.smith@example.com";
  private static final String PASSWORD = "secret";
  private static final String NAME = "John";
  private static final String SURNAME = "Smith";

  @MockitoBean
  private UserRepository userRepository;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  void create_user() {
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(
          """
          {
            "email": "%s",
            "firstname": "%s",
            "surname": "%s",
            "password": "%s"
          }
          """.formatted(EMAIL, NAME, SURNAME, PASSWORD))
    .when()
        .post("/users")
    .then()
        .statusCode(201)
        .body("email", equalTo(EMAIL))
        .body("firstname", equalTo(NAME))
        .body("surname", equalTo(SURNAME))
        .body("$", not(hasKey("password")));

    verify(userRepository).save(
        org.mockito.ArgumentMatchers.argThat(user ->
            user.getEmail().equals(EMAIL) &&
            user.getFirstname().equals(NAME) &&
            user.getSurname().equals(SURNAME) &&
            user.getPassword() != null
        )
    );
  }
}
