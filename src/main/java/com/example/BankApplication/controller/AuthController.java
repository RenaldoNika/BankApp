package com.example.BankApplication.controller;


import com.example.BankApplication.model.User;
import com.example.BankApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class AuthController {



    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String login(Model model){
        model.addAttribute("user",new User());

        return "loginRegister";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Nuk u regjistrua përdoruesi: "
                    + e.getMessage());
            return "loginRegister";
        }
    }

}
