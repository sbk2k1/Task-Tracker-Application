package tech.sbk2k1.tasktracker.response;

// error class that will contain futher data
public class ErrorResponse extends AbstractResponse {
 public String error;

 public ErrorResponse(String status, String error) {
  this.status = status;
  this.error = error;
 }
}
