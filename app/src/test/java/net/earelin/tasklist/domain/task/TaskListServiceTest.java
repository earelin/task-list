package net.earelin.tasklist.domain.task;

import static net.earelin.tasklist.domain.task.TaskListFactory.TASK_LIST_ID;
import static net.earelin.tasklist.domain.task.TaskListFactory.createTaskList;
import static net.earelin.tasklist.domain.user.UserFactory.createUser;
import static net.earelin.tasklist.domain.user.UserFactory.createUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskListServiceTest {

  @Mock
  private TaskListRepository taskListRepository;

  @InjectMocks
  private TaskListService taskListService;

  @Test
  void get_task_list_by_id_for_an_user() {
    final var user = createUser();
    final var taskList = createTaskList();
    when(taskListRepository.findById(TASK_LIST_ID))
        .thenReturn(Optional.of(taskList));

    var returnedTaskList = taskListService.getTaskList(TASK_LIST_ID, user);

    assertThat(returnedTaskList)
        .isEqualTo(taskList);
  }

  @Test
  void not_found_task_list_by_id_for_an_user() {
    final var user = createUser();
    when(taskListRepository.findById(TASK_LIST_ID))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> taskListService.getTaskList(TASK_LIST_ID, user))
        .isInstanceOf(TaskListNotFound.class);
  }

  @Test
  void user_is_not_task_list_owner() {
    final var user = createUserWithId("458unfalgkjds9a8sa");
    final var taskList = createTaskList();
    when(taskListRepository.findById(TASK_LIST_ID))
        .thenReturn(Optional.of(taskList));

    assertThatThrownBy(() -> taskListService.getTaskList(TASK_LIST_ID, user))
        .isInstanceOf(TaskListNotFound.class);
  }
}
