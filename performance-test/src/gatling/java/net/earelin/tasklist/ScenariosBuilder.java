package net.earelin.tasklist;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.exec.Executable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.datafaker.Faker;

public final class ScenariosBuilder {
  private static final Faker faker = new Faker();

  private static final Iterator<Map<String, Object>> userFeeder =
      Stream.generate((Supplier<Map<String, Object>>) () -> Map.of(
          "name", faker.name().firstName(),
          "surname", faker.name().lastName(),
          "password", faker.internet().password(),
          "email", faker.internet().emailAddress()
      )).iterator();

  private static final Iterator<Map<String, Object>> taskListFeeder =
      Stream.generate((Supplier<Map<String, Object>>) () -> Map.of(
          "task-list-name", faker.hobby().activity(),
          "task-list-description", faker.lorem().sentence(5)
      )).iterator();

  private static final Iterator<Map<String, Object>> taskFeeder =
      Stream.generate((Supplier<Map<String, Object>>) () -> Map.of(
          "task-name", faker.hobby().activity(),
          "task-description", faker.lorem().sentence(10),
          "task-deadline", faker.timeAndDate()
              .future(365, TimeUnit.DAYS, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      )).iterator();

  private static final Iterator<Map<String, Object>> tagsFeeder =
      Stream.generate((Supplier<Map<String, Object>>) () -> Map.of(
          "tag-name", faker.hobby().activity(),
          "tag-description", faker.lorem().sentence(5)
      )).iterator();

  public static final ScenarioBuilder taskList = scenario("Task List")
      .exec(feed(userFeeder), createUserAccount())
      .pause(20, 30)
      .exec(getAllTaskLists())
      .pause(1, 2)
      .exec(feed(taskListFeeder), createTaskList())
      .pause(10, 20)
      .exec(getAllTaskLists())
      .pause(10, 20)
      .exec(feed(taskFeeder), createTask())
      .pause(10, 20)
      .exec(feed(taskFeeder), updateTask())
      .pause(1, 10)
      .exec(feed(tagsFeeder), addTagToTask())
      .pause(5, 20)
      .exec(feed(taskFeeder), createTask())
      .pause(5, 20)
      .exec(removeTask());

  private static Executable removeTask() {
    return http("Remove Task")
        .delete("/task-lists/#{task-list-id}/tasks/#{task-id}")
        .basicAuth("#{email}", "#{password}")
        .check(status().is(204));
  }

  private static Executable addTagToTask() {
    return http("Add Tag to Task")
        .post("/task-lists/#{task-list-id}/tasks/#{task-id}/tags")
        .basicAuth("#{email}", "#{password}")
        .body(StringBody(
            """
            {
              "tag": "#{tag-name}"
            }
            """))
        .asJson()
        .check(status().is(200));
  }

  private static Executable updateTask() {
    return http("Update Task")
        .put("/task-lists/#{task-list-id}/tasks/#{task-id}")
        .basicAuth("#{email}", "#{password}")
        .body(StringBody(
            """
            {
              "name": "#{task-name}",
              "description": "#{task-description}"
            }
            """))
        .asJson()
        .check(status().is(200));
  }

  private static Executable createTask() {
    return http("Create Task")
        .post("/task-lists/#{task-list-id}/tasks")
        .basicAuth("#{email}", "#{password}")
        .body(StringBody(
            """
            {
              "name": "#{task-name}",
              "description": "#{task-description}",
              "deadline": "#{task-deadline}"
            }
            """))
        .asJson()
        .check(status().is(201))
        .check(jmesPath("id").saveAs("task-id"));
  }

  private static Executable createUserAccount() {
    return http("Create User Account")
        .post("/users")
        .body(StringBody(
            """
            {
              "name": "#{name}",
              "surname": "#{surname}",
              "email": "#{email}",
              "password": "#{password}"
            }
            """))
        .asJson()
        .check(status().is(201));
  }

  private static Executable getAllTaskLists() {
    return http("Get All Task List")
        .get("/task-lists")
        .basicAuth("#{email}", "#{password}")
        .check(status().is(200));
  }

  private static Executable createTaskList() {
    return http("Create Task List")
        .post("/task-lists")
        .basicAuth("#{email}", "#{password}")
        .body(StringBody(
            """
                {
                  "name": "#{task-list-name}",
                  "description": "#{task-list-description}"
                }
                """))
        .asJson()
        .check(status().is(201))
        .check(jmesPath("id").saveAs("task-list-id"));
  }

  private ScenariosBuilder() {}
}
