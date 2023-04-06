package com.myprojectapi.resource.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.User;
import com.myprojectapi.resource.user.UserRequest;
import com.myprojectapi.resource.user.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public record TokenService(UserService userService, PasswordEncoder passwordEncoder) {

	private static final String SECRET_CODE = "68576D5A7134743777217A25432A46294A404E635266556A586E327235753878";

	public TokenDTO authenticateThenGenerateTokens(UserRequest request) {
		User user = (User) userService.loadUserByUsername(request.username());
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new IncorrectPasswordException("incorrect password");
		}

		Date expDate = Date.from(Instant.now().plusSeconds(60 * 30));
		Map<String, Object> accessTokenClaims = new HashMap<>();
		accessTokenClaims.put("role", user.getRole());
		accessTokenClaims.put("id", user.getId());
		accessTokenClaims.put("type", "access");
		String accessToken = generateToken(user.getUsername(), accessTokenClaims, expDate);

		Date refreshTokenExpDate = Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 5));
		Map<String, Object> refreshClaims = new HashMap<>();
		refreshClaims.put("type", "refresh");
		String refreshToken = generateToken(user.getUsername(), refreshClaims, refreshTokenExpDate);
		accessTokenClaims.put("role", user.getRole());
		return new TokenDTO(accessToken, refreshToken);
	}
	

	private String generateToken(String username, Map<String, Object> claims, Date exp) {
		Instant now = Instant.now();
		return generateToken(username, claims, Date.from(now), exp, getSignKey());
	}
	
	private static String generateToken(String username, Map<String, Object> claims, Date issAt, Date exp, Key key) {
		return Jwts.builder()
				.setSubject(username)
				.addClaims(claims)
				.setIssuedAt(issAt)
				.setExpiration(exp)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_CODE));
	}

}
