package com.user.CustomerProfile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CHSEC_USERBANK")
public class UserBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotBlank(message = "Bank name is required")
//    @Size(min = 2, max = 50)
    private String bankName;

//    @NotBlank(message = "Manager name is required")
//    @Size(min = 2, max = 50)
    private String managerName;

//    @NotBlank(message = "Branch name is required")
//    @Size(min = 2, max = 50)
    private String branchName;

//    @NotBlank(message = "Bank address is required")
    private String bankAddress;

//    @PastOrPresent(message = "Establishment date cannot be a future date")
//    @NotNull(message = "Established date is required")
    @Temporal(TemporalType.DATE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date establishedDate;

//    @NotBlank(message = "Contact number is required")
//    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
    private String phone;

//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
    private String email;

//    @NotBlank(message = "Bank type is required")
    private String bankType;
    @Transient
    private Boolean deleted;
    
    @OneToMany(mappedBy = "userBank", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserBankAccount> accounts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

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
		UserBank other = (UserBank) obj;
		return Objects.equals(id, other.id);
	}

	

	
    
    
    
    
}
