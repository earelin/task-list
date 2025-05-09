package net.earelin.tasklist.domain.task;

import static java.util.Objects.nonNull;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
  @EqualsAndHashCode.Include
  private final Long id;

  private String name;
  private String description;
  private Boolean completed;
  private ZonedDateTime deadline;
  private ZonedDateTime completionDate;
  private final Set<String> tags = new HashSet<>();
  private Integer order;

  public Task(
      Long id,
      String name,
      String description,
      Boolean completed,
      ZonedDateTime deadline,
      ZonedDateTime completionDate,
      Set<String> tags,
      Integer order) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.completed = completed;
    this.deadline = deadline;
    this.completionDate = completionDate;
    if (nonNull(tags)) {
      this.tags.addAll(tags);
    }
    this.order = order;
  }

  public Task withId(Long id) {
    return new Task(id, name, description, completed, deadline, completionDate, tags, order);
  }

  public void addTag(String tag) {
    this.tags.add(tag);
  }

  public void removeTag(String tagName) {
    this.tags.remove(tagName);
  }

  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }
}
