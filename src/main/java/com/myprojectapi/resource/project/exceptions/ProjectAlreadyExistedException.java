package com.myprojectapi.resource.project.exceptions;

public class ProjectAlreadyExistedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectAlreadyExistedException(String message) {
		super(message);
	}
}
