package com.example.BankApplication.controller;

import com.example.BankApplication.jwt.JwtGenerated;
import com.example.BankApplication.model.*;
import com.example.BankApplication.repository.UserRepository;
import com.example.BankApplication.service.BankService;
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
    private UserRepository userRepository;

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
                              UserRepository userRepository,
                              JwtGenerated jwtGenerated,
                              DtoUserContextSpringHolder dtoUserContextSpringHolder) {
        this.userRepository = userRepository;
        this.jwtGenerated = jwtGenerated;
        this.bankService = bankService;
        this.dtoUserContextSpringHolder = dtoUserContextSpringHolder;
    }


    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Përdoruesi nuk është i autentikuar");
        }
        String username = authentication.getName();

        User user = userRepository.findByEmail(username).get();

        List<String> allTransactions = new ArrayList<>();

        for (Account account : user.getAccountList()) {
            allTransactions.add(account.getAccountNumber());
        }

        model.addAttribute("accounts", allTransactions);
        model.addAttribute("username", username);
        model.addAttribute("balance", bankService.getBalance());
        return "home";
    }

    @PostMapping("/deposit")
    public String deposit() {
        return "deposit";
    }

    @PostMapping("/transfero")
    public String transfero() {
        return "transferoPara";
    }

    @GetMapping("/transaction")
    public String getTransaction(Model model) {
        User user = dtoUserContextSpringHolder.getCurrentUser();
        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account : user.getAccountList()) {
            allTransactions.addAll(account.getTransactionList());
        }
        model.addAttribute("transactions", allTransactions);
        return "listTransaksione";
    }

    @GetMapping("/bankCard")
    public String getBankCard(Model model) {
        User user = dtoUserContextSpringHolder.getCurrentUser();

        BankCard bankCard = user.getAccountList().get(0).getBankCard();

        if (bankCard == null) {
            model.addAttribute("errorMessage", "Nuk u gjet kartë bankare për këtë llogari.");
            return "errorBankCard";
        }


        model.addAttribute("bankCard", bankCard);
        return "showBankCredit";
    }

    @GetMapping("/start/chat")
    public String startChat(@RequestParam("username") String username) {
        return "redirect:/talk/chats/" + username;
    }
}
