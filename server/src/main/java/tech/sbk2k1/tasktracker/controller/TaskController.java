package tech.sbk2k1.tasktracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.model.TaskDTO;
import tech.sbk2k1.tasktracker.response.AbstractResponse;
import tech.sbk2k1.tasktracker.response.ErrorResponse;
import tech.sbk2k1.tasktracker.response.TaskSuccess;
import tech.sbk2k1.tasktracker.response.TasksSuccess;
import tech.sbk2k1.tasktracker.services.TaskServices;

@RestController
public class TaskController {

  @Autowired
  private TaskServices taskServices;

  // get all tasks (GET /tasks)
  @GetMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> getAllTasks() {
    // fetch all tasks
    List<TaskDTO> tasks = taskServices.GetTasks();
    // check if tasks exist and return appropriate response
    if (tasks.size() > 0) {
      // creating response message
      TasksSuccess data = new TasksSuccess("success", tasks);
      return new ResponseEntity<TasksSuccess>(data, HttpStatus.OK);
    } else {
      // response message for no tasks found
      ErrorResponse error = new ErrorResponse("not found", "No tasks found");
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }

  // create a task (POST /task)
  @PostMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> createTask(@RequestBody TaskDTO task) {
    try {
      // invoking create service
      taskServices.CreateTask(task);

      // creating response message
      TaskSuccess data = new TaskSuccess("success", task);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.CREATED);
    } catch (ConstraintViolationException e) {
      // create error message for validation errors
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (Exception e) {
      // create error message for other errors
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.CONFLICT);
    }
  }

  // update a task (PUT /task)
  @PutMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> updateTask(@RequestBody TaskDTO newData) {
    try {
      // invoking update service
      TaskDTO updatedTask = taskServices.UpdateTask(newData);

      // creating response message
      TaskSuccess data = new TaskSuccess("success", updatedTask);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.OK);
    } catch (ConstraintViolationException e) {
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (Exception e) {
      // create error message
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }

  // delete a task (DELETE /task/:id)
  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<? extends AbstractResponse> deleteTask(@PathVariable String id) {
    try {
      // invoking delete service
      TaskDTO deletedTask = taskServices.DeleteTask(id);
      // create success message
      TaskSuccess data = new TaskSuccess("deleted", deletedTask);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.OK);

    } catch (Exception e) {
      // create error message
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }
}
