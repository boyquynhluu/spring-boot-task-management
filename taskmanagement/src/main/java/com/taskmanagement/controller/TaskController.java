package com.taskmanagement.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.dto.TaskRequest;
import com.taskmanagement.entities.Task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/tasks")
@Slf4j(topic = "TASK CONTROLLER")
@RequiredArgsConstructor
public class TaskController {

    @PostMapping
    public Task createTask(@RequestBody TaskRequest task) {
        log.info("CREATE TASK");
        log.info("Test");
        return new Task();
    }
}
