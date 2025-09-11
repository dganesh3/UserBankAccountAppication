package com.user.CustomerProfile.Service.impl;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.CustomerProfile.Repository.SignupReporitory;
import com.user.CustomerProfile.entity.Signup;

@Service
public class SignupService 
{

	@Autowired
    private SignupReporitory signupReporitory;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    
    public Signup register(Signup signup) {
        Optional<Signup> existingUser = signupReporitory.findByUsername(signup.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Encrypt password
        signup.setEmailVerifed(true);
        signup.setPassword(passwordEncoder.encode(signup.getPassword()));
        return signupReporitory.save(signup);
    }

    
    public Signup login(String usernameOrEmail, String rawPassword) {
        Signup user = signupReporitory.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

   
    public void sendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);

        // Send via email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Signup OTP Verification");
        message.setText("Your OTP is: " + otp + " (valid for 5 minutes)");
        try {
        	
            mailSender.send(message);
            System.out.println("Email sent successfully.");
        } catch (MailAuthenticationException e) {
            System.err.println("SMTP Authentication failed: " + e.getMessage());
        } catch (MailSendException e) {
//        	throw new RuntimeException("Invalid email format");
          System.err.println("Failed to send email (invalid recipient or server issue): " + e.getMessage());
        } catch (MailException e) {
            System.err.println("General mail sending error: " + e.getMessage());
        }

    }

   
    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }
    
    
    public Boolean existsByEmail(String email)
    {
    	return signupReporitory.existsByEmail(email);
    }
}
