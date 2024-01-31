package tech.sbk2k1.tasktracker.model;

import java.util.Collection;

// imports 
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// annotated class with @Document to be used as a document in MongoDB
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "projects")
public class ProjectDTO implements UserDetails {

 // fields

 @Id
 private String id;

 @NotNull(message = "Project Name (username) cannot be null")
 private String username;

 @NotNull(message = "Password cannot be null")
 private String password;

 @Override
 public Collection<? extends GrantedAuthority> getAuthorities() {
  return null;
 }

 @Override
 public boolean isAccountNonExpired() {
  return true;
 }

 @Override
 public boolean isAccountNonLocked() {
  return true;
 }

 @Override
 public boolean isCredentialsNonExpired() {
  return true;
 }

 @Override
 public boolean isEnabled() {
  return true;
 }

}