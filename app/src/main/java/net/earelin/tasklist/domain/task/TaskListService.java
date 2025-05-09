package net.earelin.tasklist.domain.task;

import net.earelin.tasklist.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskListService {
  private final TaskListRepository taskListRepository;

  public TaskListService(TaskListRepository taskListRepository) {
    this.taskListRepository = taskListRepository;
  }

  public TaskList getTaskList(String taskListId, User user) {
    var taskListOptional = taskListRepository.findById(taskListId);
    if (taskListOptional.isEmpty()) {
      throw new TaskListNotFound();
    }

    var taskList = taskListOptional.get();
    if (!taskList.getUser().equals(user)) {
      throw new TaskListNotFound();
    }

    return taskList;
  }

  public Page<TaskList> findAllByUser(Pageable pageable, User user) {
    return taskListRepository.findAllByUser(pageable, user);
  }

  public TaskList create(TaskList taskList) {
    return taskListRepository.insert(taskList);
  }

  public void delete(TaskList taskList) {
    taskListRepository.deleteById(taskList.getId());
  }

  public void save(TaskList updatedTaskList) {
    taskListRepository.save(updatedTaskList);
  }
}
