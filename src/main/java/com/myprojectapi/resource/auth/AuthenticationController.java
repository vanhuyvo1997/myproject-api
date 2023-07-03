package com.myprojectapi.resource.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myprojectapi.resource.user.UserRequest;
import com.myprojectapi.resource.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("api/auth")
@Controller
public record AuthenticationController(TokenService tokenService, UserService userService) {
	
	@PostMapping
	public ResponseEntity<?> authenicate(@RequestBody AuthenticateRequest request){
		return ResponseEntity.ok(tokenService.authenticateThenGenerateTokens(request));
	}
	
	@GetMapping("refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request){
		return ResponseEntity.ok(tokenService.refreshAccessToken(request.getHeader("Authorization").substring(7)));
	}
	
	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody UserRequest request){
		return ResponseEntity.ok(userService.createNew(request));
	}
}
