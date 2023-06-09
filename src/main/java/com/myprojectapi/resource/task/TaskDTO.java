package com.myprojectapi.resource.task;

import java.time.LocalDateTime;

import com.myprojectapi.entity.Task;
import com.myprojectapi.entity.TaskStatus;

import lombok.Builder;

@Builder
public record TaskDTO(Long id, String title, String description, TaskStatus status, LocalDateTime createdAt,
		LocalDateTime startedAt, LocalDateTime finishedAt, Integer subtasksNum) {
	static TaskDTO from(Task task) {
		return builFormTask(task).build();
	}
	
	private static TaskDTOBuilder builFormTask(Task task) {
		return builder()
				.id(task.getId())
				.title(task.getTitle())
				.description(task.getDescription())
				.createdAt(task.getCreatedAt())
				.startedAt(task.getStartedAt())
				.finishedAt(task.getFinishedAt())
				.status(task.getStatus());
	}
	
	public static TaskDTO from(Task task, Integer subtasksNum ) {
		return builFormTask(task).subtasksNum(subtasksNum).build();
	}
}
