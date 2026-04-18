//package com.taskmanagement.serviceimpl;
//
//import org.springframework.stereotype.Service;
//
//import com.taskmanagement.dto.TaskDescription;
//import com.taskmanagement.entities.Task;
//import com.taskmanagement.repositories.TaskRepository;
//import com.taskmanagement.service.AIService;
//import com.taskmanagement.service.TaskService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j(topic = "TASK SERVICE")
//public class TaskServiceImpl implements TaskService {
//
//    private final TaskRepository taskRepository;
//    private final AIService aiService;
//
//    @Override
//    public Task createTask(String name) {
//        log.info("TASK SERVICE");
//
//        // gọi AI generate description
//        TaskDescription description = aiService.generateTaskDescription(name);
//
//        Task task = new Task();
//        task.setTitle(name);
//        task.setDescription(description.summary() + "\nSteps: " + String.join(", ", description.steps()));
//
//        return taskRepository.save(task);
//    }
//
//}
