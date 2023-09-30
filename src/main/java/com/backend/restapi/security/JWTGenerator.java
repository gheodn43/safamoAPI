package com.backend.restapi.security;


import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
//import java.security.KeyPair;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JWTGenerator {
	//private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	public String generateToken(JwtPayloadCustom payloadCustom) {
	    Date currentDate = new Date();
	    Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
	    
	    String token = Jwts.builder()
	            .setSubject(payloadCustom.getUsername())
	            .claim("userId", payloadCustom.getUserId())
	            .claim("roles", payloadCustom.getRoles())
	            .setIssuedAt(new Date())
	            .setExpiration(expireDate)
	            .signWith(key, SignatureAlgorithm.HS512)
	            .compact();
	    
	    System.out.println("New token:");
	    System.out.println(token);
	    return token;
	}

	public String getUsernameFromJWT(String token){
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public Integer getUserIdFromJWT(String token) {
	    Claims claims = Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	    
	    Integer userId = claims.get("userId", Integer.class);
	    System.out.println("user id:");
	    System.out.println(userId);
	    return userId;
	}
	
	public String[] getRolesFromJWT(String token) {
	    Claims claims = Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	    return claims.get("roles", String[].class);
	}
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}
	

}
