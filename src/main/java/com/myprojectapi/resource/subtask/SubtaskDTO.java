package com.myprojectapi.resource.subtask;

import java.time.LocalDateTime;

import com.myprojectapi.entity.Subtask;
import com.myprojectapi.entity.SubtaskStatus;

import lombok.Builder;

@Builder
public record SubtaskDTO(
Long id, String title,
SubtaskStatus status,
LocalDateTime createdAt,
LocalDateTime startedAt,
LocalDateTime stoppedAt,
LocalDateTime finishedAt)
{
	public static SubtaskDTO from (Subtask subtask) {
		return builder()
				.id(subtask.getId())
				.title(subtask.getTitle())
				.status(subtask.getStatus())
				.createdAt(subtask.getCreatedAt())
				.startedAt(subtask.getStartedAt())
				.stoppedAt(subtask.getStoppedAt())
				.finishedAt(subtask.getFinishedAt())
				.build();
	}
}
