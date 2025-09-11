package com.user.CustomerProfile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Register")
@Getter
@Setter
public class Signup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// First Name
	@NotBlank(message = "First name is required")
	private String firstname;

	// Last Name
	@NotBlank(message = "Last name is required")
	private String lastname;

	// Username (unique)
	@Column(unique = true, nullable = false)
	@NotBlank(message = "Username is required")
	private String username;

	// Mobile
	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
	private String mobile;

	// Email (unique)
	@Column(unique = true, nullable = false)
	@Email(message = "Enter a valid email")
	@NotBlank(message = "Email is required")
	private String email;

	// Password
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	// Confirm Password (only for validation, not stored in DB)
	@Transient
	private String confirmpassword;
	
	
	
	private Boolean emailVerifed= false;

	// --- Constructors ---
//	public Signup() {
//	}
//
//	public Signup(String firstname, String lastname, String username, String mobile, String email, String password) {
//		this.firstname = firstname;
//		this.lastname = lastname;
//		this.username = username;
//		this.mobile = mobile;
//		this.email = email;
//		this.password = password;
//	}

}
