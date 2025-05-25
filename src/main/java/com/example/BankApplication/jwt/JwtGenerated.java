package com.example.BankApplication.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtGenerated {

    private String secretKey = "sekretiJuajassssssssssssssssssssssssssssssssssssssssssssssss";
    private long refreshTokenExpiration =  10 * 1000*100;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        System.out.println("Expiration date: " + expiration);
        System.out.println("Current date: " + new Date());
        return expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateToken(String token, String username) {
        System.out.println("Token: " + token);
        System.out.println("Username: " + username);
        System.out.println("Username nga tokeni: " + extractUsername(token));
        System.out.println("Token i skaduar? " + isTokenExpired(token));

        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }


}