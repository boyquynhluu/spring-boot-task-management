package com.taskmanagement.dto;

import java.util.List;

public record TaskDescription(String summary, List<String> steps) {}
