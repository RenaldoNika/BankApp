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

    @Autowired
    public SecurityConfiguration(UserAuthenticationSuccesHandler userAuthenticationSuccesHandler,
                                 JwtGenerated jwtGenerated,
                                 CustomUserDetailsService customUserDetailsService) {
        this.userAuthenticationSuccesHandler = userAuthenticationSuccesHandler;
        this.jwtGenerated = jwtGenerated;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/mybank/home/**").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/accounts/check").permitAll()
                        .requestMatchers("/bankCard/addCard").permitAll()
                        .requestMatchers("/bankCard/add").permitAll()
                        .requestMatchers("/bankCard/**").permitAll()
                        .requestMatchers("/mybank/auth/token").authenticated()
                        .requestMatchers("/mybank/**").authenticated()
                        .requestMatchers("/talk/**").authenticated()
                        .requestMatchers("/mybank/some-endpoint").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(userAuthenticationSuccesHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .userDetailsService(customUserDetailsService);


        http.addFilterBefore(new JwtAuthenticationFilter(jwtGenerated),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
