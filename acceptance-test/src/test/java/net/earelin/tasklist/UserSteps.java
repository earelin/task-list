package net.earelin.tasklist;

import static io.restassured.RestAssured.when;

import io.cucumber.java.en.Given;

public class UserSteps {

  @Given("I have an user account")
  public void i_have_an_user_account() {
    when()
        .get("/users")
    .then()
        .statusCode(200);
  }
}
