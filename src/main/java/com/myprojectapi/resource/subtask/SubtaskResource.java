package com.myprojectapi.resource.subtask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects/{projectId}/tasks/{taskId}/subtasks")
public record SubtaskResource(SubtaskService subtaskService) {

	@PostMapping
	public ResponseEntity<?> create(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,
			@RequestBody SubtaskRequest rs) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subtaskService.create(projectId, taskId, rs));
	}

	@GetMapping
	public ResponseEntity<?> getAll(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
		return ResponseEntity.ok(subtaskService.getAll(projectId, taskId));
	}

	@PatchMapping("{subtaskId}/status")
	public ResponseEntity<?> patchStatus(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,
			@PathVariable("subtaskId") Long subtaskId,@RequestBody SubtaskRequest rs) {
		subtaskService.changeStatus(projectId, taskId, subtaskId, rs.status());
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("{subtaskId}/title")
	public ResponseEntity<?> patchTitle(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,
			@PathVariable("subtaskId") Long subtaskId, @RequestBody SubtaskRequest rs) {
		subtaskService.changeTitle(projectId, taskId, subtaskId, rs.title());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("{subtaskId}")
	public ResponseEntity<?> delete(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,
			@PathVariable("subtaskId") Long subtaskId){
		subtaskService.delete(projectId, taskId, subtaskId);
		return ResponseEntity.accepted().build();
	}
	
}
