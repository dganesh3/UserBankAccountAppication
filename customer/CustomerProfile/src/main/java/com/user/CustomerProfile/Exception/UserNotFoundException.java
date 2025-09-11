package com.user.CustomerProfile.Exception;

public class UserNotFoundException extends RuntimeException {

@Override
public String getMessage() {
	
	return "user has not found ";
}
	
}
