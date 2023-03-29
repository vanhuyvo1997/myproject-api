package com.myprojectapi.resource.user;

import com.myprojectapi.entity.User;

public record UserRequest(String username, String password) {
	public User toUser() {
		return User.builder().username(username).password(password).build();
	}
}
