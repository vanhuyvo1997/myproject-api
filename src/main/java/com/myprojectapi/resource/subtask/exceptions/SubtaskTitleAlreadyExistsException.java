package com.myprojectapi.resource.subtask.exceptions;

public class SubtaskTitleAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7321978448081218411L;

	public SubtaskTitleAlreadyExistsException(String message) {
		super(message);
	}
}
