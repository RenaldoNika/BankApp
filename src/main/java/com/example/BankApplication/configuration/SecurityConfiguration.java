package com.example.BankApplication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private UserAuthenticationSuccesHandler userAuthenticationSuccesHandler;

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfiguration(UserAuthenticationSuccesHandler userAuthenticationSuccesHandler,
                                 CustomUserDetailsService customUserDetailsService) {
        this.userAuthenticationSuccesHandler = userAuthenticationSuccesHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(c -> c.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/mybank/home/**").hasRole("USER")
                        .requestMatchers("/accounts/**").hasRole("USER")
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.successHandler(userAuthenticationSuccesHandler)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .userDetailsService(customUserDetailsService);


        return httpSecurity.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
