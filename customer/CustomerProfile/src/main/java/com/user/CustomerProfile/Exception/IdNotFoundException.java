package com.user.CustomerProfile.Exception;

public class IdNotFoundException extends RuntimeException {

	public IdNotFoundException(String string) {
		super(string);
	}

	@Override
	public String getMessage() {
		return "Id not available in DB";
	}
}
