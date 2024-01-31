package tech.sbk2k1.tasktracker.model;

// imports 
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

// annotated class with @Document to be used as a document in MongoDB
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class ProjectDTO {

 // fields

 @Id
 private String id;

 @NotNull(message = "Project Name cannot be null")
 private String name;

 @NotNull(message = "Project Description cannot be null")
 private String projectDescription;

 @NotNull(message = "Project Description cannot be null")
 private String password;

 private Date createdAt;

 private Date updatedAt;
}