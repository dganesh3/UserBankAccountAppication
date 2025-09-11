package com.user.CustomerProfile.entity;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CHSEC_UserBankAcc")
public class UserBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotBlank(message = "Bank name is required")
//    @Size(min = 3, max = 30, message = "Bank name must be between 3 and 30 characters")
    private String bankName;

//    @NotBlank(message = "Account holder name is required")
//    @Size(min = 3, max = 30, message = "Account holder name must be between 3 and 30 characters")
    private String accountHolderName;

//    @NotNull(message = "Account number is required")
//    @Digits(integer = 18, fraction = 0, message = "Account number must be numeric and up to 18 digits")
//    @Column(unique = true, nullable = false)
    private Long accountNumber;

//    @NotBlank(message = "Account type is required")
//    @Pattern(regexp = "^(Savings|Current|Salary|Fixed)$", message = "Account type must be Savings, Current, Salary, or Fixed")
    private String accountType;

//    @NotBlank(message = "IFSC code is required")
//    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    private String ifscCode;

//    @NotBlank(message = "Branch name is required")
    private String branchName;

//    @NotBlank(message = "Bank address is required")
    private String bankAddress;
    
//    @NotBlank(message="amount atleast 10000 is required")
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Transient
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "userbank_id", nullable = false)
    @JsonBackReference
    private UserBank userBank;

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
		UserBankAccount other = (UserBankAccount) obj;
		return Objects.equals(id, other.id);
	}

	
    
    
}
