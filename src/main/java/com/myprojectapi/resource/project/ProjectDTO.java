package com.myprojectapi.resource.project;

import java.time.LocalDateTime;

import com.myprojectapi.entity.Project;
import com.myprojectapi.entity.ProjectStatus;

public record ProjectDTO(Long id, String name, LocalDateTime createdAt, ProjectStatus status, Long ownerId) {

	public static ProjectDTO from(Project p) {
		return new ProjectDTO(p.getId(), p.getName(), p.getStartedAt(), p.getStatus(), p.getOwner().getId());
	}

}
