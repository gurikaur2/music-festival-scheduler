package com.gurnoor.music_event.security;



import org.springframework.stereotype.Component;



import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private static final String SECRET = "festivalSecretKey2026festivalSecretKey2026!!";
	private static final long EXPIRATION = 	86400000; //24 HOURS
	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
	public String generateToken(String username, String role)
	{
		return Jwts.builder()
				.setSubject(username)
				.claim("role" , role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token)
	{
		return getClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
