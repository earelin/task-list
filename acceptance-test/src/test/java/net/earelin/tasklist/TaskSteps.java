package net.earelin.tasklist;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TaskSteps {

  private final SharedState sharedState;

  public TaskSteps(SharedState sharedState) {
    this.sharedState = sharedState;
  }

  @Given("I have a task with the information")
  @When("I create a new task with the information")
  public void i_create_a_new_task_with_the_information(List<Map<String, String>> dataTable) {
    var data = dataTable.get(0);
    var taskId = given()
        .body(
          """
          {
            "name": "%s",
            "description": "%s"
          }
          """.formatted(data.get("name"), data.get("description")))
        .post(format("/task-lists/%s/tasks", sharedState.getTaskListId()))
    .then()
        .statusCode(201)
    .extract()
        .path("id")
        .toString();
    sharedState.setTaskId(taskId);
  }

  @Then("I should get the task with the information")
  public void i_should_get_the_task_with_the_information(List<Map<String, String>> dataTable) {
    var data = dataTable.get(0);
    given()
        .get(format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(200)
        .body("id", equalTo(sharedState.getTaskId().intValue()))
        .body("name", equalTo(data.get("name")))
        .body("description", equalTo(data.get("description")));
  }

  @When("I update the task to")
  public void i_update_the_task_to(List<Map<String, String>> dataTable) {
    var data = dataTable.get(0);
    given()
        .body(
          """
          {
            "name": "%s",
            "description": "%s"
          }
          """.formatted(data.get("name"), data.get("description")))
        .put(format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(200);
  }

  @Then("I should get the updated task with the information")
  public void i_should_get_the_updated_task_with_the_information(List<Map<String, String>> dataTable) {
    var data = dataTable.get(0);
    given()
        .get(
            format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(200)
        .body("id", equalTo(sharedState.getTaskId().intValue()))
        .body("name", equalTo(data.get("name")))
        .body("description", equalTo(data.get("description")));
  }

  @When("I remove the task {string}")
  public void i_remove_the_task(String taskName) {
    given()
        .delete(format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(204);
  }

  @Then("I should not get the task {string}")
  public void i_should_not_get_the_task(String taskName) {
    given()
        .get(format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(404);
  }

  @When("I add the tag {string} to the task {string}")
  public void i_add_the_tag_to_the_task(String tag, String taskName) {
    given()
        .body(
          """
          {
            "tag": "%s"
          }
          """.formatted(tag))
        .post(
            format(
                "/task-lists/%s/tasks/%s/tags",
                sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(200);
  }

  @Then("I should get the task with the tag {string} set")
  public void i_should_get_the_task_with_the_tag_set(String tag) {
    given()
        .get(format("/task-lists/%s/tasks/%s", sharedState.getTaskListId(), sharedState.getTaskId()))
    .then()
        .statusCode(200)
        .body("tags", equalTo(List.of(tag)));
  }
}
