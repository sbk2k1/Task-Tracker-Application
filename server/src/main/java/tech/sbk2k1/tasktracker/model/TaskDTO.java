package tech.sbk2k1.tasktracker.model;

// imports 
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
public class TaskDTO {

  // fields

  @Id
  private String id;

  // @NotNull(message = "Project ID cannot be null")
  // private String projectId;

  @NotNull(message = "Title cannot be null")
  private String title;

  @NotNull(message = "Description cannot be null")
  private String description;

  @NotNull(message = "Completed cannot be null")
  private Boolean completed;

  @NotNull(message = "Due date cannot be null")
  private String dueDate;

  private String username;

  private Date createdAt;

  private Date updatedAt;
}