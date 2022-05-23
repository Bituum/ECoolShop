package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/mail")
    public void sendEmail() {

    }
}
