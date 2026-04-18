package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDescription;

public interface AIService {

    TaskDescription generateTaskDescription(String taskName);
}
