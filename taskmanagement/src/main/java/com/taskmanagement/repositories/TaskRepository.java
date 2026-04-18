package com.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanagement.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
