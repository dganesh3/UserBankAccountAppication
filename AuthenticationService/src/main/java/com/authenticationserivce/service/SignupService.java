package com.authenticationserivce.service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.authenticationserivce.entity.Signup;
import com.authenticationserivce.repository.SignupReporitory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.data.redis.core.StringRedisTemplate;


@Service
public class SignupService 
{

//	@Autowired
//    private SignupReporitory signupReporitory;
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    
//    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();
//
//    
//    public Signup register(Signup signup) {
//        Optional<Signup> existingUser = signupReporitory.findByUsername(signup.getUsername());
//        if (existingUser.isPresent()) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        // Encrypt password
//        signup.setEmailVerifed(true);
//        signup.setPassword(passwordEncoder.encode(signup.getPassword()));
//        return signupReporitory.save(signup);
//    }
//
//    
//    public Signup login(String usernameOrEmail, String rawPassword) {
//        Signup user = signupReporitory.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
//            return user;
//        }
//        return null;
//    }
//
//   
//    public void sendOtp(String email) {
//        String otp = String.format("%06d", new Random().nextInt(999999));
//        otpStore.put(email, otp);
//
//        // Send via email
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Signup OTP Verification");
//        message.setText("Your OTP is: " + otp + " (valid for 5 minutes)");
//        try {
//        	
//            mailSender.send(message);
//            System.out.println("Email sent successfully.");
//        } catch (MailAuthenticationException e) {
//            System.err.println("SMTP Authentication failed: " + e.getMessage());
//        } catch (MailSendException e) {
//        	throw new RuntimeException("Invalid email format");
//          System.err.println("Failed to send email (invalid recipient or server issue): " + e.getMessage());
//        } catch (MailException e) {
//            System.err.println("General mail sending error: " + e.getMessage());
//        }
//
//    }
//
//   
//    public boolean verifyOtp(String email, String otp) {
//        return otp.equals(otpStore.get(email));
//    }
//    
//    
//    public Boolean existsByEmail(String email)
//    {
//    	return signupReporitory.existsByEmail(email);
//    }
	
	 @Autowired
	    private SignupReporitory signupReporitory;

	    @Autowired
	    private JavaMailSender mailSender;

	    @Autowired
	    private StringRedisTemplate redisTemplate; // ✅ Redis instead of Map

	    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	    public Signup register(Signup signup) {
	        Optional<Signup> existingUser = signupReporitory.findByUsername(signup.getUsername());
	        if (existingUser.isPresent()) {
	            throw new RuntimeException("Username already exists");
	        }
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

	        // ✅ Store OTP in Redis with 5 min TTL
	        ValueOperations<String, String> ops = redisTemplate.opsForValue();
	        ops.set("otp:" + email, otp, 5, TimeUnit.MINUTES);

	        try {
	            MimeMessage mimeMessage = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

	            helper.setTo(email);
	            helper.setSubject("Signup OTP Verification");

	            // ✅ HTML body with header and footer
	            String body = """
	            <html>
	            <body style="font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
	                <table align="center" width="600" style="border:1px solid #ddd; border-radius:8px; background-color:#ffffff;">
	                    <tr style="background-color:#2c3e50;">
	                        <td style="padding:15px; text-align:center; color:#ffffff; font-size:20px; font-weight:bold;">
	                            Authentication Service
	                        </td>
	                    </tr>
	                    <tr>
	                        <td style="padding:20px; color:#333333; font-size:14px;">
	                            <p>Dear User,</p>
	                            <p>Thank you for registering with our service.</p>
	                            <p>Your One-Time Password (OTP) for email verification is:</p>
	                            <p style="text-align:center; font-size:22px; font-weight:bold; color:#d35400; background:#f4f4f4; padding:10px; border-radius:5px;">
	                                """ + otp + """
	                            </p>
	                            <p><b>⚠️ Please note:</b></p>
	                            <ul>
	                                <li>This OTP is valid for <b>5 minutes</b>.</li>
	                                <li>Do not share this code with anyone for security reasons.</li>
	                            </ul>
	                            <p>If you did not request this, please ignore this email.</p>
	                        </td>
	                    </tr>
	                    <tr style="background-color:#f1f1f1;">
	                        <td style="padding:15px; text-align:center; font-size:12px; color:#555555;">
	                            © 2025 Authentication Service | All rights reserved.
	                        </td>
	                    </tr>
	                </table>
	            </body>
	            </html>
	            """;

	            helper.setText(body, true); // true = HTML

	            mailSender.send(mimeMessage);
	            System.out.println("HTML Email with header/footer sent successfully.");

	        } catch (MessagingException e) {
	            System.err.println("Failed to send email: " + e.getMessage());
	        }
	    }

	    public boolean verifyOtp(String email, String otp) {
	        ValueOperations<String, String> ops = redisTemplate.opsForValue();
	        String storedOtp = ops.get("otp:" + email);
	        return otp.equals(storedOtp);
	    }

	    public Boolean existsByEmail(String email) {
	        return signupReporitory.existsByEmail(email);
	    }
}
