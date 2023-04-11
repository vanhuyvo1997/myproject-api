package com.myprojectapi.resource.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myprojectapi.resource.user.UserRequest;
import com.myprojectapi.resource.user.UserService;

@RequestMapping("api/auth")
@Controller
public record AuthenticationController(TokenService tokenService, UserService userService) {
	
	@PostMapping
	public ResponseEntity<?> authenicate(@RequestBody AuthenticateRequest request){
		return ResponseEntity.ok(tokenService.authenticateThenGenerateTokens(request));
	}
	
	@PostMapping("refresh")
	public ResponseEntity<?> refreshToken(@RequestBody String refreshToken){
		return ResponseEntity.ok(tokenService.refreshAccessToken(refreshToken));
	}
	
	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody UserRequest request){
		return ResponseEntity.ok(userService.createNew(request));
	}
}
