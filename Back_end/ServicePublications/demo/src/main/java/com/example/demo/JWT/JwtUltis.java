package com.example.demo.JWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

public class JwtUltis {
    private final String jwtSecret = "ecoTipsClaveSuperSeguraYlarga20030409";
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }


    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // key debe estar definido y compartido entre microservicios
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get("userId").toString());
    }
}
