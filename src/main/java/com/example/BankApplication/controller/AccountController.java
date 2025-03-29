package com.example.BankApplication.controller;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.service.AccountService;
import com.example.BankApplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;
    private BankService bankService;

    @Autowired
    public AccountController(AccountService accountService,
                             BankService bankService){
        this.accountService=accountService;
        this.bankService=bankService;
    }



    @PostMapping("/withdraw/{account}")
    public Account withdraw(@PathVariable String account,@RequestParam("sum")double sum){
        bankService.withdraw(account,sum);
        return accountService.getAccount(account);
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
    public Account updateBalance(@PathVariable Long id, @RequestParam double newBalance) {
        return accountService.updateBalance(id, newBalance);
    }

    @GetMapping("/transaction/{account}")
    private List<Transaction> getTransaction(@PathVariable String account){
        return bankService.getTransactions(account);
    }
}
