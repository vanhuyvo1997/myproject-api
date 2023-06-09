package com.myprojectapi.resource.subtask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.myprojectapi.resource.subtask.exceptions.SubtaskNotFoundException;
import com.myprojectapi.resource.subtask.exceptions.SubtaskTitleAlreadyExistsException;

@RestControllerAdvice
public class SubtaskExceptionHandler {
	
	@ExceptionHandler({SubtaskTitleAlreadyExistsException.class})
	public ResponseEntity<?> handleSubtaskAlreadyExistsException(Exception e){
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	@ExceptionHandler({SubtaskNotFoundException.class})
	public ResponseEntity<?> handleSubtaskNotFoundException(Exception e){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
