package com.user.CustomerProfile.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.user.CustomerProfile.entity.Contact;

import jakarta.validation.constraints.*;

@Getter
@Setter
public class UserRequestDTO {
	
	private Integer id;
//	@NotBlank(message = "First name is required")
//    @Size(min = 1, max = 15, message = "First name must be between 1 and 15 characters")
    private String firstName;

//    @NotBlank(message = "Last name is required")
//    @Size(min = 1, max = 15, message = "Last name must be between 1 and 15 characters")
    private String lastName;

//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
    private String email;

//    @NotNull(message = "Mobile number is required")
//    @Digits(integer = 10, fraction = 0, message = "Mobile number must be 10 digits")
    private Long mobile;

//    @NotBlank(message = "Address is required")
    private String address;

//    @NotNull(message = "Pincode is required")
//    @Min(value = 100000, message = "Pincode must be at least 6 digits")
//    @Max(value = 999999, message = "Pincode must be at most 6 digits")
    private Integer pincode;

//    @NotBlank(message = "State is required")
    private String state;

//    @NotBlank(message = "Country is required")
    private String country;

    private String profileImage; // Set after image upload
    private String district;
    
    
//    @NotEmpty(message = "At least one bank is required")
    private List<UserBankDTO> banks;
    
    private List<Contact> contacts;
    
    
    
    
    
    
}
