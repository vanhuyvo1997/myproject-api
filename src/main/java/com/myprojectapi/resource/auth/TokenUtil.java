package com.myprojectapi.resource.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.myprojectapi.entity.User;
import static com.myprojectapi.util.TermsUtil.*;

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
		claims.put(ROLE, user.getRole());
		claims.put(ID, user.getId());
		claims.put(TYPE, ACCESS);
		return claims;
	}
	
	public static Map<String, Object> generateRefreshTokenClaims(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(TYPE, REFRESH);
		return claims;
	}

	public static boolean isValid(String token, UserDetails user) {
		Date expDate = extractExperation(token);
		return extractSubject(token).equals(user.getUsername()) 
				&& expDate.after(Date.from(Instant.now()));
	}
	
	public static boolean isAccessToken(String token) {
		return extractType(token).equals(ACCESS);
	}
	
	public static boolean isRefreshToken(String token) {
		return extractType(token).equals(REFRESH);
	}
	
	private static String extractType(String token) {
		return extractClaimByKey(token, TYPE, String.class);
	}
	
	private static <T> T extractClaimByKey(String token, String key, Class<T> clazz) {
		return extractClaims(token).get(key, clazz);
	}
}
