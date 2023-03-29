package com.myprojectapi.resource.user;

import com.myprojectapi.entity.User;
import com.myprojectapi.entity.User.Role;

import lombok.Builder;

@Builder
public record UserDTO(Long id, String username, Role role) {

	public static UserDTO form(User user) {
		return UserDTO.builder().id(user.getId()).username(user.getUsername()).role(user.getRole()).build();
	}

}
