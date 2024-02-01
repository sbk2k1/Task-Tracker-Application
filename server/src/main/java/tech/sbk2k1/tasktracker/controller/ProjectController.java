package tech.sbk2k1.tasktracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolationException;
import tech.sbk2k1.tasktracker.JWT.JWTHelper;
import tech.sbk2k1.tasktracker.model.ProjectDTO;
import tech.sbk2k1.tasktracker.response.AbstractResponse;
import tech.sbk2k1.tasktracker.response.BasicResponse;
import tech.sbk2k1.tasktracker.response.ErrorResponse;
import tech.sbk2k1.tasktracker.response.User.UserLoginSuccess;
import tech.sbk2k1.tasktracker.services.Project.ProjectServices;

@RestController
public class ProjectController {

 @Autowired
 private ProjectServices projectServices;

 @Autowired
 private AuthenticationManager manager;

 @Autowired
 private JWTHelper jwtHelper;

 // / index route
 @CrossOrigin(origins = "*")
 @GetMapping("/")
 public ResponseEntity<? extends AbstractResponse> index() {
  BasicResponse data = new BasicResponse("success", "Welcome to Task Tracker API");
  return new ResponseEntity<BasicResponse>(data, HttpStatus.OK);
 }

 // create user (POST /login)
 @CrossOrigin(origins = "*")
 @PostMapping("/project/login")
 public ResponseEntity<? extends AbstractResponse> Login(@RequestBody ProjectDTO project) {
  try {

   this.doAuthenticate(project.getUsername(), project.getPassword());
   // response
   String token = this.jwtHelper.generateToken(project);
   // creating response message
   UserLoginSuccess data = new UserLoginSuccess("success", token);
   return new ResponseEntity<UserLoginSuccess>(data, HttpStatus.OK);
  } catch (ConstraintViolationException e) {
   // error message
   ErrorResponse error = new ErrorResponse("error", e.getMessage());
   return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
  } catch (RuntimeException e) {
   // error message
   ErrorResponse error = new ErrorResponse("error", e.getMessage());
   return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
 }

 private void doAuthenticate(String username, String password) {
  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
  try {
   manager.authenticate(authentication);
  } catch (Exception e) {
   throw new BadCredentialsException(e.getMessage());
  }
 }

 // signup user (POST /signup)
 @CrossOrigin(origins = "*")
 @PostMapping("/project/signup")
 public ResponseEntity<? extends AbstractResponse> Signup(@RequestBody ProjectDTO project) {
  try {
   // fetch all tasks
   String status = projectServices.signup(project);
   // creating response message
   BasicResponse data = new BasicResponse(status, "Account Created");
   return new ResponseEntity<BasicResponse>(data, HttpStatus.OK);
  } catch (ConstraintViolationException e) {
   // error message
   ErrorResponse error = new ErrorResponse("error", e.getMessage());
   return new ResponseEntity<ErrorResponse>(error, HttpStatus.UNPROCESSABLE_ENTITY);
  } catch (RuntimeException e) {
   // error message
   ErrorResponse error = new ErrorResponse("error", e.getMessage());
   return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
 }

}
