package tech.sbk2k1.tasktracker.services.Project;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech.sbk2k1.tasktracker.model.ProjectDTO;
import tech.sbk2k1.tasktracker.repository.ProjectRepository;

@Service
public class ProjectServicesImp implements ProjectServices {

 @Autowired
 private ProjectRepository projectRepo;

 @Autowired
 private PasswordEncoder passwordEncoder;

 private Logger logger = LoggerFactory.getLogger(ProjectServicesImp.class);

 @Override
 public String signup(ProjectDTO project) throws RuntimeException {
  Optional<ProjectDTO> projectOptional = projectRepo.findByUsername(project.getUsername());

  if (projectOptional.isPresent()) {
   throw new RuntimeException("Username already exists");
  } else {
   // save project with encrypted password
   ProjectDTO toSave = new ProjectDTO();
   toSave.setUsername(project.getUsername());
   // use BCryptPasswordEncoder to encrypt password
   String encodedPassword = passwordEncoder.encode(project.getPassword());
   toSave.setPassword(encodedPassword);
   // save project
   projectRepo.save(toSave);
   return "success";
  }
 }

 @Override
 public ProjectDTO getProjectByUsername(String username) throws RuntimeException {
  Optional<ProjectDTO> projectOptional = projectRepo.findByUsername(username);
  if (projectOptional.isPresent()) {
   return projectOptional.get();
  } else {
   return null;
  }
 }

}
