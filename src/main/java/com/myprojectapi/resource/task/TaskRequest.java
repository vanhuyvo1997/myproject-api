package com.myprojectapi.resource.task;

import com.myprojectapi.entity.Task;

public record TaskRequest(String title, String description) {
	public Task toTask() {
		return Task.builder().title(title).description(description).build();
	}
}
