package com.myprojectapi.resource.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(UserAlreadyExistedException.class)
	public ResponseEntity<?> UserAlreadyExistedException(Exception e) {
		return ResponseEntity.status(403).body(e.getMessage());
	}

}
