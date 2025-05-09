package net.earelin.tasklist.domain.task;

import static java.util.Objects.nonNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.earelin.tasklist.domain.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class TaskList {
  @Id
  @EqualsAndHashCode.Include
  private final String id;

  @DBRef
  @Setter
  private User user;

  @Setter
  private String name;

  @Setter
  private String description;

  private final Set<Task> tasks = new HashSet<>();

  public TaskList(String id, User user, String name, String description, Set<Task> tasks) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.description = description;
    if (nonNull(tasks)) {
      this.tasks.addAll(tasks);
    }
  }

  public TaskList withId(String id) {
    return new TaskList(id, user, name, description, tasks);
  }

  public TaskList withUser(User user) {
    return new TaskList(id, user, name, description, tasks);
  }

  public Optional<Task> findTaskById(Long id) {
    return tasks.stream()
        .filter(task -> task.getId().equals(id))
        .findFirst();
  }

  public Task addTask(Task task) {
    var maxId = tasks.stream()
        .map(Task::getId)
        .max(Long::compareTo)
        .orElse(0L);
    var taskWithId = task.withId(maxId + 1);
    tasks.add(taskWithId);
    return taskWithId;
  }

  public void removeTask(Long taskId) {
    var task = findTaskById(taskId)
        .orElseThrow(TaskNotFound::new);
    tasks.remove(task);
  }
}
