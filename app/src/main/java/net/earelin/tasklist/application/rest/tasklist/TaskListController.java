package net.earelin.tasklist.application.rest.tasklist;

import lombok.extern.slf4j.Slf4j;
import net.earelin.tasklist.application.rest.tasklist.dto.ModifyTaskListDto;
import net.earelin.tasklist.application.rest.tasklist.dto.TaskListDto;
import net.earelin.tasklist.application.rest.tasklist.dto.TaskListDtoMappers;
import net.earelin.tasklist.domain.task.TaskListService;
import net.earelin.tasklist.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-lists")
@Slf4j
public class TaskListController {
  private final TaskListService taskListService;
  private final TaskListDtoMappers taskListDtoMappers;

  public TaskListController(
      TaskListService taskListService,
      TaskListDtoMappers taskListDtoMappers) {
    this.taskListService = taskListService;
    this.taskListDtoMappers = taskListDtoMappers;
  }

  @GetMapping
  public ResponseEntity<Page<TaskListDto>> list(@AuthenticationPrincipal User user,
      Pageable pageable) {
    var taskList = taskListService.findAllByUser(pageable, user);

    return ResponseEntity.ok(taskListDtoMappers.entityToDtoPage(taskList));
  }

  @GetMapping("/{taskListId}")
  public ResponseEntity<TaskListDto> get(@PathVariable String taskListId,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);

    return ResponseEntity.ok(taskListDtoMappers.entityToDto(taskList));
  }

  @PostMapping
  public ResponseEntity<TaskListDto> create(@RequestBody ModifyTaskListDto createTaskListDto,
      @AuthenticationPrincipal User user) {
    var taskList = taskListDtoMappers.dtoToEntity(createTaskListDto)
        .withUser(user);
    var createdTaskList = taskListService.create(taskList);

    log.atInfo()
        .addKeyValue("taskList", createdTaskList)
        .log("Task list created: {}", createdTaskList.getId());

    return new ResponseEntity<>(
        taskListDtoMappers.entityToDto(createdTaskList), HttpStatus.CREATED);
  }

  @PutMapping("/{taskListId}")
  public ResponseEntity<TaskListDto> update(@RequestBody ModifyTaskListDto updateTaskListDto,
      @PathVariable String taskListId,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);

    var updatedTaskList = taskListDtoMappers.updateEntityFromDto(updateTaskListDto, taskList);
    taskListService.save(updatedTaskList);

    return ResponseEntity.ok(taskListDtoMappers.entityToDto(updatedTaskList));
  }

  @DeleteMapping("/{taskListId}")
  public ResponseEntity<Void> delete(@PathVariable String taskListId,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);

    taskListService.delete(taskList);

    return ResponseEntity.noContent().build();
  }
}
