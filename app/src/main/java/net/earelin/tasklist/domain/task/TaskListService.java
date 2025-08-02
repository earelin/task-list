package net.earelin.tasklist.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskListService {
  private final TaskListRepository taskListRepository;

  public TaskListService(TaskListRepository taskListRepository) {
    this.taskListRepository = taskListRepository;
  }

  public TaskList getTaskList(String taskListId) {
    var taskListOptional = taskListRepository.findById(taskListId).blockOptional();
    if (taskListOptional.isEmpty()) {
      throw new TaskListNotFound();
    }

    return taskListOptional.get();
  }

  public Page<TaskList> findAll(Pageable pageable) {
    return taskListRepository.findAll(pageable);
  }

  public TaskList create(TaskList taskList) {
    return taskListRepository.save(taskList).block();
  }

  public void delete(TaskList taskList) {
    taskListRepository.deleteById(taskList.getId()).block();
  }

  public void save(TaskList updatedTaskList) {
    taskListRepository.save(updatedTaskList).block();
  }
}
