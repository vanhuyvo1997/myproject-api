package com.myprojectapi.resource.subtask;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.Subtask;
import com.myprojectapi.entity.SubtaskStatus;
import com.myprojectapi.entity.Task;
import com.myprojectapi.entity.TaskStatus;
import com.myprojectapi.entity.User;
import com.myprojectapi.resource.subtask.exceptions.SubtaskNotFoundException;
import com.myprojectapi.resource.subtask.exceptions.SubtaskTitleAlreadyExistsException;
import com.myprojectapi.resource.task.TaskRepository;
import com.myprojectapi.resource.task.exception.TaskNotFoundException;

@Service
public record SubtaskService(SubtaskRepository subtaskRepo, TaskRepository taskRepo) {

	@Deprecated
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
		if (subtaskRepo.findByBelongTaskAndTitle(task, rs.title()).isPresent()) {
			throw new SubtaskTitleAlreadyExistsException("subtask titled '" + rs.title() + "'" + " already exists");
		}
		var newSubtask = Subtask.builder().title(rs.title()).belongTask(task).build();
		updateTaskStatus(task);
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

		updateTaskStatus(task);
	}

	private void updateTaskStatus(Task task) {
		var subtasks = task.getSubtasks();
		if(subtasks.stream().allMatch(e-> e.getStatus() == SubtaskStatus.FINISHED) && subtasks.size() != 0) {
			processChangeStatusToInFinished(task, taskRepo);
		} else if(subtasks.stream().anyMatch(e-> e.getStatus() == SubtaskStatus.IN_PROGRESS || e.getStatus() == SubtaskStatus.PENDING)) {
			processChangeStatusToInProgress(task, taskRepo);
		} else {
			processChangeStatusToNew(task, taskRepo);
		}
		taskRepo.save(task);
	}
	
	private void processChangeStatusToInFinished(Task task, TaskRepository taskRepo) {
		if(task.getStatus() != TaskStatus.FINISHED) {
			var now = LocalDateTime.now();
			task.setStatus(TaskStatus.FINISHED);
			if(task.getStartedAt() == null) task.setStartedAt(now);
			task.setFinishedAt(now);
			taskRepo.save(task);
		}
	}

	private void processChangeStatusToNew(Task task, TaskRepository taskRepo){
		if(task.getStatus() != TaskStatus.NEW) {
			task.setStatus(TaskStatus.NEW);
			task.setStartedAt(null);
			task.setFinishedAt(null);
			taskRepo.save(task);
		}
	}
	private void processChangeStatusToInProgress(Task task, TaskRepository taskRepo){
		if(task.getStatus() != TaskStatus.IN_PROGRESS) {
			if(task.getStartedAt() == null) task.setStartedAt(LocalDateTime.now());
			task.setStatus(TaskStatus.IN_PROGRESS);
			taskRepo.save(task);
		}
	}

	public void delete(Long projectId, Long taskId, Long subtaskId) {
		var task = validate(projectId, taskId);
		var subtask = subtaskRepo.findById(subtaskId)
				.orElseThrow(() -> new SubtaskNotFoundException("not found subtask " + subtaskId));
		if (!subtask.getBelongTask().equals(task)) {
			throw new SubtaskNotFoundException("not found subtask " + subtaskId + "in task " + taskId);
		}
		subtaskRepo.delete(subtask);
		updateTaskStatus(task);
	}

	private Subtask validateSubtask(Long belongProjectId, Long belongTaskId,Long subtaskId){
		var subtask = subtaskRepo.findById(subtaskId).orElseThrow(()-> new SubtaskNotFoundException("Not found subtask id " + subtaskId));
		
		var task = subtask.getBelongTask();
		if(!task.getId().equals(belongTaskId)) {
			throw new SubtaskNotFoundException("Not found subtask id " + subtaskId);
		}
		
		var project = task.getBelongProject();
		if(!belongProjectId.equals(project.getId())) {
			throw new SubtaskNotFoundException("Not found subtask id " + subtaskId);
		}
		
		var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!project.getOwner().equals(user)) {
			throw new SubtaskNotFoundException("Not found subtask id " + subtaskId);
		}
		return subtask;		
	}
	public void changeTitle(Long projectId, Long taskId, Long subtaskId, String title) {
		if(title.isBlank()) throw new IllegalSubtaskStateException("title is empty");
		var subtask = validateSubtask(projectId, taskId, subtaskId);
		if(!subtask.getTitle().equals(title)) {
			subtask.setTitle(title);
			subtaskRepo.save(subtask);
		}
	}

}
