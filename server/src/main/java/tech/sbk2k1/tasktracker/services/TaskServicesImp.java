package tech.sbk2k1.tasktracker.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.model.TaskDTO;
import tech.sbk2k1.tasktracker.repository.TaskRepository;
import tech.sbk2k1.tasktracker.response.ErrorResponse;

@Service
public class TaskServicesImp implements TaskServices {

  @Autowired
  private TaskRepository taskRepo;

  @Override
  public void CreateTask(TaskDTO task) throws RuntimeException, ConstraintViolationException {
    // check if task is already present (by title)
    Optional<TaskDTO> taskOptional = taskRepo.findByTitle(task.getTitle());
    if (taskOptional.isPresent()) {
      // if present, throw exception
      throw new RuntimeException("Task with title " + task.getTitle() + " already exists");
    } else {
      // else save the task
      task.setCreatedAt(new Date(System.currentTimeMillis()));
      taskRepo.save(task);
    }
  }

  @Override
  public List<TaskDTO> GetTasks() throws RuntimeException {
    // fetch all tasks
    List<TaskDTO> tasks = taskRepo.findAll();
    // check if tasks exist and return appropriate response
    return tasks;
  }

  @Override
  public TaskDTO UpdateTask(TaskDTO newData) throws RuntimeException, ConstraintViolationException {
    // get task by id
    Optional<TaskDTO> oldData = taskRepo.findById(newData.getId());
    // check if task exists
    if (!oldData.isPresent()) {
      throw new RuntimeException("Task with id " + newData.getId() + " not found");
    } else {

      // update or keep old data
      TaskDTO todoToSave = oldData.get();
      todoToSave.setCompleted(newData.getCompleted() != null ? newData.getCompleted() : todoToSave.getCompleted());
      todoToSave.setTitle(newData.getTitle() != null ? newData.getTitle() : todoToSave.getTitle());
      todoToSave
          .setDescription(newData.getDescription() != null ? newData.getDescription() : todoToSave.getDescription());
      todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));
      todoToSave.setUsername(newData.getUsername() != null ? newData.getUsername() : todoToSave.getUsername());

      // save data
      taskRepo.save(todoToSave);

      // return updated data
      return todoToSave;
    }
  }

  @Override
  public TaskDTO DeleteTask(String id) throws RuntimeException {
    // get task by id
    Optional<TaskDTO> task = taskRepo.findById(id);
    // check if task exists
    if (!task.isPresent()) {
      // create error message
      throw new RuntimeException("Task with id " + id + " not found");
    } else {
      // delete task
      taskRepo.deleteById(id);
      // return deleted task
      return task.get();
    }
  }

}
