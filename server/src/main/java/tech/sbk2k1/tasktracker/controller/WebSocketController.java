package tech.sbk2k1.tasktracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import tech.sbk2k1.tasktracker.services.Socket.TaskUpdate;

@Controller
public class WebSocketController {

 private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

 // 1. creating tasks sends back username and task details for real time update
 @SendTo("/changes")
 public TaskUpdate handleCreate(TaskUpdate data) {

  // log
  logger.info("Socket /changes published");

  return data;
 }
}