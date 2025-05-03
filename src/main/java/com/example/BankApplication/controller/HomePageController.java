package com.example.BankApplication.controller;

import com.example.BankApplication.jwt.JwtGenerated;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.DtoUserContextSpringHolder;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.model.User;
import com.example.BankApplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mybank")
public class HomePageController {

    private BankService bankService;
    private DtoUserContextSpringHolder dtoUserContextSpringHolder;

    private JwtGenerated jwtGenerated;

    @ResponseBody
    @GetMapping("/auth/token/{username}")
    public String getToken(@PathVariable("username") String username) {
        // Krijo tokenin për përdoruesin e dhënë
        return jwtGenerated.generateToken(username);
    }
    @Autowired
    public HomePageController(BankService bankService,
                              JwtGenerated jwtGenerated,
                              DtoUserContextSpringHolder dtoUserContextSpringHolder) {
        this.jwtGenerated=jwtGenerated;
        this.bankService = bankService;
        this.dtoUserContextSpringHolder=dtoUserContextSpringHolder;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("balance",bankService.getBalance());
        return "home";
    }


    @PostMapping("/transfero")
    public String transfero() {
        return "transferoPara";
    }

    @GetMapping("/transaction")
    private String getTransaction(Model model) {
        User user = dtoUserContextSpringHolder.getCurrentUser();

        List<Transaction> allTransactions = new ArrayList<>();

        for (Account account : user.getAccountList()) {
            allTransactions.addAll(account.getTransactionList());
        }
        model.addAttribute("transactions", allTransactions);
        return "listTransaksione";
    }


}
