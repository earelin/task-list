package net.earelin.tasklist.domain.task;

import static java.util.Objects.nonNull;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collectionName = "task_lists")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class TaskList {
  @DocumentId
  @EqualsAndHashCode.Include
  private final String id;

  @Setter
  private String name;

  @Setter
  private String description;

  private final Set<Task> tasks = new HashSet<>();

  public TaskList(String id, String name, String description, Set<Task> tasks) {
    this.id = id;
    this.name = name;
    this.description = description;
    if (nonNull(tasks)) {
      this.tasks.addAll(tasks);
    }
  }

  public TaskList withId(String id) {
    return new TaskList(id, name, description, tasks);
  }

  public TaskList withUserId(String userId) {
    return new TaskList(id, name, description, tasks);
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
