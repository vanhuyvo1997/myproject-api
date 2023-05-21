package com.myprojectapi.resource.project;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.myprojectapi.resource.project.exceptions.ProjectAlreadyExistedException;
import com.myprojectapi.resource.project.exceptions.ProjectNotFountException;

@RestControllerAdvice
public class ProjectExceptionHandler {

	@ExceptionHandler(ProjectAlreadyExistedException.class)
	public ResponseEntity<?> handleProjectAlreadyExistedException(Exception e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	@ExceptionHandler(ProjectNotFountException.class)
	public ResponseEntity<?> handleProjectNotFountException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

}
