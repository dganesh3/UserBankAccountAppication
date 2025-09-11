package com.user.CustomerProfile.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@Entity
@Table(name = "CHSEC_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotBlank(message = "First name is required")
//    @Size(min = 1, max = 15, message = "First name must be between 1 and 15 characters")
    @Column(name = "first_name")
    private String firstName;

//    @NotBlank(message = "Last name is required")
//    @Size(min = 1, max = 15, message = "Last name must be between 1 and 15 characters")
    @Column(name = "last_name")
    private String lastName;
    
   
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    @Column(unique = true, nullable = false)
    private String email;

//    @NotNull(message = "Mobile number is required")
//    @Digits(integer = 10, fraction = 0, message = "Mobile number must be 10 digits")
    @Column(name = "mobile_number")
    private Long mobile;

//    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Pincode is required")
    @Min(value = 100000, message = "Pincode must be at least 6 digits")
    @Max(value = 999999, message = "Pincode must be at most 6 digits")
    private Integer pincode;

//    @NotBlank(message = "State is required")
    private String state;

//    @NotBlank(message = "Country is required")
    private String country;
    
    private String district;
    
    
    @Column(name = "profile_image")
    private String profileImage;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserBank> banks = new ArrayList<>();
    
    @OneToMany(mappedBy = "userContacts",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Contact> contacts=new ArrayList<Contact>();


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	public void addBank(UserBank bank) {
	    if (this.banks == null) {
	        this.banks = new ArrayList<>();
	    }
	    this.banks.add(bank);
	    bank.setUser(this);
	}




	
    
    
    
    
}
