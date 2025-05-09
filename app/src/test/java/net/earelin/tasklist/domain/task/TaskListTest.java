package net.earelin.tasklist.domain.task;

import static net.earelin.tasklist.domain.task.TaskFactory.TASK_ID;
import static net.earelin.tasklist.domain.task.TaskFactory.createTask;
import static net.earelin.tasklist.domain.task.TaskListFactory.TASK_LIST_DESCRIPTION;
import static net.earelin.tasklist.domain.task.TaskListFactory.TASK_LIST_ID;
import static net.earelin.tasklist.domain.task.TaskListFactory.TASK_LIST_NAME;
import static net.earelin.tasklist.domain.user.UserFactory.createUser;
import static net.earelin.tasklist.domain.user.UserFactory.createUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.earelin.tasklist.domain.user.User;

class TaskListTest {
  private User user;
  private TaskList taskList;
  private Set<Task> tasks;

  @BeforeEach
  void setUp() {
    tasks = Set.of(createTask());
    user = createUser();
    taskList = new TaskList(
        TASK_LIST_ID,
        user,
        TASK_LIST_NAME,
        TASK_LIST_DESCRIPTION,
        tasks
    );
  }

  @Test
  void create_task_list() {
    assertThat(taskList)
        .satisfies(
            tl -> assertThat(tl.getId()).isEqualTo(TASK_LIST_ID),
            tl -> assertThat(tl.getUser()).isEqualTo(user),
            tl -> assertThat(tl.getName()).isEqualTo(TASK_LIST_NAME),
            tl -> assertThat(tl.getDescription()).isEqualTo(TASK_LIST_DESCRIPTION),
            tl -> assertThat(tl.getTasks()).isEqualTo(tasks)
        );
  }

  @Test
  void create_task_list_with_null_tasks() {
    taskList = new TaskList(
        TASK_LIST_ID,
        user,
        TASK_LIST_NAME,
        TASK_LIST_DESCRIPTION,
        null
    );

    assertThat(taskList)
        .satisfies(
            tl -> assertThat(tl.getId()).isEqualTo(TASK_LIST_ID),
            tl -> assertThat(tl.getUser()).isEqualTo(user),
            tl -> assertThat(tl.getName()).isEqualTo(TASK_LIST_NAME),
            tl -> assertThat(tl.getDescription()).isEqualTo(TASK_LIST_DESCRIPTION),
            tl -> assertThat(tl.getTasks()).isEmpty()
        );
  }

  @Test
  void create_task_list_with_an_updated_id() {
    final var newId = "newId";

    final var updatedTaskList = taskList.withId(newId);

    assertThat(updatedTaskList)
        .satisfies(
            tl -> assertThat(tl.getId()).isEqualTo(newId),
            tl -> assertThat(tl.getUser()).isEqualTo(user),
            tl -> assertThat(tl.getName()).isEqualTo(TASK_LIST_NAME),
            tl -> assertThat(tl.getDescription()).isEqualTo(TASK_LIST_DESCRIPTION),
            tl -> assertThat(tl.getTasks()).isEqualTo(tasks)
        );
  }

  @Test
  void create_task_list_with_an_updated_user() {
    final var newUserId = "3jlkfjdsf8jdsf";
    final var newUser = createUserWithId(newUserId);

    final var updatedTaskList = taskList.withUser(newUser);

    assertThat(updatedTaskList)
        .satisfies(
            tl -> assertThat(tl.getId()).isEqualTo(TASK_LIST_ID),
            tl -> assertThat(tl.getUser()).isEqualTo(newUser),
            tl -> assertThat(tl.getName()).isEqualTo(TASK_LIST_NAME),
            tl -> assertThat(tl.getDescription()).isEqualTo(TASK_LIST_DESCRIPTION),
            tl -> assertThat(tl.getTasks()).isEqualTo(tasks)
        );
  }

  @Test
  void find_task_by_id() {
    var task = taskList.findTaskById(TASK_ID);

    assertThat(task)
        .isPresent()
        .get()
        .satisfies(t -> assertThat(t.getId()).isEqualTo(TASK_ID));
  }

  @Test
  void find_task_by_id_not_found() {
    var task = taskList.findTaskById(999L);

    assertThat(task)
        .isNotPresent();
  }

  @Test
  void add_task() {
    var task = createTask();

    var addedTask = taskList.addTask(task);

    assertThat(taskList.getTasks())
        .hasSize(2)
        .contains(addedTask);
  }

  @Test
  void remove_task() {
    var task = createTask();
    var addedTask = taskList.addTask(task);

    taskList.removeTask(addedTask.getId());

    assertThat(taskList.getTasks())
        .hasSize(1)
        .doesNotContain(addedTask);
  }

  @Test
  void remove_task_not_found() {
    assertThatThrownBy(() -> taskList.removeTask(999L))
        .isInstanceOf(TaskNotFound.class);
  }
}
