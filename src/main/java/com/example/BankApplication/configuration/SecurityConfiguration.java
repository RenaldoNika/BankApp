package com.example.BankApplication.configuration;

import com.example.BankApplication.jwt.JwtAuthenticationFilter;
import com.example.BankApplication.jwt.JwtGenerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserAuthenticationSuccesHandler userAuthenticationSuccesHandler;
    private final JwtGenerated jwtGenerated;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfiguration(UserAuthenticationSuccesHandler userAuthenticationSuccesHandler,
                                 JwtGenerated jwtGenerated,
                                 JwtAuthenticationFilter jwtAuthenticationFilter,
                                 CustomUserDetailsService customUserDetailsService) {
        this.userAuthenticationSuccesHandler = userAuthenticationSuccesHandler;
        this.jwtGenerated = jwtGenerated;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("Security config loaded");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/mybank/home/**").authenticated()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtAuthenticationFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);


                http.formLogin(form -> form
                        .successHandler(userAuthenticationSuccesHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
