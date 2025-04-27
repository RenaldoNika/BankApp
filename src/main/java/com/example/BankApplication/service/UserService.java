package com.example.BankApplication.service;

import com.example.BankApplication.exception.UserException;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.User;
import com.example.BankApplication.repository.AccountRepository;
import com.example.BankApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AccountRepository accountRepository,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).get();

    }

    public User registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserException("Ky email është tashmë i përdorur.");
        }
        String passowrdEncdoder = passwordEncoder.encode(user.getPassword());

        user.setPassword(passowrdEncdoder);
        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(0.0);
        accountRepository.save(account);
        return user;
    }
}