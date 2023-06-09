package com.myprojectapi.resource.task;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.Project;
import com.myprojectapi.entity.Task;
import com.myprojectapi.entity.User;
import com.myprojectapi.resource.project.ProjectRepository;
import com.myprojectapi.resource.project.exceptions.ProjectNotFountException;
import com.myprojectapi.resource.subtask.SubtaskRepository;
import com.myprojectapi.resource.task.exception.TaskNotFoundException;
import com.myprojectapi.resource.task.exception.TaskTitleAlreadyExistedException;
import com.myprojectapi.util.PageResponse;

@Service
public record TaskService(TaskRepository taskRepo, ProjectRepository projRepo, SubtaskRepository subtaskRepo) {

	private Project validateThenGetProject(Long projectId) {
		var owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var project = projRepo.findById(projectId)
				.orElseThrow(() -> new ProjectNotFountException("not found project with id = " + projectId));
		if (!project.getOwner().equals(owner)) {
			throw new ProjectNotFountException(
					"project id " + project.getId() + " is not belong to user id" + owner.getId());
		}
		return project;
	}

	public TaskDTO create(Long projectId, TaskRequest rs) {
		var project = validateThenGetProject(projectId);
		if (taskRepo.findByBelongProjectAndTitleIgnoreCase(project, rs.title()).isPresent()) {
			throw new TaskTitleAlreadyExistedException(rs.title() + " is already in use");
		}
		Task task = rs.toTask();
		task.setBelongProject(project);
		return TaskDTO.from(taskRepo.save(task));
	}

	public PageResponse<TaskDTO> getPage(final Long projectId, int pageNum, int size, boolean isDescending, String term,
			String[] sortBy) {
		var project = validateThenGetProject(projectId);
		var sort = Sort.by(sortBy);
		if (isDescending)
			sort = sort.descending();
		Pageable page = PageRequest.of(pageNum, size, sort);

		var currPage = taskRepo.findByBelongProjectAndTitleIgnoreCaseContaining(project, term, page);
		return new PageResponse<TaskDTO>(currPage.getTotalPages(), currPage.getNumber(),
				currPage.stream().map(task -> TaskDTO.from(task, subtaskRepo.countByBelongTask(task))).toList());
	}

	public void delete(Long projectId, Long taskId) {
		var project = validateThenGetProject(projectId);
		Task task = taskRepo.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Not found task id = " + taskId));
		if (!task.getBelongProject().getId().equals(project.getId())) {
			throw new TaskNotFoundException("Not found task id = " + taskId + " in project id = " + projectId);
		}
		taskRepo.delete(task);
	}

}
