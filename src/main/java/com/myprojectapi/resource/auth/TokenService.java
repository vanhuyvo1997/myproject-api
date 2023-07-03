package com.myprojectapi.resource.auth;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.User;

@Service
public record TokenService(
		UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager) {

	public TokenDTO authenticateThenGenerateTokens(AuthenticateRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password())
				);
		
		User user = (User) userDetailsService.loadUserByUsername(request.username());

		Date expDate = Date.from(Instant.now().plusSeconds(60 * 30));
		Map<String, Object> accessTokenClaims = TokenUtil.generateAccessTokenClaims(user);
		String accessToken = TokenUtil.generateToken(user.getUsername(), accessTokenClaims, expDate);

		Date refreshTokenExpDate = Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 7));
		Map<String, Object> refreshClaims = TokenUtil.generateRefreshTokenClaims(user);
		String refreshToken = TokenUtil.generateToken(user.getUsername(), refreshClaims, refreshTokenExpDate);
		return new TokenDTO(accessToken, refreshToken);
	}

	public TokenDTO refreshAccessToken(String refreshToken) {
		if(!TokenUtil.isRefreshToken(refreshToken)) {
			throw new InvalidTokenException("not a refresh token");
		}
		
		String username = TokenUtil.extractSubject(refreshToken);
		var user = (User) userDetailsService.loadUserByUsername(username);
		Date expDate = Date.from(Instant.now().plusSeconds(60 * 30));
		Map<String, Object> accessClaims = TokenUtil.generateAccessTokenClaims(user);
		String accessToken = TokenUtil.generateToken(user.getUsername(), accessClaims, expDate);
		return new TokenDTO(accessToken, refreshToken);
	}
	


}
