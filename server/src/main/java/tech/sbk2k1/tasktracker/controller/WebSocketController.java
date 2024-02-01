package tech.sbk2k1.tasktracker.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import tech.sbk2k1.tasktracker.services.Socket.TaskUpdate;

@Controller
public class WebSocketController {

 // 1. creating tasks sends back username and task details for real time update
 @SendTo("/changes")
 public TaskUpdate handleCreate(TaskUpdate data) {
  return data;
 }
}