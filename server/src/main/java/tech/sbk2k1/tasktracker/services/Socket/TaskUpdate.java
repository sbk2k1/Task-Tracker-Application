package tech.sbk2k1.tasktracker.services.Socket;

import tech.sbk2k1.tasktracker.model.TaskDTO;

public class TaskUpdate {
 public TaskDTO task;
 public String changeType;

 public TaskUpdate(TaskDTO task, String changeType) {
  this.task = task;
  this.changeType = changeType;
 }
}
