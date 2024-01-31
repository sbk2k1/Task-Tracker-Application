package tech.sbk2k1.tasktracker.services.Project;

import org.springframework.security.core.userdetails.User;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.model.ProjectDTO;

public interface ProjectServices {
 // signup
 public String signup(ProjectDTO project) throws RuntimeException, ConstraintViolationException;

 // get project by username
 public ProjectDTO getProjectByUsername(String username) throws RuntimeException;

}
