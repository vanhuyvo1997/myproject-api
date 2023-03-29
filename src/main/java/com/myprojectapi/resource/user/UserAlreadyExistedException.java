package com.myprojectapi.resource.user;

public class UserAlreadyExistedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistedException(String message) {
		super(message);
	}

}
