package com.user.CustomerProfile.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBankAccountDTO {
	
//	@NotNull(message = "ID required", groups = Update.class)
	private Integer id;
//	@NotBlank(message = "Bank name is required")
//    @Size(min = 3, max = 30, message = "Bank name must be between 3 and 30 characters")
    private String bankName;

//    @NotBlank(message = "Account holder name is required")
//    @Size(min = 3, max = 30, message = "Account holder name must be between 3 and 30 characters")
    private String accountHolderName;

//    @NotNull(message = "Account number is required")
//    @Digits(integer = 18, fraction = 0, message = "Account number must be numeric and up to 18 digits")
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
    
    private Boolean deleted;
}
