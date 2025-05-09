package net.earelin.tasklist.application.rest.task;

import net.earelin.tasklist.application.rest.task.dto.CreateTaskDto;
import net.earelin.tasklist.application.rest.task.dto.TagDto;
import net.earelin.tasklist.application.rest.task.dto.TaskDto;
import net.earelin.tasklist.application.rest.task.dto.TaskDtoMappers;
import net.earelin.tasklist.application.rest.task.dto.UpdateTaskDto;
import net.earelin.tasklist.domain.task.Task;
import net.earelin.tasklist.domain.task.TaskList;
import net.earelin.tasklist.domain.task.TaskListRepository;
import net.earelin.tasklist.domain.task.TaskListService;
import net.earelin.tasklist.domain.task.TaskNotFound;
import net.earelin.tasklist.domain.user.User;
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
@RequestMapping("/task-lists/{taskListId}/tasks")
public class TaskController {

  private final TaskListRepository taskListRepository;
  private final TaskDtoMappers taskDtoMappers;
  private final TaskListService taskListService;

  public TaskController(TaskListRepository taskListRepository,
      TaskDtoMappers taskDtoMappers, TaskListService taskListService) {
    this.taskListRepository = taskListRepository;
    this.taskDtoMappers = taskDtoMappers;
    this.taskListService = taskListService;
  }

  @PostMapping
  public ResponseEntity<TaskDto> create(@PathVariable String taskListId,
      @RequestBody CreateTaskDto taskDto,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);

    var task = taskDtoMappers.dtoToEntity(taskDto);
    var taskWithId = taskList.addTask(task);

    taskListRepository.save(taskList);

    return new ResponseEntity<>(
        taskDtoMappers.entityToDto(taskWithId), HttpStatus.CREATED);
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> get(@PathVariable String taskListId,
      @PathVariable Long taskId,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);
    var task = this.getTask(taskId, taskList);

    return ResponseEntity.ok(taskDtoMappers.entityToDto(task));
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<TaskDto> update(@PathVariable String taskListId,
      @PathVariable Long taskId,
      @RequestBody UpdateTaskDto updateTaskDto,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);
    var task = this.getTask(taskId, taskList);

    var updatedTask = taskDtoMappers.updateEntityFromDto(updateTaskDto, task);
    taskListRepository.save(taskList);

    return ResponseEntity.ok(taskDtoMappers.entityToDto(updatedTask));
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> delete(@PathVariable String taskListId,
      @PathVariable Long taskId,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);
    taskList.removeTask(taskId);

    taskListRepository.save(taskList);

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{taskId}/tags")
  public ResponseEntity<TaskDto> addTag(@PathVariable String taskListId,
      @PathVariable Long taskId,
      @RequestBody TagDto tagDto,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);
    var task = this.getTask(taskId, taskList);

    task.addTag(tagDto.tag());

    taskListRepository.save(taskList);

    return ResponseEntity.ok(taskDtoMappers.entityToDto(task));
  }

  @DeleteMapping("/{taskId}/tags/{tagName}")
  public ResponseEntity<Void> deleteTag(@PathVariable String taskListId,
      @PathVariable Long taskId,
      @PathVariable String tagName,
      @AuthenticationPrincipal User user) {
    var taskList = taskListService.getTaskList(taskListId, user);
    var task = this.getTask(taskId, taskList);

    task.removeTag(tagName);

    taskListRepository.save(taskList);

    return ResponseEntity.noContent().build();
  }

  private Task getTask(Long taskId, TaskList taskList) {
    var task = taskList.findTaskById(taskId);
    if (task.isEmpty()) {
      throw new TaskNotFound();
    }

    return task.get();
  }
}
