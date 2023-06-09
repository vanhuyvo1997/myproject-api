package com.myprojectapi.resource.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.myprojectapi.resource.task.exception.TaskTitleAlreadyExistedException;

@RestControllerAdvice
public class TaskExceptionHandler {

	@ExceptionHandler({ TaskTitleAlreadyExistedException.class })
	public ResponseEntity<?> handleTaskTitleAlreadyExistedException(Exception e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Title was in use");
	}
}
