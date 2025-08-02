package net.earelin.tasklist.domain.task;

import static net.earelin.tasklist.domain.task.TaskListFactory.TASK_LIST_ID;
import static net.earelin.tasklist.domain.task.TaskListFactory.createTaskList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Mono;

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
  void get_task_list_by_id() {
    final var taskList = createTaskList();
    when(taskListRepository.findById(TASK_LIST_ID))
        .thenReturn(Mono.just(taskList));

    var returnedTaskList = taskListService.getTaskList(TASK_LIST_ID);

    assertThat(returnedTaskList)
        .isEqualTo(taskList);
  }

  @Test
  void not_found_task_list_by_idr() {
    when(taskListRepository.findById(TASK_LIST_ID))
        .thenReturn(Mono.empty());

    assertThatThrownBy(() -> taskListService.getTaskList(TASK_LIST_ID))
        .isInstanceOf(TaskListNotFound.class);
  }
}
