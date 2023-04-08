package com.myprojectapi.resource.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class AuthenticationExceptionHandler {

	@ExceptionHandler({ 
		IncorrectPasswordException.class,
		UsernameNotFoundException.class,
		ExpiredJwtException.class,
		InvalidTokenException.class})
	public ResponseEntity<?> incorrectAuthenticatedInfor(Exception e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}

}
