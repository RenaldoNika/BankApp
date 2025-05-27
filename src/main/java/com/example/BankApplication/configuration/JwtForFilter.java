//package com.example.BankApplication.configuration;
//
//import com.example.BankApplication.jwt.JwtGenerated;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
//@Component
//public class JwtForFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtGenerated jwtGenerated;
//
//    public JwtForFilter(JwtGenerated jwtGenerated) {
//        this.jwtGenerated = jwtGenerated;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String token = null;
//
//
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("token".equals(cookie.getName()))
//                    token = cookie.getValue();
//            }
//        }
//        try {
//            if (token != null) {
//                if (jwtGenerated.isTokenExpired(token)) {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }
//
//
//                String username = jwtGenerated.extractUsername(token);
//
//                System.out.println("Username i marre nga token: " + username);
//
//                System.out.println("Authentication aktuale: " + SecurityContextHolder.getContext().getAuthentication());
//
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    if (jwtGenerated.validateToken(username, token)) {
//                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
//                                null, new ArrayList<>());
//
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                        System.out.println("Pas setAuthentication: " + SecurityContextHolder.getContext().getAuthentication());
//
//                    } else
//                        SecurityContextHolder.clearContext();
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                }
//
//            }
//
//            filterChain.doFilter(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
