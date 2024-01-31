package tech.sbk2k1.tasktracker.response;

import tech.sbk2k1.tasktracker.model.TaskDTO;

// success class that will contain futher data
public class TaskSuccess extends AbstractResponse {
 public TaskDTO data;

 public TaskSuccess(String status, TaskDTO data) {
  this.status = status;
  this.data = data;
 }
}