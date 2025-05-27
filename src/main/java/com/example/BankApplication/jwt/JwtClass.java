package com.example.BankApplication.jwt;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtClass {

    private String key = "sekretiJuajassssssssssssssssssssssssdsffdsssssssssssssssssssssssss";


    public String gjeneroToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 100 * 10))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Date expired(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean isTokenExpired(String token) {

        Date expired = expired(token);
        return expired.before(new Date());


    }
    public boolean isValidToken(String username,String token){
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));

    }

    public static void main(String[] args) {
        JwtClass jwtClass=new JwtClass();

        String token=jwtClass.gjeneroToken("reno");


       System.out.println(jwtClass.expired(token));

    }


}
