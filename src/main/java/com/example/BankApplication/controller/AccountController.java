package com.example.BankApplication.controller;

import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.DtoUserContextSpringHolder;
import com.example.BankApplication.service.AccountService;
import com.example.BankApplication.service.BankCardService;
import com.example.BankApplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;
    private BankCardService bankCardService;
    private BankService bankService;
    private DtoUserContextSpringHolder dtoUserContextSpringHolder;

    @Autowired
    public AccountController(AccountService accountService,
                             DtoUserContextSpringHolder dtoUserContextSpringHolder,
                             BankCardService bankCardService,
                             BankService bankService) {
        this.bankCardService = bankCardService;
        this.accountService = accountService;
        this.bankService = bankService;
        this.dtoUserContextSpringHolder = dtoUserContextSpringHolder;
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("account") String account,
                          @RequestParam("sum") double sum) {
        bankService.deposit(account, sum);
        return "redirect:/mybank/home";
    }


    @PostMapping("/withdraw")
    public Account withdraw(@RequestParam("account") String account,
                            @RequestParam("sum") double balance) {
        bankService.withdraw(account, balance);
        return accountService.getAccount(account);
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkCard(
            @RequestParam String cvv,
            @RequestParam String cardNumber,
            @RequestParam String expirationDate,
            @RequestParam double shuma
    ) {
        bankCardService.cardBankCheck(cvv, cardNumber, expirationDate, shuma);
        return ResponseEntity.ok("Pagesa u realizua me sukses.");
    }


    @GetMapping("/balance")
    public double getBalanceForShopin(@RequestParam String account) {
        return bankService.getBalanceForShopin(account);
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


    @PostMapping("/transfer")
    public String transfer(@RequestParam("llogari2") String llogari2,
                           @RequestParam("shuma") double shuma) {
        bankService.transfer(llogari2, shuma);

        return "redirect:/mybank/home";

    }
}

