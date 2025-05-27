package com.example.BankApplication.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerated jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtGenerated jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        } else {
            System.out.println("Nuk ka cookie.");
        }
        System.out.println("Cookies: " + Arrays.toString(request.getCookies()));

        if (token != null) {
            try {
                System.out.println("Token: " + token);

                if (jwtUtil.isTokenExpired(token)) {
                    System.out.println("Tokeni ka skaduar!");
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token has expired.");
                    return;
                }

                String username = jwtUtil.extractUsername(token);

                System.out.println("Authentication para setAutentikation: " + SecurityContextHolder.getContext().getAuthentication());


                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, username)) {


                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username,
                                        null, new ArrayList<>());


                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("Pas setAuthentication: " + SecurityContextHolder.getContext().getAuthentication());
                    } else {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid or expired token.");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error during token verification: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
