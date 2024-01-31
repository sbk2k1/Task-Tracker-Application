package tech.sbk2k1.tasktracker.response;

// success class that will contain futher data
public class UserLoginSuccess extends AbstractResponse {
 public String token;

 public UserLoginSuccess(String status, String data) {
  this.status = status;
  this.token = data;
 }
}