//package com.taskmanagement.controller;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.taskmanagement.dto.TaskRequest;
//import com.taskmanagement.entities.Task;
//import com.taskmanagement.service.TaskService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/api/tasks")
//@Slf4j(topic = "TASK CONTROLLER")
//@RequiredArgsConstructor
//public class TaskController {
//
//    private final TaskService taskService;
//
//    @PostMapping
//    public Task createTask(@RequestBody TaskRequest task) {
//        log.info("CREATE TASK");
//        return taskService.createTask(task.getTitle());
//    }
//}
