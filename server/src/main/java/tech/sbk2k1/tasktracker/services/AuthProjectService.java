package tech.sbk2k1.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.sbk2k1.tasktracker.model.ProjectDTO;
import tech.sbk2k1.tasktracker.repository.ProjectRepository;

@Service
public class AuthProjectService implements UserDetailsService {

  @Autowired
  private ProjectRepository projectRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // get project from database
    ProjectDTO project = projectRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

    // create user details object
    UserDetails user = User.builder()
        .username(project.getUsername())
        .password(project.getPassword())
        .build();

    System.out.println("User: " + user.getUsername() + " " + user.getPassword());

    return user;
  }

}
