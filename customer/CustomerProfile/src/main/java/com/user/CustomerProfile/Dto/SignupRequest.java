package com.user.CustomerProfile.Dto;

import lombok.Data;

@Data
public class SignupRequest 
{
	 private String firstname;
	    private String lastname;
	    private String username;
	    private String mobile;
	    private String password;
	    private String email;

}
