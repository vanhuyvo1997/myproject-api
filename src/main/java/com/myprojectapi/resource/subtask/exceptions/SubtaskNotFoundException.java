package com.myprojectapi.resource.subtask.exceptions;

public class SubtaskNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubtaskNotFoundException(String message) {
		super(message);
	}
}
