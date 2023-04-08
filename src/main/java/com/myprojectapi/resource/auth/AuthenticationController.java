package com.myprojectapi.resource.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myprojectapi.resource.user.UserRequest;

@RequestMapping("authenticate")
@Controller
public record AuthenticationController(TokenService tokenService) {
	
	@PostMapping
	public ResponseEntity<?> authenicate(@RequestBody UserRequest request){
		return ResponseEntity.ok(tokenService.authenticateThenGenerateTokens(request));
	}
	
	@PostMapping("refresh")
	public ResponseEntity<?> refreshToken(@RequestBody String refreshToken){
		return ResponseEntity.ok(tokenService.refreshAccessToken(refreshToken));
	}
}
