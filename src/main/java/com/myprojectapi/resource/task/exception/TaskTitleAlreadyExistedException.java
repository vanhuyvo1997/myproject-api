package com.myprojectapi.resource.task.exception;

public class TaskTitleAlreadyExistedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskTitleAlreadyExistedException(String message) {
		super(message);
	}

}
