package tech.sbk2k1.tasktracker.response.Task;

import tech.sbk2k1.tasktracker.model.TaskDTO;
import tech.sbk2k1.tasktracker.response.AbstractResponse;

// success class that will contain futher data
public class TaskSuccess extends AbstractResponse {
 public TaskDTO data;

 public TaskSuccess(String status, TaskDTO data) {
  this.status = status;
  this.data = data;
 }
}