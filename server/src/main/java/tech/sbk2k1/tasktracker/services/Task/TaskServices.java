package tech.sbk2k1.tasktracker.services.Task;

import java.util.List;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.model.TaskDTO;

public interface TaskServices {

 // create
 public void CreateTask(TaskDTO task) throws RuntimeException, ConstraintViolationException;

 // read
 public List<TaskDTO> GetTasks() throws RuntimeException;

 // update
 public TaskDTO UpdateTask(TaskDTO task) throws RuntimeException, ConstraintViolationException;

 // delete
 public TaskDTO DeleteTask(String id) throws RuntimeException;

}
