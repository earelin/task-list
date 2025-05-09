package net.earelin.tasklist.domain.task;

import static net.earelin.tasklist.domain.task.TaskFactory.TASK_COMPLETED;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_COMPLETION_DATE;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_DEADLINE;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_DESCRIPTION;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_ID;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_NAME;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_ORDER;
import static net.earelin.tasklist.domain.task.TaskFactory.TASK_TAGS;
import static net.earelin.tasklist.domain.task.TaskFactory.createTask;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {

  private Task task;

  @BeforeEach
  void setUp() {
    task = createTask();
  }

  @Test
  void create_task() {
    assertThat(task)
        .satisfies(
            t -> assertThat(t.getId()).isEqualTo(TASK_ID),
            t -> assertThat(t.getName()).isEqualTo(TASK_NAME),
            t -> assertThat(t.getDescription()).isEqualTo(TASK_DESCRIPTION),
            t -> assertThat(t.getCompleted()).isEqualTo(TASK_COMPLETED),
            t -> assertThat(t.getDeadline()).isEqualTo(TASK_DEADLINE),
            t -> assertThat(t.getCompletionDate()).isEqualTo(TASK_COMPLETION_DATE),
            t -> assertThat(t.getTags()).containsExactlyInAnyOrderElementsOf(TASK_TAGS),
            t -> assertThat(t.getOrder()).isEqualTo(TASK_ORDER)
        );
  }

  @Test
  void create_task_with_null_tags() {
    task = new Task(
        TASK_ID,
        TASK_NAME,
        TASK_DESCRIPTION,
        TASK_COMPLETED,
        TASK_DEADLINE,
        TASK_COMPLETION_DATE,
        null,
        TASK_ORDER
    );

    assertThat(task)
        .satisfies(
            t -> assertThat(t.getId()).isEqualTo(TASK_ID),
            t -> assertThat(t.getName()).isEqualTo(TASK_NAME),
            t -> assertThat(t.getDescription()).isEqualTo(TASK_DESCRIPTION),
            t -> assertThat(t.getCompleted()).isEqualTo(TASK_COMPLETED),
            t -> assertThat(t.getDeadline()).isEqualTo(TASK_DEADLINE),
            t -> assertThat(t.getCompletionDate()).isEqualTo(TASK_COMPLETION_DATE),
            t -> assertThat(t.getTags()).isNotNull().isEmpty(),
            t -> assertThat(t.getOrder()).isEqualTo(TASK_ORDER)
        );
  }

  @Test
  void create_a_task_with_an_updated_id() {
    final var newId = 8L;
    var taskWithUpdatedId = task.withId(newId);

    assertThat(taskWithUpdatedId)
        .satisfies(
            t -> assertThat(t.getId()).isEqualTo(newId),
            t -> assertThat(t.getName()).isEqualTo(TASK_NAME),
            t -> assertThat(t.getDescription()).isEqualTo(TASK_DESCRIPTION),
            t -> assertThat(t.getCompleted()).isEqualTo(TASK_COMPLETED),
            t -> assertThat(t.getDeadline()).isEqualTo(TASK_DEADLINE),
            t -> assertThat(t.getCompletionDate()).isEqualTo(TASK_COMPLETION_DATE),
            t -> assertThat(t.getTags()).containsExactlyInAnyOrderElementsOf(TASK_TAGS),
            t -> assertThat(t.getOrder()).isEqualTo(TASK_ORDER)
        );
  }

  @Test
  void add_a_tag_to_the_task() {
    final var newTag = "tag4";

    task.addTag(newTag);

    assertThat(task.getTags())
        .containsOnly("tag1", "tag2", "tag3", newTag);
  }

  @Test
  void remove_a_tag_from_the_task() {
    final var tagToRemove = "tag2";

    task.removeTag(tagToRemove);

    assertThat(task.getTags())
        .containsOnly("tag1", "tag3");
  }
}
