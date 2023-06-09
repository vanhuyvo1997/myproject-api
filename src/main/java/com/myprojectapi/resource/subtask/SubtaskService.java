package com.myprojectapi.resource.subtask;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.Subtask;
import com.myprojectapi.entity.SubtaskStatus;
import com.myprojectapi.entity.Task;
import com.myprojectapi.entity.User;
import com.myprojectapi.resource.subtask.exceptions.SubtaskNotFoundException;
import com.myprojectapi.resource.subtask.exceptions.SubtaskTitleAlreadyExistsException;
import com.myprojectapi.resource.task.TaskRepository;
import com.myprojectapi.resource.task.exception.TaskNotFoundException;

@Service
public record SubtaskService(SubtaskRepository subtaskRepo, TaskRepository taskRepo) {

	private Task validate(Long projectId, Long taskId) {
		var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var task = taskRepo.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("not found task has id = " + taskId));
		var belongProject = task.getBelongProject();
		if (!belongProject.getId().equals(projectId) || !belongProject.getOwner().getId().equals(user.getId())) {
			throw new TaskNotFoundException("not found task has id = " + taskId);
		}
		return task;
	}

	public SubtaskDTO create(Long projectId, Long taskId, SubtaskRequest rs) {
		var task = validate(projectId, taskId);
		if(subtaskRepo.findByBelongTaskAndTitle(task, rs.title()).isPresent()) {
			throw new SubtaskTitleAlreadyExistsException("subtask titled '" + rs.title() + "'" + " already exists");
		};
		var newSubtask = Subtask.builder().title(rs.title()).belongTask(task).build();
		return SubtaskDTO.from(subtaskRepo.save(newSubtask));
	}

	public List<SubtaskDTO> getAll(Long projectId, Long taskId) {
		var task = validate(projectId, taskId);
		var subtasks = subtaskRepo.findByBelongTask(task);
		return subtasks.stream().map(SubtaskDTO::from).toList();
	}

	public void changeStatus(Long projectId, Long taskId, Long subtaskID, SubtaskStatus status) {
		if (status == null || status.equals(SubtaskStatus.NEW)) {
			throw new IllegalArgumentException("Invalid state");
		}
		var task = validate(projectId, taskId);
		var subtask = subtaskRepo.findById(subtaskID)
				.orElseThrow(() -> new SubtaskNotFoundException("Not found subtask id = "));
		if (!task.getId().equals(subtask.getBelongTask().getId())) {
			throw new SubtaskNotFoundException("Not found subtask id = ");
		}

		if (subtask.getStatus().equals(status)) {
			return;
		}
		var now = LocalDateTime.now();
		if (status.equals(SubtaskStatus.IN_PROGRESS) && subtask.getStartedAt() == null) {
			subtask.setStartedAt(now);
		}
		if (status.equals(SubtaskStatus.IN_PROGRESS)) {
			subtask.setFinishedAt(null);
			subtask.setStoppedAt(null);
		} else if (status.equals(SubtaskStatus.PENDING)) {
			subtask.setFinishedAt(null);
			subtask.setStoppedAt(now);
		} else if (status.equals(SubtaskStatus.FINISHED)) {
			if (subtask.getStartedAt() == null)
				subtask.setStartedAt(now);
			subtask.setFinishedAt(now);
			subtask.setStoppedAt(null);
		} else {
			throw new IllegalStateException(status + "is illegal status");
		}

		subtask.setStatus(status);
		subtaskRepo.save(subtask);
	}

}
