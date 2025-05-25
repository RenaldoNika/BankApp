package com.example.BankApplication.controller;

import com.example.BankApplication.model.ChatMessage;
import com.example.BankApplication.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/talk")
public class ChatController {


    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/chats/{recipient}")
    public String getPrivateChat(@PathVariable("recipient") String recipient,
                                 Model model, Principal principal) {
        String currentUser = principal.getName();
        List<ChatMessage> messages = chatMessageRepository.findChatBetween(currentUser,
                recipient);
        model.addAttribute("messages", messages);
        model.addAttribute("newMessage", new ChatMessage());
        model.addAttribute("recipient", recipient);
        return "private-chat";
    }


    @PostMapping("/chatprivat/{recipient}")
    public String sendMessage(@PathVariable String recipient,
                              @ModelAttribute("newMessage") ChatMessage message,
                              Principal principal) {
        message.setSender(principal.getName());
        message.setRecipient(recipient);
        chatMessageRepository.save(message);
        return "redirect:/talk/chats/" + recipient;
    }





}
