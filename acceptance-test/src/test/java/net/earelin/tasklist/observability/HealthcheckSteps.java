package net.earelin.tasklist.observability;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class HealthcheckSteps {
  private static final String HEALTHCHECK_URL = "/actuator/health";

  private Response response;

  @Given("I have access to the microservice")
  public void i_have_access_to_the_microservice() {
    // No authentication or setup needed for this test
  }

  @When("I send a healthcheck request")
  public void i_send_a_healthcheck_request() {
    response = get(HEALTHCHECK_URL);
  }

  @Then("I should receive a valid response")
  public void i_should_receive_a_valid_response() {
    assertThat(response.getStatusCode())
        .isEqualTo(200);
  }

  @Then("the response should report the service as healthy")
  public void the_response_should_report_the_service_as_healthy() {
    response.then()
        .assertThat()
        .body("status", equalTo("UP"));
  }
}
