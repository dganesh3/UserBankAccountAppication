package com.user.CustomerProfile.Dto;

import java.util.Date;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserBankDTO {
	
//	@NotNull(message = "ID required", groups = Update.class)
	private Integer id;
	
//	 @NotBlank(message = "Bank name is required")
//	    @Size(min = 2, max = 50)
	    private String bankName;

//	    @NotBlank(message = "Manager name is required")
//	    @Size(min = 2, max = 50)
	    private String managerName;

//	    @NotBlank(message = "Branch name is required")
//	    @Size(min = 2, max = 50)
	    private String branchName;

//	    @NotBlank(message = "Bank address is required")
	    private String bankAddress;

//	    @NotNull(message = "Established date is required")
//	    @Temporal(TemporalType.DATE)
//	    @PastOrPresent(message = "Establishment date cannot be in the future")
//	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	    private Date establishedDate;

//	    @NotBlank(message = "Contact number is required")
//	    @Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
	    private String phone;

//	    @NotBlank(message = "Email is required")
//	    @Email(message = "Invalid email format")
	    private String email;

//	    @NotBlank(message = "Bank type is required")
	    private String bankType;
	    private Boolean deleted;
	 
//	    @NotEmpty(message = "At least one account is required")
	    private List<UserBankAccountDTO> accounts;
}
