package com.example.BankApplication.controller;

import com.example.BankApplication.jwt.JwtGenerated;
import com.example.BankApplication.model.Account;
import com.example.BankApplication.model.DtoUserContextSpringHolder;
import com.example.BankApplication.model.Transaction;
import com.example.BankApplication.model.User;
import com.example.BankApplication.service.BankService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @GetMapping("/auth/token")
    public String getToken() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Përdoruesi nuk është i autentikuar");
        }
        String username = authentication.getName();
        System.out.println(jwtGenerated.generateToken(username));
        return jwtGenerated.generateToken(username);

    }

    @Autowired
    public HomePageController(BankService bankService,
                              JwtGenerated jwtGenerated,
                              DtoUserContextSpringHolder dtoUserContextSpringHolder) {
        this.jwtGenerated = jwtGenerated;
        this.bankService = bankService;
        this.dtoUserContextSpringHolder = dtoUserContextSpringHolder;
    }

    @PostMapping("/some-endpoint")
    @ResponseBody
    public String getRequestHeaders(@RequestBody String requestBody, HttpServletRequest request) {
        String contentType = request.getHeader("Accept");
        return "Content-Type: " + contentType + ", Body: " + requestBody;
    }


    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Përdoruesi nuk është i autentikuar");
        }
        String username = authentication.getName();

        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null) {
            System.out.println("Tokeni i përdoruesit nga cookie: " + token);
        } else {
            System.out.println("Nuk u gjet token në cookie.");
        }

        System.out.println("Emri i përdoruesit: " + username);

        model.addAttribute("username",username);
        model.addAttribute("balance", bankService.getBalance());
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
