package tech.sbk2k1.tasktracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.model.TaskDTO;
import tech.sbk2k1.tasktracker.response.AbstractResponse;
import tech.sbk2k1.tasktracker.response.ErrorResponse;
import tech.sbk2k1.tasktracker.response.Task.TaskSuccess;
import tech.sbk2k1.tasktracker.response.Task.TasksSuccess;
import tech.sbk2k1.tasktracker.services.Task.TaskServices;

@RestController
public class TaskController {

  private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

  @Autowired
  private TaskServices taskServices;

  // get all tasks (GET /tasks)
  @CrossOrigin(origins = "*")
  @GetMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> getAllTasks(@RequestParam int pageNumber,
      @RequestParam int pageSize) {
    // fetch all tasks
    List<TaskDTO> tasks = taskServices.GetTasks(pageNumber, pageSize);
    // check if tasks exist and return appropriate response
    if (tasks.size() > 0) {

      // log httpmethod route params and status
      logger.info("GET /tasks?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "success");

      // creating response message
      TasksSuccess data = new TasksSuccess("success", tasks);
      return new ResponseEntity<TasksSuccess>(data, HttpStatus.OK);
    } else {

      // log httpmethod route params and status
      logger.info("GET /tasks?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "not_found");

      // response message for no tasks found
      ErrorResponse error = new ErrorResponse("not found", "No tasks found");
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }

  // the cross origin annotation here is not working because of a bug in spring
  //

  // create a task (POST /task)
  @CrossOrigin(origins = "*")
  @PostMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> createTask(@RequestBody TaskDTO task) {
    try {
      // invoking create service
      taskServices.CreateTask(task);

      // log httpmethod route params and status
      logger.info("POST /tasks success");

      // creating response message
      TaskSuccess data = new TaskSuccess("success", task);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.CREATED);
    } catch (ConstraintViolationException e) {

      // log httpmethod route params and status
      logger.info("POST /tasks unprocessable_entity");

      // create error message for validation errors
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (Exception e) {

      // log httpmethod route params and status
      logger.info("POST /tasks conflict");

      // create error message for other errors
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.CONFLICT);
    }
  }

  // update a task (PUT /task)
  @CrossOrigin(origins = "*")
  @PutMapping("/tasks")
  public ResponseEntity<? extends AbstractResponse> updateTask(@RequestBody TaskDTO newData) {
    try {
      // invoking update service
      TaskDTO updatedTask = taskServices.UpdateTask(newData);

      // log httpmethod route params and status
      logger.info("PUT /tasks success");

      // creating response message
      TaskSuccess data = new TaskSuccess("success", updatedTask);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.OK);
    } catch (ConstraintViolationException e) {

      // log httpmethod route params and status
      logger.info("PUT /tasks unprocessable_entity");

      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (Exception e) {

      // log httpmethod route params and status
      logger.info("PUT /tasks not_found");

      // create error message
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }

  // delete a task (DELETE /task/:id)
  @CrossOrigin(origins = "*")
  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<? extends AbstractResponse> deleteTask(@PathVariable String id) {
    try {
      // invoking delete service
      TaskDTO deletedTask = taskServices.DeleteTask(id);

      // log httpmethod route params and status
      logger.info("DELETE /tasks/" + id + " success");

      // create success message
      TaskSuccess data = new TaskSuccess("deleted", deletedTask);
      return new ResponseEntity<TaskSuccess>(data, HttpStatus.OK);

    } catch (Exception e) {

      // log httpmethod route params and status
      logger.info("DELETE /tasks/" + id + " not_found");

      // create error message
      ErrorResponse error = new ErrorResponse("error", e.getMessage());
      return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
    }
  }
}
