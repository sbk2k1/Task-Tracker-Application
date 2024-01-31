package tech.sbk2k1.tasktracker.response.Task;

import java.util.List;

import tech.sbk2k1.tasktracker.model.TaskDTO;
import tech.sbk2k1.tasktracker.response.AbstractResponse;

public class TasksSuccess extends AbstractResponse {
 public List<TaskDTO> data;

 public TasksSuccess(String status, List<TaskDTO> data) {
  this.status = status;
  this.data = data;
 }
}