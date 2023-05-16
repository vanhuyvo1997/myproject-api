package com.myprojectapi.resource.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
			@RequestParam("sortBy") String ...sortBy){
		return ResponseEntity.ok(projectService.getPage(pageNum, size, isDescending, sortBy));
	}
}
