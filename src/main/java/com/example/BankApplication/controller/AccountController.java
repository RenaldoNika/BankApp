package com.example.BankApplication.controller;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.service.AccountService;
import com.example.BankApplication.service.BankService;
import com.example.BankApplication.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;
    private BankService bankService;
    private SmsService sendSms;

    @Autowired
    public AccountController(AccountService accountService,
                             SmsService sendSms,
                             BankService bankService){
        this.accountService=accountService;
        this.bankService=bankService;
        this.sendSms=sendSms;
    }



    @PostMapping("/withdraw/{account}")
    public Account withdraw(@PathVariable String account,@RequestParam("sum") double balance){
        bankService.withdraw(account,balance);
        return accountService.getAccount(account);
    }

    @PostMapping("/create")
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PostMapping("sms")
    public ResponseEntity<String> sendSms(@RequestParam("to") String to,
                                          @RequestParam("text") String message) {
        sendSms.sendSms(to, message);
        return ResponseEntity.ok("SMS u dërgua me sukses!");
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
    @GetMapping("/balance/{account}")
    private double getBalance(@PathVariable String account){
        return bankService.getBalance(account);
    }

    @PostMapping("/trasnfer")
    public Account transfer(@RequestParam String llogari1,@RequestParam String llogari2,
                            @RequestParam double shuma){
        bankService.transfer(llogari1,llogari2,shuma);

        bankService.getBalance(llogari1);
        Account account=bankService.getAccountBynumber(llogari1);
        return account;

    }
}
