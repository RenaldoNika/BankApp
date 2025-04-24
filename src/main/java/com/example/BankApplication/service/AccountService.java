package com.example.BankApplication.service;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository=accountRepository;
    }


    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccount(Long id) {
        return accountRepository.findById(id);
    }

    public Account updateBalance(Long id, double newBalance) {
        Account account = accountRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Account not found"));
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    public Account getAccount(String account){
        return accountRepository.findByAccountNumber(account).get();
    }


}
