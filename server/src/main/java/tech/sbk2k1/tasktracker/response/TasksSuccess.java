package tech.sbk2k1.tasktracker.response;

import java.util.List;

import tech.sbk2k1.tasktracker.model.TaskDTO;

public class TasksSuccess extends AbstractResponse {
 public List<TaskDTO> data;

 public TasksSuccess(String status, List<TaskDTO> data) {
  this.status = status;
  this.data = data;
 }
}