package com.user.CustomerProfile.Dto;

import lombok.Data;

@Data
public class LoginRequest {
	  private String usernameOrEmail;
      private String password;
}
