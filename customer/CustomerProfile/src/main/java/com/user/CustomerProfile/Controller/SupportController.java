package com.user.CustomerProfile.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.CustomerProfile.Dto.SupportRequest;

@RestController
@RequestMapping("/support")
@CrossOrigin(origins = "http://localhost:4200")
public class SupportController {

	@Autowired
    private  JavaMailSender mailSender;

    

    @PostMapping
    public  Map<String, String> sendSupportRequest(@RequestBody SupportRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("derangulaganesh305@gmail.com"); // Replace with your email
        message.setSubject("New Support Request from " + request.getName());
        message.setText(
            "Name: " + request.getName() + "\n" +
            "Number: " + request.getEmail() + "\n" +
            "Mobile: " + request.getMobile() + "\n" +
            "Reason: " + request.getReason()
        );

        mailSender.send(message);
        return Map.of("message", "Support request sent successfully!");
    }
}