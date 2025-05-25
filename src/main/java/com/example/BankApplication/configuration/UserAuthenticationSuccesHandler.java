package com.example.BankApplication.configuration;

import com.example.BankApplication.jwt.JwtGenerated;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;


@Component
public class UserAuthenticationSuccesHandler implements AuthenticationSuccessHandler {


    private final JwtGenerated generateToken;

    @Autowired
    public UserAuthenticationSuccesHandler(JwtGenerated jwtGenerated) {
        this.generateToken = jwtGenerated;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String username = authentication.getName();
        String token = generateToken.generateToken(username);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_USER".equals(authority.getAuthority())) {
                response.sendRedirect("/mybank/home");
                return;
            }
        }
        response.sendRedirect("a");
    }
}
