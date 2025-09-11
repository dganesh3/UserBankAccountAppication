package com.user.CustomerProfile.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.CustomerProfile.Dto.LoginRequest;
import com.user.CustomerProfile.Service.impl.SignupService;
import com.user.CustomerProfile.entity.Signup;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class SignupController 
{
	 @Autowired
	    private SignupService signupService;

	    
	    @PostMapping("/send-otp")
	    public ResponseEntity<String> sendOtp(@RequestParam String email) {
	        signupService.sendOtp(email);
	        return ResponseEntity.ok("OTP sent to " + email);
	    }

	    
	    @PostMapping("/verify-otp")
	    public ResponseEntity<String> verifyOtp(@RequestParam String email,
	                                            @RequestParam String otp) {
	        boolean valid = signupService.verifyOtp(email, otp);
	        if (valid) {
	            return ResponseEntity.ok("OTP verified successfully");
	        } else {
	            return ResponseEntity.badRequest().body("Invalid or expired OTP");
	        }
	    }

	    
	    @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody Signup signup,
	                                      @RequestParam String otp) {
	        boolean valid = signupService.verifyOtp(signup.getEmail(), otp);
	        if (!valid) {
	            return ResponseEntity.badRequest().body("OTP verification failed");
	        }

	        try {
	            Signup savedUser = signupService.register(signup);
	            return ResponseEntity.ok(savedUser);
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    
	    @PostMapping("/login")
	    public Signup login(@RequestBody LoginRequest request) {
	        Signup user = signupService.login(request.getUsernameOrEmail(), request.getPassword());
	        if (user != null) {
	            user.setPassword(null); // hide password in response
	            return user;
	        }
	        throw new RuntimeException("Invalid credentials");
	    }

	   
	    
	    @GetMapping("/check-email")
	    public ResponseEntity<Boolean> checkEmail(@RequestParam String email)
	    {
	    	boolean exists= signupService.existsByEmail(email);
	    	return  ResponseEntity.ok(exists);
	    }
	    
}
