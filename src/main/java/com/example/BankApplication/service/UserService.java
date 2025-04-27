package com.example.BankApplication.service;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.User;
import com.example.BankApplication.repository.AccountRepository;
import com.example.BankApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private AccountRepository accountRepository;
    private UserRepository userRepository;

    @Autowired
    public UserService(AccountRepository accountRepository,
                       UserRepository userRepository){
        this.accountRepository=accountRepository;
        this.userRepository=userRepository;
    }

    public User getUser(Long id){
        return userRepository.findById(id).get();

    }

    public User registerUser(User user) {

        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(0.0);
        accountRepository.save(account);

        return user;
    }
}