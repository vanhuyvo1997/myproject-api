package com.myprojectapi.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(length = 255)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private TaskStatus status = TaskStatus.NEW;

	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime startedAt;

	private LocalDateTime finishedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project belongProject;
	
	@OneToMany(mappedBy = "belongTask", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Subtask> subtasks;
}
