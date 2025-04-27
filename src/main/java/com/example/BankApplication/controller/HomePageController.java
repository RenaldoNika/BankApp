package com.example.BankApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mybank")
public class HomePageController {

    @GetMapping("/home")
    public String homePage(){
        return "homePage";
    }

}
