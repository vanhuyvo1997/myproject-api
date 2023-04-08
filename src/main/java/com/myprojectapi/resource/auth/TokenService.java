package com.myprojectapi.resource.auth;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.User;
import com.myprojectapi.resource.user.UserRequest;
import com.myprojectapi.resource.user.UserService;

@Service
public record TokenService(UserService userService, PasswordEncoder passwordEncoder) {

	public TokenDTO authenticateThenGenerateTokens(UserRequest request) {
		User user = (User) userService.loadUserByUsername(request.username());
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new IncorrectPasswordException("incorrect password");
		}

		Date expDate = Date.from(Instant.now().plusSeconds(60 * 30));
		Map<String, Object> accessTokenClaims = TokenUtil.generateAccessTokenClaims(user);
		String accessToken = TokenUtil.generateToken(user.getUsername(), accessTokenClaims, expDate);

		Date refreshTokenExpDate = Date.from(Instant.now().plusSeconds(60 * 1));
		Map<String, Object> refreshClaims = TokenUtil.generateRefreshTokenClaims(user);
		String refreshToken = TokenUtil.generateToken(user.getUsername(), refreshClaims, refreshTokenExpDate);
		return new TokenDTO(accessToken, refreshToken);
	}

	public TokenDTO refreshAccessToken(String refreshToken) {
		String username = TokenUtil.extractSubject(refreshToken);
		var user = (User) userService.loadUserByUsername(username);
		Date expDate = Date.from(Instant.now().plusSeconds(60 * 30));
		Map<String, Object> accessClaims = TokenUtil.generateAccessTokenClaims(user);
		String accessToken = TokenUtil.generateToken(user.getUsername(), accessClaims, expDate);
		return new TokenDTO(accessToken, refreshToken);
	}
	


}
