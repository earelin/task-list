package net.earelin.tasklist;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TaskListSteps {
  private static final String TASK_LIST_URL = "/task-lists";

  private final SharedState sharedState;

  public TaskListSteps(SharedState sharedState) {
    this.sharedState = sharedState;
  }

  @When("I create a new task list with the information")
  @Given("I have a task list with the information")
  public void i_create_anew_task_list_with_the_information(List<Map<String, String>> dataTable) {
    Map<String, String> data = dataTable.get(0);
    var taskListId = given()
        .body(
          """
          {
            "name": "%s",
            "description": "%s"
          }
          """.formatted(data.get("name"), data.get("description")))
        .post(TASK_LIST_URL)
    .then()
        .statusCode(201)
    .extract()
        .path("id")
        .toString();
    sharedState.setTaskListId(taskListId);
  }

  @Then("I should get the task list with the information")
  public void i_should_get_the_task_list_with_the_information(List<Map<String, String>> dataTable) {
    Map<String, String> data = dataTable.get(0);
    given()
        .get(format("%s/%s", TASK_LIST_URL, sharedState.getTaskListId()))
    .then()
        .statusCode(200)
        .body("id", equalTo(sharedState.getTaskListId()))
        .body("name", equalTo(data.get("name")))
        .body("description", equalTo(data.get("description")))
        .body("tasks", anyOf(empty(), nullValue()));
  }

  @When("I update the task list to")
  public void i_update_the_task_list_to(List<Map<String, String>> dataTable) {
    Map<String, String> data = dataTable.get(0);
    given()
        .body(
          """
          {
            "name": "%s",
            "description": "%s"
          }
          """.formatted(data.get("name"), data.get("description")))
        .put(format("%s/%s", TASK_LIST_URL, sharedState.getTaskListId())).
    then()
        .statusCode(200);
  }

  @Given("I have a task list named {string}")
  public void i_have_a_task_list_named(String name) {
    var taskListId = given()
        .body(
            """
            {
              "name": "%s"
            }
            """.formatted(name))
        .post(TASK_LIST_URL)
    .then()
        .statusCode(201)
    .extract()
        .path("id")
        .toString();
    sharedState.setTaskListId(taskListId);
  }

  @When("I remove the task list {string}")
  public void i_remove_the_task_list(String name) {
    given()
        .delete(format("%s/%s", TASK_LIST_URL, sharedState.getTaskListId()))
    .then()
        .statusCode(204);
  }

  @Then("I should not get the task list {string}")
  public void i_should_not_get_the_task_list(String name) {
    given()
        .get(format("%s/%s", TASK_LIST_URL, sharedState.getTaskListId()))
    .then()
        .statusCode(404);
  }

}
