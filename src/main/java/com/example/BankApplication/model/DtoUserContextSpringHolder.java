package com.example.BankApplication.model;

import com.example.BankApplication.exception.UserException;
import com.example.BankApplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DtoUserContextSpringHolder {


    @Autowired
    private UserRepository userRepository;




    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByEmail(username).orElseThrow(()
                ->new UserException("not found"));
    }

}
