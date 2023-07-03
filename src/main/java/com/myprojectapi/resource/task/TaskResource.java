package com.myprojectapi.resource.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/projects/{projectId}/tasks")
public record TaskResource(TaskService taskService) {

	@PostMapping
	public ResponseEntity<?> create(@PathVariable("projectId") Long projectId,@RequestBody TaskRequest rs) {
		return ResponseEntity.ok(taskService.create(projectId,rs));
	}
	
	@GetMapping
	public ResponseEntity<?> getPage(
			@PathVariable("projectId") Long projectId,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("size") int size,
			@RequestParam("desc") boolean isDescending,
			@RequestParam("term") String term,
			@RequestParam("sortBy") String ...sortBy){
		return ResponseEntity.ok(taskService.getPage(projectId, pageNum, size, isDescending, term, sortBy));
	}
	
	@DeleteMapping("{taskId}")
	public ResponseEntity<?> delete(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId){
		taskService.delete(projectId, taskId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("{taskId}")
	public ResponseEntity<?> getOne(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId){
		return ResponseEntity.ok().body(taskService.getById(projectId, taskId));
	}
	
	@PutMapping("{taskId}")
	public ResponseEntity<?> update(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId, @RequestBody TaskRequest request){
		taskService.update(projectId, taskId, request);
		return ResponseEntity.noContent().build();
	}
	
}
