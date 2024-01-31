package tech.sbk2k1.tasktracker.response;

// success class that will contain futher data
public class BasicResponse extends AbstractResponse {
 public String message;

 public BasicResponse(String status, String message) {
  this.status = status;
  this.message = message;
 }
}