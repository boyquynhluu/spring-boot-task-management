//package com.taskmanagement.serviceimpl;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.converter.BeanOutputConverter;
//import org.springframework.stereotype.Service;
//
//import com.taskmanagement.dto.TaskDescription;
//import com.taskmanagement.service.AIService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j(topic = "AI SERVICE")
//@RequiredArgsConstructor
//public class AIServiceImpl implements AIService {
//
//    private final ChatClient.Builder builder;
//
//    @Override
//    public TaskDescription generateTaskDescription(String taskName) {
//        log.info("AI GENERATE TASK DESCTIPTION");
//
//        BeanOutputConverter<TaskDescription> converter =
//                new BeanOutputConverter<>(TaskDescription.class);
//
//        String prompt = """
//            Generate task description for: %s
//            %s
//            """.formatted(taskName, converter.getFormat());
//
//        return builder.build()
//                .prompt()
//                .user(prompt)
//                .call()
//                .entity(TaskDescription.class);
//    }
//}
