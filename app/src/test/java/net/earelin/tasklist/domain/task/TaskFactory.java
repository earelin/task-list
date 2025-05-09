package net.earelin.tasklist.domain.task;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

public final class TaskFactory {
  public static final Long TASK_ID = 7L;
  public static final String TASK_NAME = "Task Name";
  public static final String TASK_DESCRIPTION = "Task Description";
  public static final Boolean TASK_COMPLETED = true;
  public static final ZonedDateTime TASK_DEADLINE = ZonedDateTime.of(2024, 8, 23, 17, 0, 0, 0, ZoneId.of("Europe/London"));
  public static final ZonedDateTime TASK_COMPLETION_DATE = ZonedDateTime.of(2024, 8, 17, 11, 25, 19, 456, ZoneId.of("Europe/London"));
  public static final Integer TASK_ORDER = 4;
  public static final Set<String> TASK_TAGS = Set.of("tag1", "tag2", "tag3");

  public static Task createTask() {
    return new Task(TASK_ID, TASK_NAME, TASK_DESCRIPTION, TASK_COMPLETED, TASK_DEADLINE, TASK_COMPLETION_DATE, TASK_TAGS, TASK_ORDER);
  }

  private TaskFactory() {}
}
