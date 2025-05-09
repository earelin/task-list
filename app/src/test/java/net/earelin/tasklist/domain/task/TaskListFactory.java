package net.earelin.tasklist.domain.task;

import static net.earelin.tasklist.domain.task.TaskFactory.createTask;
import static net.earelin.tasklist.domain.user.UserFactory.createUser;

import java.util.Set;

import net.earelin.tasklist.domain.user.User;

public final class TaskListFactory {
  public static final String TASK_LIST_ID = "sdf98jhd9a8f8kjdsaf";
  public static final String TASK_LIST_NAME = "My Task List";
  public static final String TASK_LIST_DESCRIPTION = "My Task List Description";

  public static TaskList createTaskList() {
    var user = createUser();
    var tasks = Set.of(createTask());
    return createTaskListWithUserAndTasks(user, tasks);
  }

  public static TaskList createTaskListWithUserAndTasks(User user, Set<Task> tasks) {
    return new TaskList(
        TASK_LIST_ID,
        user,
        TASK_LIST_NAME,
        TASK_LIST_DESCRIPTION,
        tasks
    );
  }

  private TaskListFactory() {}
}
