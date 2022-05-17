package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.util.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Value("${email.sender}")
    private String setFrom;

    @Override
    public void sendSimpleMessage(MessageWrapper wrapper) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(setFrom);
        message.setTo(wrapper.getTo());
        message.setSubject(wrapper.getHeader());
        message.setText(wrapper.getText());

        emailSender.send(message);
    }
}
