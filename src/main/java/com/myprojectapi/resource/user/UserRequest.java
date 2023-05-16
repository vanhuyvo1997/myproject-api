package com.myprojectapi.resource.user;

import com.myprojectapi.entity.User;

public record UserRequest(String name,String username, String password) {
	public User toUser() {
		return User.builder()
				.name(name)
				.username(username)
				.password(password)
				.build();
	}
}
