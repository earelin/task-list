package net.earelin.tasklist;

import lombok.Data;

@Data
public class SharedState {
  private String taskListId;
  private Long taskId;

  public void setTaskId(String taskId) {
    this.taskId = Long.valueOf(taskId);
  }
}
