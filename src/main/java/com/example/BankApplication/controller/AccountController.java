package com.example.BankApplication.controller;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }


    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}")
    public Account updateBalance(@PathVariable Long id, @RequestBody double newBalance) {
        return accountService.updateBalance(id, newBalance);
    }
}
