package com.myprojectapi.resource.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.myprojectapi.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TokenUtil {
	private static final String SECRET_CODE = "68576D5A7134743777217A25432A46294A404E635266556A586E327235753878";

	public static Key getSignKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_CODE));
	}

	private static String generateToken(String username, Map<String, Object> claims, String issuer, Date issAt,
			Date exp, Key key) {
		return Jwts.builder()
				.setSubject(username)
				.addClaims(claims)
				.setIssuer(issuer)
				.setIssuedAt(issAt)
				.setExpiration(exp)
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public static String generateToken(String username, Map<String, Object> claims, Date exp) {
		Instant now = Instant.now();
		return generateToken(username, claims, null, Date.from(now), exp, getSignKey());
	}
	
	private static Claims extractClaims(String token) {
		var jwtBody = Jwts.parserBuilder().setSigningKey(TokenUtil.getSignKey()).build().parse(token).getBody();
		if (jwtBody instanceof Claims claims) {
			return claims;
		}
		throw new InvalidTokenException("invalid token");
	}
	
	public static String extractSubject(String token) {
		return extractClaims(token).getSubject();
	}
	
	public static Date extractExperation(String token) {
		return extractClaims(token).getExpiration();
	}
	
	public static Map<String, Object> generateAccessTokenClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole());
		claims.put("id", user.getId());
		claims.put("type", "access");
		return claims;
	}
	
	public static Map<String, Object> generateRefreshTokenClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", "refresh");
		return claims;
	}

	public static boolean isValid(String token, UserDetails user) {
		Date expDate = extractExperation(token);
		return extractSubject(token).equals(user.getUsername()) 
				&& expDate.after(Date.from(Instant.now()));
	}
}
