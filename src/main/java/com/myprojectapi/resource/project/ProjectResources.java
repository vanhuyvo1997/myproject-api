package com.myprojectapi.resource.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/projects")
@AllArgsConstructor
public class ProjectResources {
	
	
	private final ProjectService projectService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ProjectRequest rq){
		return ResponseEntity.ok(projectService.create(rq));
	}
	
	@GetMapping
	public ResponseEntity<?> get(
			@RequestParam("pageNum") int pageNum,
			@RequestParam("size") int size,
			@RequestParam("desc") boolean isDescending,
			@RequestParam("term") String term,
			@RequestParam("sortBy") String ...sortBy){
		return ResponseEntity.ok(projectService.getPage(pageNum, size, isDescending, term, sortBy));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		projectService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PatchMapping("{id}/name")
	public ResponseEntity<?> updateName(
			@PathVariable("id") Long id,
			@RequestBody ProjectRequest rq){
		projectService.updateName(id, rq.name());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
