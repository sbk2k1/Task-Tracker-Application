package tech.sbk2k1.tasktracker.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
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
import tech.sbk2k1.tasktracker.repository.TaskRepository;

import tech.sbk2k1.tasktracker.response.AbstractResponse;
import tech.sbk2k1.tasktracker.response.ErrorResponse;
import tech.sbk2k1.tasktracker.response.TaskSuccess;
import tech.sbk2k1.tasktracker.response.TasksSuccess;
import tech.sbk2k1.tasktracker.services.TaskServices;

@RestController
public class ProjectController {

 @Autowired
 private TaskRepository taskRepo;

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
}
