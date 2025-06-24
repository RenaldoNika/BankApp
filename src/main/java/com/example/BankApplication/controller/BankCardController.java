package com.example.BankApplication.controller;


import com.example.BankApplication.model.BankCard;
import com.example.BankApplication.service.BankCardService;
import com.example.BankApplication.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/bankCard")
public class BankCardController {

    private BankCardService bankCardService;
    private BankService bankService;

    @Autowired
    public BankCardController(BankCardService bankCardService,
    BankService bankService){
        this.bankCardService=bankCardService;
        this.bankService=bankService;
    }


    @GetMapping("/addCard")
    public String showBankCardForm(Model model) {
        model.addAttribute("bankCard", new BankCard());

//       String abc= request.getRequestURI();
//       System.out.println("=========== "+abc+" ================");
        return "bankCredit";
    }

    @PostMapping("/add")
    public String addBankCard(@ModelAttribute BankCard bankCard,
                              @RequestParam String accountNumber,
                              Model model) {
        bankCardService.addBankCard(bankCard, accountNumber);
        model.addAttribute("message", "Karta u shtua me sukses!");
        return "redirect:/bankCard/addCard";
    }

    @PostMapping("/transfero/onlineshop")
    public String blerje(@RequestParam String llogariaOnline,
                         @RequestParam ("cvv")String cvv,
                         @RequestParam ("cardNumber")String cardNumber,
                         @RequestParam ("expirationDate")String expirationDate,
                         @RequestParam double amount){
        bankService.trasnferToShop(llogariaOnline,cvv,cardNumber,expirationDate,amount);
        return "Ok";
    }

}
