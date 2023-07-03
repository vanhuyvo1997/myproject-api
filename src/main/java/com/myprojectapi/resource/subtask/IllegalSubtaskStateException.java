package com.myprojectapi.resource.subtask;

public class IllegalSubtaskStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2908482573523510906L;

	public IllegalSubtaskStateException(String message) {
		super(message);
	}
}
