package com.example.BankApplication.controller;


import com.example.BankApplication.model.BankCard;
import com.example.BankApplication.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bankCard")
public class BankCardController {

    private BankCardService bankCardService;

    @Autowired
    public BankCardController(BankCardService bankCardService){
        this.bankCardService=bankCardService;
    }


    @GetMapping("/addCard")
    public String showBankCardForm(Model model) {
        model.addAttribute("bankCard", new BankCard());
        return "bankCredit"; // shfaq faqen me formën
    }

    @PostMapping("/add")
    public String addBankCard(@ModelAttribute BankCard bankCard,
                              @RequestParam String accountNumber,
                              Model model) {
        bankCardService.addBankCard(bankCard, accountNumber);
        model.addAttribute("message", "Karta u shtua me sukses!");
        return "redirect:/bankCard/addCard"; // ose vendndodhja që dëshiron
    }


}
