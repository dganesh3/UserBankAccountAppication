package com.user.CustomerProfile.Dto;

import lombok.Data;

@Data
public class OtpRequest {
	private String email;
    private String otp;
}
