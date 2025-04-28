package com.example.BankApplication.controller;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.DtoUserContextSpringHolder;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.model.User;
import com.example.BankApplication.service.AccountService;
import com.example.BankApplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    private BankService bankService;
    private DtoUserContextSpringHolder dtoUserContextSpringHolder;

    @Autowired
    public AccountController(AccountService accountService,
                             DtoUserContextSpringHolder dtoUserContextSpringHolder,
                             BankService bankService) {
        this.accountService = accountService;
        this.bankService = bankService;
        this.dtoUserContextSpringHolder = dtoUserContextSpringHolder;
    }

    @PostMapping("deposit/{account}")
    public ResponseEntity<?> deposit(@PathVariable("account") String account,
                                     @RequestParam("sum") double sum) {
        bankService.deposit(account, sum);
        return ResponseEntity.ok("ok");
    }


    @PostMapping("/withdraw/{account}")
    public Account withdraw(@PathVariable String account, @RequestParam("sum") double balance) {
        bankService.withdraw(account, balance);
        return accountService.getAccount(account);
    }


    @GetMapping("/account/{id}")
    public String getAccount(@PathVariable Long id, Model model) {
        Optional<Account> account = accountService.getAccount(id);

        model.addAttribute("account", account.get());
        return "account";

    }

    @PutMapping("/{id}")
    public Account updateBalance(@PathVariable Long id, @RequestParam double newBalance) {
        return accountService.updateBalance(id, newBalance);
    }

    @GetMapping("/transaction")
    private String getTransaction(Model model) {
        User user = dtoUserContextSpringHolder.getCurrentUser();

        List<Transaction> allTransactions = new ArrayList<>();

        for (Account account : user.getAccountList()) {
            allTransactions.addAll(account.getTransactionList());
        }
        model.addAttribute("transactions", allTransactions);
        return "transactionList";
    }


    @GetMapping("/balance/{account}")
    private double getBalance(@PathVariable String account) {
        return bankService.getBalance(account);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("llogari2") String llogari2,
                           @RequestParam("shuma") double shuma) {
        bankService.transfer(llogari2, shuma);

        return "redirect:/transferoPara";

    }
}

